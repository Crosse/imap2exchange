package edu.jmu.email.conversion.jmu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SubjectTerm;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.ExchangeConversion;
import edu.yale.its.tp.email.conversion.PluggableConversionAction;
import edu.yale.its.tp.email.conversion.User;
import edu.yale.its.tp.email.conversion.imap.ImapServerFactory;

/**
 * <pre>
 * $Id: JmuPostConversionActionMirapointAddressBookImporter.java 96 2009-11-06 01:38:09Z seth $
 * 
 * Copyright (c) 2009 Seth Wright (wrightst@jmu.edu)
 * 
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED &quot;AS IS&quot; AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * </pre>
 * 
 */
public class JmuPreConversionAction extends PluggableConversionAction {
    
    private static Log logger = LogFactory.getLog(JmuPreConversionAction.class);
    private String proxyDomain;
    private List<String> ignoredDomains;
    private String ldapUserObject;
    private String netidAttribute;
    private String welcomeFile;
    private static final String FORWARDING_ADDRESS = "miForwardingAddress";
    private static final String DELIVERY_OPTION = "miDeliveryOption";
//    private boolean forwardAlreadySet;
    
    @Override
    public boolean perform(ExchangeConversion conv) {
        logger.info("Setting forwarding information for Mirapoint");
        if ( !setForwardingInformation(conv) ) {
            return false;
        }
        
        logger.info("Attempting to create the Welcome message");
        if ( !createWelcomeMessage(conv) ) {
            return false;
        }
        
        return true;
    }
    
    private boolean setForwardingInformation(ExchangeConversion conv) {
        User user = conv.getUser();
        String forwardingAddress;
        
        logger.info("----------------");
        logger.info("Current forwarding information:");
        if ((forwardingAddress = getCurrentForwardingValues(user)) == null) {
            return false;
        }
        logger.info("----------------");
        
        for (String domain : ignoredDomains) {
            domain = domain.trim().toLowerCase();
            if (forwardingAddress.trim().toLowerCase().contains(domain)) {
                logger.info(String.format("Forward currently set to an ignored domain: %s", domain));
                logger.info("Refusing to modify forwarding information.");
                return true;
            }
        }
        
        if (modifyForwarding(user)) {
            // Print out the new values, in order to verify that everything
            // got set right.
            logger.info("New forwarding information:");
            getCurrentForwardingValues(user);
            logger.info("----------------");
        } else {
            logger.warn("COULD NOT SET FORWARDING ADDRESS--MANUALLY SET IT AND RE-RUN THIS CONVERSION");
            logger.warn("(If you've already set the forward address, disregard this message)");
            conv.warnings++;
        }
        
        return true;
    }

    private synchronized String getCurrentForwardingValues(User user) {
        DirContext directory = JmuLdap.getInstance().getLdap();
        String filterExpr = String.format("(%s=%s)", netidAttribute, user.getUid());
        SearchControls cons = new SearchControls();
        cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String[] attrs = { FORWARDING_ADDRESS, DELIVERY_OPTION };
        cons.setReturningAttributes(attrs);
        SearchResult result = null;
        String forwardingAddress = "";

        try {
            NamingEnumeration<SearchResult> answer = directory.search(ldapUserObject, filterExpr, cons);
            if (answer.hasMore()) {
                result = (SearchResult) answer.next();
            }

            if (result != null) {
                logger.info(String.format("dn: %s", result.getNameInNamespace()));
                NamingEnumeration<? extends Attribute> ne = result.getAttributes().getAll();
                while (ne.hasMore()) {
                    Attribute attr = ne.next();
                    for (int i = 0; i < attr.size(); i++) {
                        logger.info(String.format("%s: %s", attr.getID(), attr.get(i)));
                        if (FORWARDING_ADDRESS.equalsIgnoreCase(attr.getID())) {
                            forwardingAddress = attr.get(i).toString();
                        }
                    }
                }
            } else {
                return null;
            }
        } catch (NamingException e) {
            logger.warn(String.format("Error getting current values:  %s", e.getMessage()));
            return null;
        }
        return forwardingAddress;
    }

    private synchronized boolean modifyForwarding(User user) {
        
        DirContext directory = JmuLdap.getInstance().getLdap();

        ModificationItem[] mods = new ModificationItem[2];

        Attribute forwardingAddress = new BasicAttribute("miForwardingAddress", String.format("%s@%s", user.getUid(), proxyDomain));
        Attribute deliveryOption = new BasicAttribute("miDeliveryOption", "forward");

        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, forwardingAddress);
        mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, deliveryOption);

        try {
            directory.modifyAttributes(String.format("%s=%s,%s", netidAttribute, user.getUid(), ldapUserObject), mods);
        } catch (NamingException e) {
            logger.warn("Error setting mail forward:  " + e.getMessage());
            return false;
        }

        logger.info(String.format("Modified forwarding attributes for %s", user.getUid()));
        return true;
    }
    
    private synchronized boolean createWelcomeMessage(ExchangeConversion conv) {

        File emlFile = new File(welcomeFile);
        InputStream source = null;
        try {
            source = new FileInputStream(emlFile);
        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage());
            conv.warnings++;
        }

        Store store = ImapServerFactory.getInstance().getImapStore(conv.getUser());
        Folder rootFolder = null;
        try {
            rootFolder = store.getDefaultFolder().getFolder("INBOX");
            if (!rootFolder.isOpen()) {
                rootFolder.open(Folder.READ_WRITE);
            }
            Message[] messages = new Message[1];

            messages[0] = new MimeMessage(null, source);
            SubjectTerm subjTerm = new SubjectTerm(messages[0].getSubject());
            if ((rootFolder.search(subjTerm)).length == 0) {
                rootFolder.appendMessages(messages);
                logger.info("Created welcome message");
            } else {
                logger.info("Welcome message already exists in mail store");
            }
        } catch (MessagingException e) {
            logger.warn("Could not create Welcome message");
            logger.warn(e.getMessage());
            conv.warnings++;
        } finally {
            if (rootFolder != null && rootFolder.isOpen()) {
                try {
                    rootFolder.close(false);
                } catch (MessagingException e) { }
            }
        }
        return true;
    }

    public String getWelcomeFile() {
        return welcomeFile;
    }

    public void setWelcomeFile(String welcomeFile) {
        this.welcomeFile = welcomeFile;
    }

    public String getProxyDomain() {
        return proxyDomain;
    }

    public void setProxyDomain(String proxyDomain) {
        this.proxyDomain = proxyDomain;
    }
    public String getLdapUserObject() {
        return ldapUserObject;
    }

    public void setLdapUserObject(String ldapUserObject) {
        this.ldapUserObject = ldapUserObject;
    }

    public String getNetidAttribute() {
        return netidAttribute;
    }

    public void setNetidAttribute(String netidAttribute) {
        this.netidAttribute = netidAttribute;
    }

    public void setIgnoredDomains(List<String> ignoredDomains) {
        this.ignoredDomains = ignoredDomains;
    }

    public List<String> getIgnoredDomains() {
        return ignoredDomains;
    }
}
