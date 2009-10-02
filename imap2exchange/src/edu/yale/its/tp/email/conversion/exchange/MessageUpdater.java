package edu.yale.its.tp.email.conversion.exchange;

import java.io.*;
import java.util.*;
import javax.mail.*;
//import org.apache.log4j.*;
import edu.yale.its.tp.email.conversion.*;
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
 * These are updates that update a group of messages based on weather thay pass the needs update method.
 * This class will only update messages that need updating
 */
public abstract class MessageUpdater {

//	private static final Logger logger = Logger.getLogger(MessageUpdater.class);
	
	protected String type;
	protected User user;
	protected List<Integer> updatedMessageIndexes = new ArrayList<Integer>();
	
	protected MessageUpdater(User user){
		this.user = user;
	}
	
	/**
	 * How to Perform the update
	 * @param user
	 * @param destMessages
	 * @param messages
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	protected abstract List<MessageType> update(List<Message> messages, List<MessageType> messageTypes) throws MessagingException, IOException;

	/**
	 * Return true if the message needs to be updated. 
	 * @param message
	 * @param messageType
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	protected abstract boolean needsUpdate(Message message, MessageType messageType) throws MessagingException, IOException;
	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}
	/**
	 * 
	 * @param type
	 */
	protected void setType(String type) {
		this.type = type;
	}
	/**
	 * 
	 * @param i
	 */
	protected void addUpdatedMessageIndex(int i){
		updatedMessageIndexes.add(i);
	}
	/**
	 * 
	 * @return
	 */
	public List<Integer> getUpdatedMessageIndexes(){
		return this.updatedMessageIndexes;
	}
	
	/**
	 * 
	 * @param updaters
	 * @param messages
	 * @param messageTypes
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void processUpdaters(List<MessageUpdater> updaters
			                         , List<Message> messages
			                         , List<MessageType> messageTypes) throws MessagingException, IOException{
		Message message = null;
		MessageType messageType = null;
		for(int i=0; i<messages.size(); i++){
			for(MessageUpdater updater : updaters){
				message = messages.get(i);
				messageType = messageTypes.get(i);
				if(updater.needsUpdate(message, messageType)){
					updater.addUpdatedMessageIndex(i);
				}
			}
		}
	}

	/**
	 *  Updates the messages and copy changed items back to the master list
	 *  This is needed because exchange has a changeId associated with every MessageId
	 *  So you can only make subsequent changes on MessageTypes with a valid changeId
	 * 
	 * @param messages
	 * @param messageTypes
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void updateMessages(List<Message> messages, List<MessageType> messageTypes) throws MessagingException, IOException{

		if(updatedMessageIndexes.isEmpty()) return;

		List<Message> messagesToUpdate = new ArrayList<Message>();
		List<MessageType> messageTypesToUpdate =  new ArrayList<MessageType>();
		for(int i : updatedMessageIndexes){
			messagesToUpdate.add(messages.get(i));
			messageTypesToUpdate.add(messageTypes.get(i));
		}
		
		List<MessageType> updatedMessages = update(messagesToUpdate, messageTypesToUpdate);
		for(int i=0; i<updatedMessageIndexes.size(); i++){
			messageTypes.set(updatedMessageIndexes.get(i), updatedMessages.get(i));
		}
	}
	
}
