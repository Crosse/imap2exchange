package edu.yale.its.tp.email.conversion;

import java.io.*;
import java.util.*;
import javax.mail.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.exchange.*;
import edu.yale.its.tp.email.conversion.imap.*;
import edu.yale.its.tp.email.conversion.exchange.updaters.*;
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
 * Synchronizes the messages in the folders.  Manages the Flagged deleted items in Deleted-Items folder.
 * 
 */
public class MessageSynchronizer {
	
	private static final Log logger = LogFactory.getLog(MessageSynchronizer.class);

	int maxMessageSize;
	int maxMessageGrpSize;
	
	MergedImapFolder imapFolder;
	BaseFolderType exchangeFolder;
	User user;

	// Master Lists
	List<Message> imapMessages;
	List<Message> imapDeletedMessages;
	List<MessageType> exchangeMessages;
	List<MessageType> exchangeDeletedMessages;
	
	// Imap Lists
	List<Message> imapMessagesToCreate = new ArrayList<Message>();
	List<Message> imapMessagesToCreateInDeletedItems = new ArrayList<Message>();
	List<Message> imapMessagesToUpdate = new ArrayList<Message>();
	List<Message> imapMessagesToUpdateInDeletedItems = new ArrayList<Message>();
	
	// Exchange Lists
	List<MessageType> exchangeMessagesToUpdate = new ArrayList<MessageType>();
	List<MessageType> exchangeMessagesToUpdateInDeletedItems = new ArrayList<MessageType>();
	List<MessageType> exchangeMessagesToDelete = new ArrayList<MessageType>();
	List<MessageType> exchangeMessagesToDeleteInDeletedItems = new ArrayList<MessageType>();

	// Creators and Updaters
	MessageCreator creator;
	DeletedItemsCreator deletedItemsCreator;
	List<MessageUpdater> updaters = new ArrayList<MessageUpdater>();
	List<MessageUpdater> deletedUpdaters = new ArrayList<MessageUpdater>();
	
	/**
	 * Defines the creators and updaters for the target folder
	 * @param user
	 */
	public MessageSynchronizer(){
	}
	
	/*
	 * This sorts the master list of all messages into sublists for processing
	 */
	protected void sort() throws MessagingException{
		
		imapMessages = imapFolder.getMessages();
		exchangeMessages = imapFolder.getMessageTypesFromThisFolder(MessageUtil.getMessages(user, exchangeFolder.getFolderId()));
		
		List<String> imapUids = MessageUtil.getMessageUIDs(user, imapMessages);
		List<String> exchangeUids = MessageUtil.getMessageTypeUIDs(exchangeMessages);

		imapMessagesToCreate.addAll(imapMessages);
		exchangeMessagesToDelete.addAll(exchangeMessages);
		
		Message foundMessage = null;
		MessageType foundMessageType = null;
		for(int i=0; i<imapUids.size(); i++){
			String imapUid = imapUids.get(i);
			if(exchangeUids.contains(imapUid)){
				foundMessage = imapMessages.get(i); 
				foundMessageType = exchangeMessages.get(exchangeUids.indexOf(imapUid));
				imapMessagesToUpdate.add(foundMessage);
				exchangeMessagesToUpdate.add(foundMessageType);
				imapMessagesToCreate.remove(foundMessage);
				exchangeMessagesToDelete.remove(foundMessageType);
			} 
		}

		// Process the Delete Items
		if(!exchangeFolder.getDisplayName().equals(ExchangeSystemFolders.DELETED_ITEMS.getFolderName())){
			
			imapDeletedMessages = imapFolder.getDeletedMessages();
			List<MessageType> exchangeDeletedMessagesFromThisFolder = imapFolder.getMessageTypesFromThisFolder(exchangeDeletedMessages);
			
			List<String> imapDeletedUids = MessageUtil.getMessageUIDs(user, imapDeletedMessages);
			List<String> exchangeDeletedUids = MessageUtil.getMessageTypeUIDs(exchangeDeletedMessagesFromThisFolder);

			imapMessagesToCreateInDeletedItems.addAll(imapDeletedMessages);
			exchangeMessagesToDeleteInDeletedItems.addAll(exchangeDeletedMessagesFromThisFolder);
			for(int i=0; i<imapDeletedUids.size(); i++){
				String imapDeletedUid = imapDeletedUids.get(i);
				if(exchangeDeletedUids.contains(imapDeletedUid)){
					foundMessage = imapDeletedMessages.get(i); 
					foundMessageType = exchangeDeletedMessagesFromThisFolder.get(exchangeDeletedUids.indexOf(imapDeletedUid));
					imapMessagesToUpdateInDeletedItems.add(foundMessage);
					exchangeMessagesToUpdateInDeletedItems.add(foundMessageType);
					imapMessagesToCreateInDeletedItems.remove(foundMessage);
					exchangeMessagesToDeleteInDeletedItems.remove(foundMessageType);
				}
			}
			
		} 
	}
	
