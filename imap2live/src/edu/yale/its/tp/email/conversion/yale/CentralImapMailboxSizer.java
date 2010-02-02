package edu.yale.its.tp.email.conversion.yale;

import edu.yale.its.tp.email.conversion.User;
import edu.yale.its.tp.email.conversion.imap.ImapMailboxSizer;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
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
 */
public class CentralImapMailboxSizer implements ImapMailboxSizer {

	private static final Log logger = LogFactory.getLog(CentralImapMailboxSizer.class);
	
	public static final String UID = "uid";
	public static final String PO = "po";
	private static int SUCCESS = 200;
	
	int multiplier = 1024;
	String url;
	
	/* (non-Javadoc)
	 * @see edu.yale.its.tp.email.conversion.imap.ImapMailboxSizer#getMailboxSize(edu.yale.its.tp.email.conversion.User)
	 */
	public long getMailboxSize(User user) {
		long ret = -1;

		String value = null;
		try{
			value = getSize(user.getUid(), user.getSourceImapPo());
			ret = Long.parseLong(value);
			if(ret > 0) ret = ret * (long)multiplier;
		} catch (NumberFormatException nfe){
			logger.error("Error Parsing the value from http: " + value);
		} catch (HttpException e) {
			logger.error("Error retreiving MailboxSize from http.", e);
		}
		return ret;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	@SuppressWarnings("deprecation")
	protected String getSize(String uid, String po) throws HttpException { 
	
	    String ret = "-1";

	    PostMethod post = new PostMethod(url);
	    post.addParameter(UID, uid);
	    post.addParameter(PO, po);
	    logger.info("Requesting Size from: " + url + "?uid=" + uid + "&po=" + po);
	    HttpClient httpclient = new HttpClient();
	    try {
	    	if(httpclient.executeMethod(post) == SUCCESS){
	    		ret = post.getResponseBodyAsString().trim();
	    	}
	    } catch (IOException ioe){
	    	logger.error("Error getting size from http.", ioe);
	    } finally {
	        post.recycle();
	    }
	    return ret;
	}

}
