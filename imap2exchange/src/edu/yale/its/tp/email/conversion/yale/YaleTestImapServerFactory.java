package edu.yale.its.tp.email.conversion.yale;

import java.util.*;
import javax.mail.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.imap.*;
import edu.yale.its.tp.email.conversion.trust.*;

/**
 * 
 * <pre>
 * Copyright (c) 2000-2003 Yale University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL YALE UNIVERSITY OR ITS EMPLOYEES BE
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
 * includes software developed by Yale University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "Yale" and "Yale University" must not be used to endorse
 * or promote products derived from this software.
 * </pre>
 *

 *
 * I would pool this object, but that will do no good...
 * A new connection is needed every time...
 */
public class YaleTestImapServerFactory  extends ImapServerFactory{

	static Log logger = LogFactory.getLog(YaleTestImapServerFactory.class);

	/** 
	 * Get me an IMAP Store
	 * @param user
	 * @return
	 */
	public Store getImapStore(User user){

		final ImapServer server = super.getImapServer(user.getSourceImapPo());
		if(server == null
		|| server.getPort() == null
		|| server.getProtocol() == null
		|| server.getAdminPwd() == null)
			throw new RuntimeException("User[" + user.getUid() + "]'s sourcePo[" + user.getSourceImapPo() + "] is not defined in config.properties correctly.");

		Store imapStore = null;
		
		try {
			
			// configure the jvm to use the jsse security.
//			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

			// JavaMail stuff
	    	Properties javamailProps = new Properties(System.getProperties());
            String prot = server.getProtocol();
        	javamailProps.setProperty("mail.store.protocol", prot);
	        javamailProps.setProperty("mail." + prot + ".host", server.getUri());
            javamailProps.setProperty("mail." + prot + ".port", server.getPort());
            if(prot.equals(ImapServer.IMAPS_PROTOCOL)){
		    	javamailProps.setProperty("mail." + prot + ".socketFactory.class", AllTrustingSocketFactory.class.getName());
		    	javamailProps.setProperty("mail." + prot + ".socketFactory.fallback", "false");
		    	javamailProps.setProperty("mail." + prot + ".socketFactory.port", server.getPort());
		    	javamailProps.setProperty("mail." + prot + ".partialfetch", "false");
            }

            
	        // SASL stuff 
            if(server.isSasl()){
		        javamailProps.setProperty("mail." + prot + ".sasl.enable", "true");
		        javamailProps.setProperty("mail." + prot + ".sasl.mechanisms", "plain");
		        javamailProps.setProperty("mail." + prot + ".sasl.authorizationid", user.getUid());
            }
            
		    /* This looks weird, but this causes the nop-sasl plain auth to use use both
        	 * the authorized and authenicated uid in the base64 encoded authentication string. 
        	 * which is exactly what I want for UWash.*/            	
//	        javamailProps.setProperty("mail." + prot + ".sasl.authorizationid", user.getUid());

            final String uid = user.getUid();
	        javax.mail.Authenticator adminAuth= new javax.mail.Authenticator(){
	        	protected javax.mail.PasswordAuthentication  getPasswordAuthentication(){
	                if(server.isSasl()){
	                	logger.debug("Using SASL");
	                	return new javax.mail.PasswordAuthentication (server.getAdminUid(), server.getAdminPwd());
	                } else {
	                	logger.debug("NOT Using SASL");
	                	return new javax.mail.PasswordAuthentication (uid, server.getAdminPwd());
	                }
	        	}
	        };
	        
	        // Connect to the Configured Server
	        Session session = Session.getInstance(javamailProps, adminAuth);
	        imapStore = session.getStore();
		    imapStore.connect();

		} catch (Exception e) {
			throw new RuntimeException("Error Connecting to " + user.getUid() + "@" + user.getSourceImapPo(), e);
		}
		return imapStore;
		
	}

}
