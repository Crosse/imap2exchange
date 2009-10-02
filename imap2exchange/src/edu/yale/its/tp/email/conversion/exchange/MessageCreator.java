package edu.yale.its.tp.email.conversion.exchange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.imap.*;
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
 */
public class MessageCreator {

	private static final Log logger = LogFactory.getLog(MessageCreator.class);

	int maxMessageSize;
	int maxMessageGrpSize;
	
	int grpSize = 0;
	List<Message> groupToMove = new ArrayList<Message>();
	MergedImapFolder imapFolder;
	BaseFolderType exchangeFolder;
	User user;
	String type;

	public BaseFolderType getExchangeFolder() {
		return exchangeFolder;
	}
	public void setExchangeFolder(BaseFolderType exchangeFolder) {
		this.exchangeFolder = exchangeFolder;
	}
	public MergedImapFolder getImapFolder() {
		return imapFolder;
	}
	public void setImapFolder(MergedImapFolder imapFolder) {
		this.imapFolder = imapFolder;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public void addMessage(Message messageToMove) throws MessagingException, IOException{
		
		int size = messageToMove.getSize();
		
		if(size >= maxMessageGrpSize){
			
			if(size >= maxMessageSize){
				logger.warn("NOT Moving single " + type + " bigger then max message size (" 
						  + maxMessageSize + " bytes) from folder " + imapFolder.getFolderNames() + ", subject:" + messageToMove.getSubject()
						  + "(" + size + ")");
				user.getConversion().warnings++;
			} else {
			
				List<Message> baMessage = new ArrayList<Message>();
				baMessage.add(messageToMove);
				if(logger.isInfoEnabled())
					logger.info("Moving single " + type + " bigger then max message group size (" 
							  + maxMessageGrpSize + " bytes)from folder " + imapFolder.getFolderNames() + ", subject:" + messageToMove.getSubject()
							  + "(" + size + ")");
				int moved = createMessagesInExchange(user, baMessage, exchangeFolder.getFolderId());
				if(moved != 1){
					logger.warn("Error Moving single " + type + " bigger then max message group size (" 
							  + maxMessageGrpSize + " bytes)from folder " + imapFolder.getFolderNames() + ", subject:" + messageToMove.getSubject()
							  + "(" + size + ")");
					user.getConversion().warnings++;
				}
			}
							
		} else if(size + grpSize >= maxMessageGrpSize){
		
			// First Move the current group to the server 
			if(logger.isInfoEnabled())
				logger.info("Moving group of " + groupToMove.size()  
						  + " " + type + " (" + grpSize + " bytes) from folder " + imapFolder.getFolderNames() + ".");
			int moved = createMessagesInExchange(user, groupToMove, exchangeFolder.getFolderId());
			if(moved != groupToMove.size()){
				logger.warn("Error Moving group of " + groupToMove.size()  
						  + " " + type + " (" + grpSize + " bytes) from folder " + imapFolder.getFolderNames() + ", only " + moved + " " + type + " were successful.");
				user.getConversion().warnings++;
			}
			
			// Reset the group and counter
			groupToMove = new ArrayList<Message>();
			groupToMove.add(messageToMove);
			grpSize = size;
			
		} else {
			
			//logger.debug("Adding message to GroupToMove to Exchange.");
			groupToMove.add(messageToMove);
			grpSize += size;
			
		}

	}

	protected int createMessagesInExchange(User user, List<Message> messagesToMove, FolderIdType destFolderId) throws MessagingException, IOException {
		
		/*
		 * Create the Messages in Exchange
		 */
		List<MessageType> movedMessages = create(user, messagesToMove, destFolderId);
		
		if(movedMessages == null){
			return 0;
		} else {
			return movedMessages.size();
		}
		
	}
	
	protected List<MessageType> create(User user, List<Message> messagesToMove, BaseFolderIdType destFolderId) throws MessagingException, IOException {
		return MessageUtil.createMessagesInExchange(user, messagesToMove, destFolderId);
	} 
	
	public void flush() throws MessagingException, IOException{
		if(!groupToMove.isEmpty()){
			if(logger.isInfoEnabled())
				logger.info("Moving last group of " + groupToMove.size()  
						  + " " + type + " (" + grpSize + " byes) from folder " + imapFolder.getFolderNames() + ".");
			int moved = createMessagesInExchange(user, groupToMove, exchangeFolder.getFolderId());
			if(moved != groupToMove.size()){
				logger.warn("Error Moving group of " + groupToMove.size()  
						  + " " + type + " (" + grpSize + " bytes) from folder " + imapFolder.getFolderNames() + ", only " + moved + " " + type + " were successful.");
				user.getConversion().warnings++;
			}
		}
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
