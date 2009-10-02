package edu.yale.its.tp.email.conversion;

import java.util.*;

import javax.mail.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.util.*;
import edu.yale.its.tp.email.conversion.imap.*;
import edu.yale.its.tp.email.conversion.event.*;
import edu.yale.its.tp.email.conversion.exchange.*;

import com.microsoft.schemas.exchange.services._2006.types.*;

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
 * This class is the runnable class that will be queue up in the Conversion
 * Manager for running this users conversion.  This Class can also be used to 
 * Clean a users exchange account, hard deleting all mail found everywhere!!!
 */
 public class ExchangeConversion implements Runnable{

	private static final Log logger = LogFactory.getLog(ExchangeConversion.class);
	private static final boolean FAILED = false;
	
	List<ExchangeConversionCompleteListener> completeListeners = Collections.synchronizedList(new ArrayList<ExchangeConversionCompleteListener>());
	List<MessageType> exchangeDeletedItems;
	List<String> excludedImapFolders;
	List<String> includedImapFolders;
	FolderAltNames altNames;
	Folder imapRootFolder;
	FolderIdType rootExchangeFolderId;
	User user;
	Map<String, PluggableConversionAction> pluggableConversionActions = new HashMap<String, PluggableConversionAction>();
	int maxMessageSize;
	int maxMessageGrpSize;
	boolean isCleaner = false;
	long start = -1;
	long end = -1;
	public int warnings = 0;
	boolean success;
	boolean finalRun;
	
	long messagesMovedCnt;
	long messagesMovedSize;
	
	Report report;

	/**
	 *  method called by thread to start the conversion 
	 */
	public void run() {
		report = new Report();
		start = System.currentTimeMillis();
		try{
			if(performUserSetupAction() == FAILED)
				throw new RuntimeException("Error performing User Setup for " + this.getId());
			if(isCleaner) {
				cleanExchangeAccount();
				success = true;
			} else {
				success =  performPreConversionAction()
			            && convert()
			            && performPostConversionAction();
			}
		} catch (Exception e){
			logger.error("Error running conversion", e);
			success = false;
		}
		if(!success){
			logger.info("Conversion for " + user.getUid() + " did NOT complete unsuccessfully.");
		} else {
			logger.info(messagesMovedCnt + " messages [" 
					  + ByteFormatter.of(messagesMovedSize).format(messagesMovedSize) 
					  + "] were moved." );
			if(warnings > 0){
				logger.info("Conversion for " + user.getUid() + " completed with " + warnings + " warnings.");
			} else {
				logger.info("Conversion for " + user.getUid() + " completed successfully.");
			}
		}
		end = System.currentTimeMillis();
		if(logger.isDebugEnabled()){
			logger.debug("Timer Report:\n" + report.toString());
		}
	}
	
	public void clean() {
		
		if(this.isCleaner()) return;
		
		completeListeners = null;
		pluggableConversionActions = null;
		exchangeDeletedItems = null;
		altNames = null;
		try{
			if(imapRootFolder != null && imapRootFolder.isOpen()){
				imapRootFolder.close(false);
			}
		} catch (MessagingException me){
			logger.debug("Error closing imap folder: " + imapRootFolder.getFullName() + " - " + me.getMessage());
		}
	}
	
	/**
	 * Performs user defined action prior to conversion
	 */
	public boolean performUserSetupAction(){
		return performConversionAction("userSetupAction");
	}

	/**
	 * Performs user defined action prior to conversion
	 */
	public boolean performPreConversionAction(){
		return performConversionAction("preConversionAction");
	}

	/**
	 * Performs user defined action after to conversion
	 */
	public boolean performPostConversionAction(){
		return performConversionAction("postConversionAction");
	}

	/**
	 * actual method that runs the two conversion actions
	 * @param actionClassConfigKey
	 */
	protected boolean performConversionAction(String actionName){
		PluggableConversionAction action = pluggableConversionActions.get(actionName);
		if(action == null) {
			logger.debug("No Puggable Conversion Action defined for: " + actionName);
			return true;
		}
		return action.perform(this);
	}
	
	public void addPluggableConversionAction(String actionName, PluggableConversionAction action){
		pluggableConversionActions.put(actionName, action);
	}
	
	/**
	 * Cleans an exchange acount, hard deleting all Mail
	 * Called from run() if isClean is true.
	 *
	 */
	public void cleanExchangeAccount(){
		logger.info("Cleaning Exchange Account: " + user.getUid());
		List<BaseFolderType> exchangeFolders = FolderUtil.getRootMailFolders(user);
		for(BaseFolderType folder : exchangeFolders){
			if(ExchangeSystemFolders.isExchangeSystemFolders(folder.getDisplayName())){
				logger.info("Cleaning System Folder: " + folder.getDisplayName());
				List<MessageType> messages = MessageUtil.getMessages(user, folder.getFolderId());
				MessageUtil.deleteMessages(user, messages, DisposalType.HARD_DELETE);
			} else {
				logger.info("Deleting non-System Folder: " + folder.getDisplayName());
				FolderUtil.deleteFolder(user, folder.getFolderId());
			}
		}
	}
	
	/**
	 * Converts the mail from an IMAP source to an Exchange target
	 *
	 */
	public boolean convert(){
		Store imapStore = null;
		try{
			/*
			 * Get Connection to IMAP Server to start the Conversion
			 * I don't pool the connection because there is no advantage
			 * I have to get a new connection for each user...
			 */
			imapStore = ImapServerFactory.getInstance().getImapStore(user);
			
			MergedImapFolder root = new MergedImapFolder();
			root.setConv(this);
			Folder rootImapFolder = imapStore.getDefaultFolder();
			this.imapRootFolder = rootImapFolder;
			root.addFolder(rootImapFolder);
			root.setExcludedImapFolders(excludedImapFolders);
			root.setIncludedImapFolders(includedImapFolders);
			List<MergedImapFolder> imapFolders = root.getChildFolders();
			
			List<BaseFolderType> exchangeFolders = FolderUtil.getRootMailFolders(user);
			rootExchangeFolderId = FolderUtil.getFolderTypeFromList(exchangeFolders, "INBOX").getParentFolderId();
			BaseFolderType deletedItems = FolderUtil.getFolderTypeFromList(exchangeFolders, ExchangeSystemFolders.DELETED_ITEMS.getFolderName());
			setExchangeDeletedItems(MessageUtil.getMessages(user, deletedItems.getFolderId()));
			
			FolderSynchronizer folderSyncer = new FolderSynchronizer();
			folderSyncer.setConv(this);
			folderSyncer.mergeFolders(imapFolders, exchangeFolders, rootExchangeFolderId);
			
		} catch (MessagingException me){
			logger.error("Error Communicating with IMAP Server", me);
			return false;
		} catch (Exception e){
			logger.error("Error converting user: " + getId(), e);
			return false;
		} finally {
			try{
				if(imapStore != null) imapStore.close();
			} catch (Exception e){
				logger.debug("Exception Closing Imap Connection to " + user.getSourceImapPo(), e);
			}
		}
		return true;
	}
	
	public List<MessageType> getExchangeDeletedItems() {
		return exchangeDeletedItems;
	}

	public void setExchangeDeletedItems(List<MessageType> exchangeDeletedItems) {
		this.exchangeDeletedItems = exchangeDeletedItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Cleans up the conversion to release resources
	 *
	 */
	public void flush(){
		this.setExchangeDeletedItems(null);
	}
	
	/**
	 * This flag tells the conversion clean the exchange users account
	 * @return
	 */
	public boolean isCleaner() {
		return isCleaner;
	}

	/**
	 * This flag tells the conversion clean the exchange users account
	 * @return
	 */
	public void setCleaner(boolean isCleaner) {
		this.isCleaner = isCleaner;
	}

	/**
	 * handle to the root imap folder
	 * @return
	 */
	public Folder getImapRootFolder() {
		return imapRootFolder;
	}

	/**
	 * Used for merging folders of different names
	 * @return
	 */
	public FolderAltNames getAltNames() {
		return altNames;
	}

	/**
	 * Used for merging folders of different names
	 * @return
	 */
	public void setAltNames(FolderAltNames altNames) {
		this.altNames = altNames;
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

	public Map<String, PluggableConversionAction> getPluggableConversionActions() {
		return pluggableConversionActions;
	}

	public void setPluggableConversionActions(
			Map<String, PluggableConversionAction> pluggableConversionActions) {
		this.pluggableConversionActions = pluggableConversionActions;
	}

	/**
	 * reports false if not finished.
	 * @return
	 */
	public String getStatus() {
		if(start == 0) return "queued";
		if(end == 0) return "running";
		if(success){
			if(warnings > 0)
				return "warning";
			else 
				return "success";
		}
		return "error";
	}

	public long getEnd() {
		return end;
	}

	public long getStart() {
		return start;
	}
	
	public String getId(){
		return user.getUid() + "@" + user.getSourceImapPo();
	}
	
	public void addMessagesCnt(long cnt){
		messagesMovedCnt += cnt;
	}

	public void addMessagesSize(long size){
		messagesMovedSize += size;
	}

	public long getMessagesMovedCnt() {
		return messagesMovedCnt;
	}

	public long getMessagesMovedSize() {
		return messagesMovedSize;
	}

	public FolderIdType getRootExchangeFolderId() {
		return rootExchangeFolderId;
	}

	public boolean isFinalRun() {
		return finalRun;
	}

	public void setFinalRun(boolean finalRun) {
		this.finalRun = finalRun;
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

	/**
	 * @return the report
	 */
	public Report getReport() {
		return report;
	}

	/**
	 * @param report the report to set
	 */
	public void setReport(Report report) {
		this.report = report;
	}
	
	
}
