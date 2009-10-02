package edu.yale.its.tp.email.conversion.util;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.yale.*;

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
 */
public class GenericUserSetupAction extends PluggableConversionAction {

	private static Log logger = LogFactory.getLog(GenericUserSetupAction.class);
	
	private static final String NOT_FOUND = "NOT_FOUND";

	String netidAttribute;
	String upnAttribute;
	String smtpAttribute;
	String userObject;
    
	@Override
	public boolean perform(ExchangeConversion conv) {
		// This populates the required Fields for the user
		updateUser(conv.getUser());
		return true;
	}

    /**
     * Get the Email Address
     * @param user
     * @return
     */
	public void updateUser(User user){
		
		String upn = NOT_FOUND;
		String email = NOT_FOUND;
    	DirContext directory = null;

    	try {
    		
    		directory = YaleAD.getInstance().getAD();
    		Attributes match = new BasicAttributes(true); 
    		match.put(new BasicAttribute(netidAttribute, user.getUid()));
    		String[] returnAttribs = {upnAttribute, smtpAttribute};

    		NamingEnumeration answer = directory.search(userObject, match, returnAttribs);
    		SearchResult result = null;
    		
    		if(answer.hasMore()){
    			result = (SearchResult)answer.next();
	    		if(answer.hasMore()){
	    	    	logger.warn("More than one  " + userObject + " record found for " + user.getUid());
	    	    	user.getConversion().warnings++;
	    		}
    		} 
    		
    		if(result != null){
	            upn = result.getAttributes().get(upnAttribute).get().toString();
	            email = result.getAttributes().get(smtpAttribute).get().toString();
		    	logger.info("UPN for " + user.getUid() + ": " + upn);
		    	logger.info("SMTP for " + user.getUid() + ": " + email);
    		}
    		

	    } catch (Exception e) {
        	logger.error("Error Communicating with LDAP Server for [" + user.getUid()+"]", e);
	    } finally {
	    	try{ directory.close();} catch (Exception e){/*ignore*/}
	    }
	    
	    if(upn == NOT_FOUND){
	    	throw new RuntimeException("userPrincipleName(UPN) not found for " + user.getUid());
	    }
	    if(email == NOT_FOUND){
	    	throw new RuntimeException("primarySMTPAddress not found for " + user.getUid());
	    }
	    
    	user.setPrimarySMTPAddress(email);
    	user.setUPN(upn);

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
	
}
