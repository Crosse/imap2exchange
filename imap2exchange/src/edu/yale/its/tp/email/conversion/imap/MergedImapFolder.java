package edu.yale.its.tp.email.conversion.imap;

import javax.mail.*;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.mail.imap.*;
import edu.yale.its.tp.email.conversion.*;
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
 * Used to abstract merged Folders in Exchange, since it is not case-sensitive 
 */
public class MergedImapFolder {
	

	private static final Log logger = LogFactory.getLog(MergedImapFolder.class);

	String name;
	boolean subscribed;
	List<Message> messages;
	List<Message> deletedMessages;
	List<Folder> folders = new ArrayList<Folder>();
	List<String> folderUids = new ArrayList<String>();
	MergedImapFolder parent;

	static final FetchProfile IMAP_FETCH_PROFILE = new FetchProfile();
	static{
		IMAP_FETCH_PROFILE.add(FetchProfile.Item.FLAGS);
		IMAP_FETCH_PROFILE.add(FetchProfile.Item.CONTENT_INFO);
		IMAP_FETCH_PROFILE.add(IMAPFolder.FetchProfileItem.HEADERS);
		IMAP_FETCH_PROFILE.add(UIDFolder.FetchProfileItem.UID);
	}
	
	/**
	 * Get all messages in folder group not flagged deleted
	 * @return
	 * @throws MessagingException 
	 */
	public List<Message> getMessages() throws MessagingException {
		loadMessagesMetaData();
		return messages;
	}
	/**
	 * Get all messages in folder group flagged deleted
	 * @return
	 * @throws MessagingException 
	 */
	public List<Message> getDeletedMessages() throws MessagingException {
		loadMessagesMetaData();
		return deletedMessages;
	}
	/**
	 * Returns the folderName of the Exchange Created Folder and a 
	 * list of Folders that are sources of the messages.
	 * @return
	 * @throws MessagingException 
	 */
	public String getFolderNames() throws MessagingException {
		if(folders.size() > 1){
			StringBuilder sb = new StringBuilder();
			sb.append(getFullName()).append("[");
			for(Folder folder : folders){
				sb.append(folder.getFullName()).append(", ");
			}
			sb.delete(sb.length() - 2, sb.length());
			sb.append("]");
			return sb.toString();
		} else {
			return getFullName();
		}
	}
	
	/**
	 * Returns the Name of the folder that will be created in Exchange
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/** 
	 * Sets the name... Be carefull here...
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
	
	public String getFullName() throws MessagingException{
		if(this.parent == null)
			return getName();
		else
			return parent.getFullName() + ExchangeConversion.getConv().getImapRootFolder().getSeparator() + getName();
	}

	/**
	 * Maybe some day I can use this in Exchange
	 * @return
	 */
	public boolean isSubscribed() {
		return subscribed;
	}
	/**
	 * Maybe some day I can use this in Exchange
	 * @return
	 */
	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
	/**
	 * returns the list of folders
	 * @return
	 */
	public List<Folder> getFolders() {
		return folders;
	}
	/**
	 * Add a folder to the group
	 * @param folder
	 * @throws MessagingException
	 */
	public void addFolder(Folder folder) throws MessagingException{
		this.folders.add(folder);
		logger.info("Indexing Messages in " + folder.getFullName() + " for Merged Folder: " + getFullName());
		folderUids.add(MessageUtil.getFolderUid(folder));
	}

	protected void loadMessagesMetaData() throws MessagingException {
		if (messages == null && deletedMessages == null) {

			messages = new ArrayList<Message>();
			deletedMessages = new ArrayList<Message>();

			for (Folder folder : folders) {
			    if(folder.getType() == Folder.HOLDS_MESSAGES ||
			       folder.getType() == (Folder.HOLDS_MESSAGES | Folder.HOLDS_FOLDERS)){
				    if (!folder.isOpen()) {
				        folder.open(Folder.READ_ONLY);
				    }
					Message[] messageArray = folder.getMessages();
					Report.getReport().start(Report.IMAP_META);
					folder.fetch(messageArray, IMAP_FETCH_PROFILE);
					Report.getReport().stop(Report.IMAP_META);
					for (Message message : messageArray) {
						if (ImapUtil.isDeleted(message)) {
							deletedMessages.add(message);
						} else {
							messages.add(message);
						}
					}
				} else {
				    logger.debug("Folder " + folder.getFullName() + " does not hold messages");
				}
			}
		}
	}

	/**
	 * determines if this MessageType is from this folder
	 * @param message
	 * @return
	 */
	public boolean originatedFromThisFolder(MessageType message){
		for(String uid : folderUids){
			String messageUid = MessageUtil.getMessageTypeUID(message); 
			if(messageUid == null) continue;
			if(messageUid.startsWith(uid)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Filters out messageTypes only from this folder
	 * @param messages
	 * @return
	 */
	public List<MessageType> getMessageTypesFromThisFolder(List<MessageType> messages){
		List<MessageType> returnMessages = new ArrayList<MessageType>();
		returnMessages.addAll(messages);
		for(MessageType message : messages){
			if(!originatedFromThisFolder(message)){
				returnMessages.remove(message);
			}
		}
		return returnMessages;
	}
	
	public MergedImapFolder getParent() {
		return parent;
	}
	public void setParent(MergedImapFolder parent) {
		this.parent = parent;
	}
	
	public void close(boolean expunge) {
		for(Folder folder : folders){
			try{
				if(folder.isOpen()){
					folder.close(expunge);
				}
			} catch (MessagingException me){
				logger.debug("Error closing imap folder: " + folder.getFullName() + " - " + me.getMessage());
			}
		}
	}

}
