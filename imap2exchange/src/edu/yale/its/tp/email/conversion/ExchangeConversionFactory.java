package edu.yale.its.tp.email.conversion;

import java.util.*;
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
public class ExchangeConversionFactory {
	
	int maxMessageSize;
	int maxMessageGrpSize;
	boolean isCleaner;
	FolderAltNames altNames;
	List<String> excludedImapFolders;
	List<String> includedImapFolders;
	Map<String, PluggableConversionAction> pluggableConversionActions = new HashMap<String, PluggableConversionAction>();

	static ExchangeConversionFactory singleton;
	
	public ExchangeConversionFactory(){
		singleton = this;
	}

	public int getMaxMessageSize() {
		return maxMessageSize;
	}

	public void setMaxMessageSize(int maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}
	
	public int getMaxMessageGrpSize() {
		return maxMessageGrpSize;
	}

	public void setMaxMessageGrpSize(int maxMessageGrpSize) {
		this.maxMessageGrpSize = maxMessageGrpSize;
	}

	public static ExchangeConversionFactory getInstance(){
		return singleton;
	}
	
	public boolean isCleaner() {
		return isCleaner;
	}

	public void setCleaner(boolean isCleaner) {
		this.isCleaner = isCleaner;
	}

	public FolderAltNames getAltNames() {
		return altNames;
	}

	public void setAltNames(FolderAltNames altNames) {
		this.altNames = altNames;
	}
	
	public ExchangeConversion makeExchangeConversion(User user){
		ExchangeConversion conv = new ExchangeConversion();
		conv.setUser(user);
		conv.setCleaner(isCleaner);
		conv.setAltNames(altNames);
		conv.setMaxMessageSize(maxMessageSize);
		conv.setMaxMessageGrpSize(maxMessageGrpSize);
		conv.setPluggableConversionActions(pluggableConversionActions);
		conv.setExcludedImapFolders(excludedImapFolders);
		conv.setIncludedImapFolders(this.includedImapFolders);
		user.setConversion(conv);
		return conv;
	}

	public Map<String, PluggableConversionAction> getPluggableConversionActions() {
		return pluggableConversionActions;
	}

	public void setPluggableConversionActions(
			Map<String, PluggableConversionAction> pluggableConversionActions) {
		this.pluggableConversionActions = pluggableConversionActions;
	}

	public List<String> getExcludedImapFolders() {
		return excludedImapFolders;
	}

	public void setExcludedImapFolders(List<String> excludedImapFolders) {
		this.excludedImapFolders = excludedImapFolders;
	}
	
	/**
	 * @return the includedImapFolders
	 */
	public List<String> getIncludedImapFolders() {
		return includedImapFolders;
	}

	/**
	 * @param includedImapFolders the includedImapFolders to set
	 */
	public void setIncludedImapFolders(List<String> includedImapFolders) {
		this.includedImapFolders = includedImapFolders;
	}



}
