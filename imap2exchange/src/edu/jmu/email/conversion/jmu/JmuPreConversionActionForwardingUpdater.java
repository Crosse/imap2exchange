package edu.jmu.email.conversion.jmu;

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
public class JmuPreConversionActionForwardingUpdater extends PluggableConversionAction {
    
    private static Log logger = LogFactory.getLog(JmuPreConversionActionForwardingUpdater.class);
    private String proxyDomain;
    
    @Override
    public boolean perform(ExchangeConversion conv) {
        User user = conv.getUser();
        
        if (!getCurrentValues(user)) {
            return false;
        }
        
        if (modifyForwarding(user)) {
            // Print out the new values, in order to verify that everything
            // got set right.
            getCurrentValues(user);
        } else {
            logger.error("Could not set forwarding");
            return false;
        }
        
        return true;
    }

    private synchronized boolean getCurrentValues(User user) {
        DirContext directory = JmuLdap.getInstance().getLdap();
        String filterExpr = String.format("(cn=%s)", user.getUid());
        SearchControls cons = new SearchControls();
        cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String[] attrs = { "miForwardingAddress", "miDeliveryOption" };
        cons.setReturningAttributes(attrs);
        SearchResult result = null;

        try {
            NamingEnumeration<SearchResult> answer = directory.search("cn=" + user.getUid(), filterExpr, cons);
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
                    }
                }
            } else { 
                return false;
            }
        } catch (NamingException e) {
            logger.warn(e.getMessage());
            return false;
        }
        return true;
    }

    private synchronized boolean modifyForwarding(User user) {
        
        DirContext directory = JmuLdap.getInstance().getLdap();

        ModificationItem[] mods = new ModificationItem[2];

        Attribute forwardingAddress = new BasicAttribute("miForwardingAddress", String.format("%s@%s", user.getUid(), proxyDomain));
        Attribute deliveryOption = new BasicAttribute("miDeliveryOption", "forward");

        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, forwardingAddress);
        mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, deliveryOption);

        try {
            directory.modifyAttributes(String.format("cn=%s", user.getUid()), mods);
        } catch (NamingException e) {
            logger.warn("Error setting mail forward:  " + e.getMessage());
            return false;
        }

        logger.info(String.format("Modified forwarding attributes for %s", user.getUid()));
        return true;
    }

    public String getProxyDomain() {
        return proxyDomain;
    }

    public void setProxyDomain(String proxyDomain) {
        this.proxyDomain = proxyDomain;
    }


}
