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
import com.sun.mail.imap.IMAPFolder;

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
	Matcher includeExcludeMatcher;
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
	
	private static ThreadLocal<ExchangeConversion> conv = new ThreadLocal<ExchangeConversion>();
	
	/**
	 *  method called by thread to start the conversion 
	 */
	public void run() {
		
		logger.debug("Start run()");

		conv.set(this);

		try{
			new Report();
			start = System.currentTimeMillis();

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
			logger.info("Conversion for " + user.getUid() + " did NOT complete successfully.");
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
			logger.debug("Timer Report:\n" + Report.getReport().toString());
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
			logger.debug("No Pluggable Conversion Action defined for: " + actionName);
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
		List<BaseFolderType> exchangeFolders = FolderUtil.getRootMailFolders();
		for(BaseFolderType folder : exchangeFolders){
			if(ExchangeSystemFolders.isExchangeSystemFolders(folder.getDisplayName())){
				logger.info("Cleaning System Folder: " + folder.getDisplayName());
				List<MessageType> messages = MessageUtil.getMessages(folder.getFolderId());
				MessageUtil.deleteMessages(messages, DisposalType.HARD_DELETE);
			} else {
				logger.info("Deleting non-System Folder: " + folder.getDisplayName());
				FolderUtil.deleteFolder(folder.getFolderId());
			}
		}
	}
	
	/**
	 * Converts the mail from an IMAP source to an Exchange target
	 *
	 */
	public boolean convert(){
		
		logger.debug("Start convert()");
		
		Store imapStore = null;
		try{
			
			/* Get IMAP Folders	 */
			imapStore = ImapServerFactory.getInstance().getImapStore(user);
			imapRootFolder = imapStore.getDefaultFolder();
			Map<String, Folder> imapFolders = new TreeMap<String, Folder>();
			getAllChildImapFolders(imapFolders, imapRootFolder);

			/* Get Exchange Folders */
			Map<String, BaseFolderType> exchangeFolders = new HashMap<String, BaseFolderType>();
			BaseFolderIdType rootExchangeFolderId = FolderUtil.getRootFolderId();
			getAllChildExchangeFolders(exchangeFolders, rootExchangeFolderId, null);
			
			if(logger.isDebugEnabled())	{
				if(exchangeFolders.isEmpty())
					logger.debug("Root Exchange Mail Folders are Empty");
			}
			
			/* Filter Imap Folders to includes/excludes */
			filterImapFolders(imapFolders);
			
			/* MergeImapFolders */
			Map<String, MergedImapFolder> mergedImapFolders = mergeFolders(imapFolders);
			
			BaseFolderType deletedItems = exchangeFolders.get(ExchangeSystemFolders.DELETED_ITEMS.getFolderName().toLowerCase());
			setExchangeDeletedItems(MessageUtil.getMessages(deletedItems.getFolderId()));
			
			FolderSynchronizer folderSyncer = new FolderSynchronizer();
			folderSyncer.setConv(this);
			folderSyncer.syncFolders(mergedImapFolders, exchangeFolders);
			
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
	
	// Recursively add all the folders in this mailbox to a list.
	protected void getAllChildImapFolders(Map<String, Folder> folders, Folder parent) throws MessagingException{
		for(Folder folder : parent.list()){
			folders.put(folder.getFullName(), folder);
			getAllChildImapFolders(folders, folder);
		}
	}
	
	// Recursively add all the folders in this mailbox to a list.
	protected void getAllChildExchangeFolders(Map<String, BaseFolderType> exchangeFolders, BaseFolderIdType parentId, String parentName) throws MessagingException{
		List<BaseFolderType> exchangeMailFolders = FolderUtil.getChildFolders(parentId);
		for(BaseFolderType folder : exchangeMailFolders){
			if(parentName != null)
				folder.setDisplayName(parentName + imapRootFolder.getSeparator() + folder.getDisplayName());
			logger.debug("Found pre-existing exchange folder: " + folder.getDisplayName());
			exchangeFolders.put(folder.getDisplayName().toLowerCase(), folder);
			getAllChildExchangeFolders(exchangeFolders, folder.getFolderId(), folder.getDisplayName());
		}
	}
	
	public void filterImapFolders(Map<String, Folder> folders){
		
		if(getExcludedImapFolders() == null
		&& getIncludedImapFolders() == null){
			logger.debug("syncing all folder because there are no include/excludes defined.");
			return;
		}

		// Find the ones to remove
		List<String> pathsToRemove = new ArrayList<String>(); 
		for(String path : folders.keySet()){
			if(isExcluded(folders.get(path).getFullName()) && !isIncluded(folders.get(path).getFullName())){
				pathsToRemove.add(path);
				continue;
			}
		}
		
		// Actually remove them...
		for(String path : pathsToRemove){
		    folders.remove(path);
		}
		
		// Clear the removeBuffer and test opening them...
		pathsToRemove = new ArrayList<String>();
		for(String folderName : folders.keySet()){
			Folder folder = folders.get(folderName);
			if(!canOpenFolder(folder)){
				pathsToRemove.add(folderName);
			}
		}
		
		// Actually remove the ones I can't open...
		for(String path : pathsToRemove){
			folders.remove(path);
		}

	}
	
	protected boolean isExcluded(String folderName) {
		if (this.getExcludedImapFolders() == null)
			return false;
		
		logger.debug("Comparing folder \"" + folderName + "\" to the excluded list");
        
		for (String exclude : this.getExcludedImapFolders()) {
		    if (includeExcludeMatcher == null) {
		        logger.warn("includeExcludeMatcher == null");
		    }
			if (includeExcludeMatcher.matches(exclude, folderName)) {
				logger.info("Excluding " + folderName + " because it is in the exclusion list.");
				return true;
			}
		}
		return false;
	}

	protected boolean isIncluded(String folderName) {
		if (this.getIncludedImapFolders() == null) {
			return false;
		}
		for (String include : this.getIncludedImapFolders()) {
			if (includeExcludeMatcher.matches(include, folderName)) {
				logger.info("Including " + folderName + " because it is in the inclusion list.");
				return true;
			}
		}
		logger.info("Excluding " + folderName + " because it is not in the inclusion list.");
		return false;
	}

	/**
	 * This merges folders of the same name and if any folder of the same name
	 * with the same parent is subscribed, the exchange folder will be subscribed.
	 */
	protected Map<String, MergedImapFolder> mergeFolders(Map<String, Folder> imapFolders) throws MessagingException{
		Map<String, MergedImapFolder> mergedFolders = new TreeMap<String, MergedImapFolder>();
		for(String folderName : imapFolders.keySet()){
			Folder folder = imapFolders.get(folderName);
			// Special Case for Alt Folder Names
			if(getAltNames() != null){
				FolderAltName altName = getAltNames().getFolderAltName(folder.getFullName()); 
				if(altName != null){
					mergeFolder(mergedFolders, altName.getExchangeFolderName(), folder);
					continue;
				}
			}
			mergeFolder(mergedFolders, folder.getFullName(), folder);
		}
		return mergedFolders;
	}

	/**
	 * I create new MergedImapFolders here to control what is merged with what...
	 * @param mergedFolders
	 * @param mergedFolderName
	 * @param folder
	 * @throws MessagingException
	 */
	protected void mergeFolder(Map<String, MergedImapFolder> mergedFolders, String mergedFolderName, Folder folder) throws MessagingException{
		/*  The toLowerCase merges folders of the same name different case, because exchange is case insensitive. */
		MergedImapFolder mergedFolder = mergedFolders.get(mergedFolderName.toLowerCase());
		if(mergedFolder == null){
			mergedFolder = new MergedImapFolder();
			mergedFolder.setName(getFolderName(mergedFolderName));
			mergedFolder.setParent(getParentFolder(mergedFolderName, mergedFolders));
			mergedFolder.setSubscribed(folder.isSubscribed());
			mergedFolder.addFolder(folder);
			mergedFolders.put(mergedFolderName.toLowerCase(), mergedFolder);
		} else {
			mergedFolder.setSubscribed(mergedFolder.isSubscribed() || folder.isSubscribed());
			mergedFolder.addFolder(folder);
		}
	}
	
	/**
	 * Return the leaf foldername
	 * @param fullname
	 * @return
	 * @throws MessagingException 
	 */
	protected String getFolderName(String fullname) throws MessagingException{
		String name  = fullname.substring(fullname.lastIndexOf(imapRootFolder.getSeparator()) + 1);
		logger.debug("Get FolderName for: " + fullname + " returned " + name);
		return name;
	}
	
	/**
	 * I can do this because I use a treemap to store the folders causing them 
	 * to be created in exchange in the right order.
	 * @param folderPath
	 * @param mergedFolders
	 * @return
	 * @throws MessagingException 
	 */
	protected MergedImapFolder getParentFolder(String fullname, Map<String, MergedImapFolder> mergedFolders) throws MessagingException{
		int idx = fullname.lastIndexOf(imapRootFolder.getSeparator());
		if(idx == -1)
			return null;
		String parentFolderName = fullname.substring(0, idx);
		logger.debug("Get ParentFolderName for: " + fullname + " returned " + parentFolderName);
		MergedImapFolder parentFolder = mergedFolders.get(parentFolderName.toLowerCase());
		return parentFolder;		
	}
	

	/**
	 * Javamail/UW will return all files in the user home directory This is an
	 * easy way to filter out the noncompliant folders
	 */
	protected boolean canOpenFolder(Folder folder) {

		Folder imapFolder = folder;
		logger.debug("Trying to open imapFolder : " + folder.getFullName());
		try {
			long uid = ((IMAPFolder) imapFolder).getUIDValidity();
			if (uid > 1) {
				imapFolder.open(Folder.READ_ONLY);
				return true;
			} else {
				logger.debug("invalid folder uidvalidity: " + uid);
			}
		} catch (MessagingException me) {
			logger.debug("Folder or File " + user.getUid() + "." + folder.getFullName() + " was not converted because it is not a mail folder:" + me.getMessage());
		}
		return false;
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
	 * @return the conv
	 */
	public static ExchangeConversion getConv() {
		return conv.get();
	}

	/**
	 * @param conv the conv to set
	 */
	public static void setConv(ExchangeConversion conv) {
		ExchangeConversion.conv.set(conv);
	}

	/**
	 * @return the includeExcludeMatcher
	 */
	public Matcher getIncludeExcludeMatcher() {
		return includeExcludeMatcher;
	}

	/**
	 * @param includeExcludeMatcher the includeExcludeMatcher to set
	 */
	public void setIncludeExcludeMatcher(Matcher includeExcludeMatcher) {
		this.includeExcludeMatcher = includeExcludeMatcher;
	}
	
	
	
}
