package edu.jmu.email.conversion.jmu;

import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.ExchangeConversion;
import edu.yale.its.tp.email.conversion.PluggableConversionAction;
import edu.yale.its.tp.email.conversion.User;

/**
 * 
 * <pre>
 * Copyright (c) 2000-2003 James Madison University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL JAMES MADISON UNIVERSITY OR ITS EMPLOYEES BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED, THE COSTS OF
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 * 
 * Redistribution and use of this software in source or binary forms,
 * with or without modification, are permitted, provided that the
 * following conditions are met:
 * 
 * 1. Any redistribution must include the above copyright notice and
 * disclaimer and this list of conditions in any related documentation
 * and, if feasible, in the redistributed software.
 * 
 * 2. Any redistribution must include the acknowledgment, "This product
 * includes software developed by James Madison University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "JMU" and "James Madison University" must not be used to endorse
 * or promote products derived from this software.
 * </pre>
 * 
 * 
 * 
 */
public class LiveUserSetupAction extends PluggableConversionAction {

    private static Log logger = LogFactory.getLog(LiveUserSetupAction.class);

    private static final String NOT_FOUND = "";

    String netidAttribute;
    String ldapNetidAttribute;
    String upnAttribute;
    String smtpAttribute;
    String mailHostAttribute;
    String userObject;
    String ldapUserObject;
    String somUserObject;

    @Override
    public boolean perform(ExchangeConversion conv) {
        // This populates the required Fields for the user
        updateUser(conv.getUser());
        return true;
    }

    /**
     * Get the Email Address
     * 
     * @param user
     * @return
     */
    public void updateUser(User user) {

        String email = String.format("%s@%s", user.getUid(), JmuSite.getInstance().getTargetMailDomain());
        user.setPrimarySMTPAddress(email);
        user.setUPN(email);

        logger.info("Email = " + email);
        // Get the mailHost for the user from eDir.
        updateSourceImapPo(user);

    }

    private void updateSourceImapPo(User user) {
        logger.info("Finding mailhost");
        String mailHost = NOT_FOUND;
        DirContext directory = null;

        try {

            directory = JmuLdap.getInstance().getLdap();
            // Attributes match = new BasicAttributes(true);
            // match.put(new BasicAttribute(netidAttribute, user.getUid()));
            String filterExpr = String.format("(%s=%s)", ldapNetidAttribute, user.getUid());

            SearchControls cons = new SearchControls();
            cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String[] attrs = { mailHostAttribute };
            cons.setReturningAttributes(attrs);
            NamingEnumeration<SearchResult> answer = directory.search(ldapUserObject, filterExpr, cons);
            SearchResult result = null;

            if (answer.hasMore()) {
                logger.info("Found user in eDirectory.");
                result = (SearchResult) answer.next();
                if (answer.hasMore()) {
                    logger.warn("More than one  " + ldapUserObject + " record found for " + user.getUid());
                    user.getConversion().warnings++;
                }
            }

            if (result != null) {
                mailHost = result.getAttributes().get(mailHostAttribute).get().toString();
                logger.info("mailHost for " + user.getUid() + ": " + mailHost);
            }

        } catch (Exception e) {
            logger.error("Error Communicating with LDAP Server for [" + user.getUid() + "]", e);
        } finally {
            try {
                directory.close();
            } catch (Exception e) {/* ignore */
            }
        }

        if (mailHost == NOT_FOUND) {
            throw new RuntimeException("mailHost not found for " + user.getUid());
        }

        user.setSourceImapPo(mailHost);
    }

    public String getNetidAttribute() {
        return netidAttribute;
    }

    public void setNetidAttribute(String netidAttribute) {
        this.netidAttribute = netidAttribute;
    }

    public String getSmtpAttribute() {
        return smtpAttribute;
    }

    public void setSmtpAttribute(String smtpAttribute) {
        this.smtpAttribute = smtpAttribute;
    }

    public String getSomUserObject() {
        return somUserObject;
    }

    public void setSomUserObject(String somUserObject) {
        this.somUserObject = somUserObject;
    }

    public String getUpnAttribute() {
        return upnAttribute;
    }

    public void setUpnAttribute(String upnAttribute) {
        this.upnAttribute = upnAttribute;
    }

    public String getUserObject() {
        return userObject;
    }

    public void setUserObject(String userObject) {
        this.userObject = userObject;
    }

    public String getMailHostAttribute() {
        return mailHostAttribute;
    }

    public String getLdapUserObject() {
        return ldapUserObject;
    }

    public void setLdapUserObject(String ldapUserObject) {
        this.ldapUserObject = ldapUserObject;
    }

    public String getLdapNetidAttribute() {
        return ldapNetidAttribute;
    }

    public void setLdapNetidAttribute(String ldapNetidAttribute) {
        this.ldapNetidAttribute = ldapNetidAttribute;
    }

    public void setMailHostAttribute(String mailHostAttribute) {
        this.mailHostAttribute = mailHostAttribute;
    }
}
