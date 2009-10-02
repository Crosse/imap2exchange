package edu.yale.its.tp.email.conversion.yale;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.User;
import edu.yale.its.tp.email.conversion.imap.*;

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
public class MedImapMailboxSizer implements ImapMailboxSizer{

	private static final Log logger = LogFactory.getLog(MedImapMailboxSizer.class); 
	
	String emailFile;
	String omegaFile;
	int multiplier = 1024; 
	
	public long getMailboxSize(User user) {
		long ret = -1;
		String value = getSize(user.getUid(), user.getSourceImapPo());
		try{
			ret = Long.parseLong(value);
			if(ret > 0) ret = ret * (long)multiplier;
		} catch (NumberFormatException nfe){
			logger.error("Error Parsing the value from File: " + value);
		}
		return ret;
	}
	
	public String getEmailFile() {
		return emailFile;
	}
	
	public void setEmailFile(String emailFile) {
		this.emailFile = emailFile;
	}
	
	public String getOmegaFile() {
		return omegaFile;
	}

	public void setOmegaFile(String omegaFile) {
		this.omegaFile = omegaFile;
	}
	
	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	protected String getSize(String uid, String po){
		if(po.substring(0, 5).equalsIgnoreCase("email")){
			return getSizeFromFile(uid, emailFile);
		} else if (po.substring(0, 5).equalsIgnoreCase("omega")){
			return getSizeFromFile(uid, omegaFile);
		} else {
			throw new RuntimeException("Not a medical PO: " + po);
		}
	}
	
	protected String getSizeFromFile(String uid, String filename){
	    BufferedReader in = null;
	    try {
    	  logger.info("Getting Size from: " + filename + " where uid=" + uid);
	      in = new BufferedReader(new FileReader(filename));
	      String line;
	      int i = 0;
	      while ((line = in.readLine()) != null) {
	    	  if(i++ < 2) continue;
	    	  StringTokenizer toks = new StringTokenizer(line, " ");
	    	  String currUid = toks.nextToken();
	    	  if(uid.equals(currUid.trim())){
		    	  toks.nextToken();  // ignore second one...
		    	  return toks.nextToken();
	    	  }
	      }
	    } catch (FileNotFoundException fnf) {
	    	logger.error("Error Reading Med|Omega FileSize File, File Not Found: " + filename, fnf);
	    } catch (IOException ioe) {
	    	logger.error("Error Reading Med|Omega FileSize File: " + filename, ioe);
	    } finally {
		      try{in.close();}catch(Exception e){}
	    }
	    return "-1";
	}

//	public static void main(String[] args) throws Exception{
//		MedImapMailboxSizer sizer = new MedImapMailboxSizer();
//		sizer.setEmailFile("config/quota.email");
//		sizer.setOmegaFile("config/quota.omega");
//		String size = sizer.getSize("mac256", "omega.med.yale.edu");
//		logger.info("Size: " + size);
//	}
	
	
}