	public void initialize(){
		
		creator = new MessageCreator();
		creator.setType("messages");
		creator.setUser(user);
		creator.setMaxMessageSize(maxMessageSize);
		creator.setMaxMessageGrpSize(maxMessageGrpSize);
		creator.setImapFolder(imapFolder);
		creator.setExchangeFolder(exchangeFolder);

		deletedItemsCreator = new DeletedItemsCreator();
		deletedItemsCreator.setType("messages(deleted)");
		deletedItemsCreator.setUser(user);
		deletedItemsCreator.setMaxMessageSize(maxMessageSize);
		deletedItemsCreator.setMaxMessageGrpSize(maxMessageGrpSize);
		deletedItemsCreator.setImapFolder(imapFolder);
		deletedItemsCreator.setExchangeFolder(exchangeFolder);
		
//		updaters.add(new IsImportantUpdater(user));
		updaters.add(new IsReadUpdater(user));

		// Uid Prop is not settable....
//		updaters.add(new ImapUidUpdater(user));
		
		// PrMsgStatus is not settable
//		updaters.add(new PrMsgStatusUpdater(user));
		
		deletedUpdaters.add(new IsReadUpdater(user));
//		deletedUpdaters.add(new IsImportantUpdater(user));
//		deletedUpdaters.add(new PrMsgStatusUpdater(user));
		
	}
	
	/**
	 * Synchronizes the folder's Messages
	 */
	public void sync() throws IOException, MessagingException {
		this.initialize();
		this.sort();
		this.createMessagesInExchange();
		this.updateMessagesInExchange();		
		this.deleteMessagesInExchange();
	}
	
	/**
	 * Creates the already sorted messages in exchange
	 * @throws IOException
	 * @throws MessagingException
	 */
	protected void createMessagesInExchange() throws IOException, MessagingException{
		
		logger.info("There are " + imapMessagesToCreate.size() + " messages to create in " + imapFolder.getFolderNames());
		for(Message message : imapMessagesToCreate){
			if(!message.getFolder().isOpen()) message.getFolder().open(Folder.READ_ONLY);
			creator.addMessage(message);
		}
		creator.flush();
		
		logger.info("There are " + imapMessagesToCreateInDeletedItems.size() + " messages to create in Deleted Items");
		for(Message message : imapMessagesToCreateInDeletedItems){
			if(!message.getFolder().isOpen()) message.getFolder().open(Folder.READ_ONLY);
			deletedItemsCreator.addMessage(message);
		}
		deletedItemsCreator.flush();
	}
	
	/**
	 * Deletes the already sorted messages in exchange
	 * @throws IOException
	 * @throws MessagingException
	 */
	protected void deleteMessagesInExchange() throws IOException, MessagingException{
		logger.info("There are " + exchangeMessagesToDelete.size() + " messages to delete in " + imapFolder.getFolderNames());
		MessageUtil.deleteMessages(user, exchangeMessagesToDelete, DisposalType.HARD_DELETE);
		logger.info("There are " + exchangeMessagesToDeleteInDeletedItems.size() + " messages to delete in Deleted Items");
		MessageUtil.deleteMessages(user, exchangeMessagesToDeleteInDeletedItems, DisposalType.HARD_DELETE);
	}
	
	/**
	 * runs the messages that were already created through the list of updaters for processing
	 * @throws IOException
	 * @throws MessagingException
	 */
	protected void updateMessagesInExchange()  throws IOException, MessagingException{
		
		if(imapMessagesToUpdate.size() != exchangeMessagesToUpdate.size()){
			logger.warn("Already converted messages counts don't match up");
			user.getConversion().warnings++;
		}
		logger.info("There are " + exchangeMessagesToUpdate.size() + " messages to check for updates in " + imapFolder.getFolderNames());
		MessageUpdater.processUpdaters(updaters, imapMessagesToUpdate, exchangeMessagesToUpdate);
		for(MessageUpdater updater : updaters){
			updater.updateMessages(imapMessagesToUpdate, exchangeMessagesToUpdate);
		}

		logger.info("There are " + exchangeMessagesToUpdateInDeletedItems.size() + " messages to check for updates in Deleted Items");
		MessageUpdater.processUpdaters(deletedUpdaters, imapMessagesToUpdateInDeletedItems, exchangeMessagesToUpdateInDeletedItems);
		for(MessageUpdater updater : deletedUpdaters){
			updater.updateMessages(imapMessagesToUpdateInDeletedItems, exchangeMessagesToUpdateInDeletedItems);
		}

	}

	public void setUser(User user) {
		this.user = user;
	}
	public void setImapFolder(MergedImapFolder imapFolder) {
		this.imapFolder = imapFolder;
	}
	public void setExchangeFolder(BaseFolderType exchangeFolder) {
		this.exchangeFolder = exchangeFolder;
	}
	public void setExchangeDeletedMessages(List<MessageType> exchangeDeletedMessages) {
		this.exchangeDeletedMessages = exchangeDeletedMessages;
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
	
}
