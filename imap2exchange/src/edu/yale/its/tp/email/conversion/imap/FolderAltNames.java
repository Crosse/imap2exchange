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
package edu.yale.its.tp.email.conversion.imap;

import java.util.*;

import edu.yale.its.tp.email.conversion.util.Matcher;
import edu.yale.its.tp.email.conversion.util.StringEqualsMatcher;

public class FolderAltNames {

	List<FolderAltName> altNames;
	Matcher altNamesMatcher = new StringEqualsMatcher();

	public List<FolderAltName> getAltNames() {
		return altNames;
	}

	public void setAltNames(List<FolderAltName> altNames) {
		this.altNames = altNames;
	}
	
	public FolderAltName getFolderAltName(String imapFolderName){
		for(FolderAltName altName : altNames){
			if(contains(altName,  imapFolderName)){
				return altName;
			} 
		}
		return null;
	}
	
	protected boolean contains(FolderAltName altName, String imapFolderName){
		for(String folderNameExpression : altName.getImapFolderNames()){
			if(altNamesMatcher.matches(folderNameExpression, imapFolderName)){
				return true;
			}
		}
		return false; 
	}
	
	/**
	 * @return the altNamesMatcher
	 */
	public Matcher getAltNamesMatcher() {
		return altNamesMatcher;
	}

	/**
	 * @param altNamesMatcher the altNamesMatcher to set
	 */
	public void setAltNamesMatcher(Matcher altNamesMatcher) {
		this.altNamesMatcher = altNamesMatcher;
	}
	
}
