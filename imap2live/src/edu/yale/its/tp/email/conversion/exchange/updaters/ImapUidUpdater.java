package edu.yale.its.tp.email.conversion.exchange.updaters;

import java.io.*;
import java.util.*;
import javax.mail.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.microsoft.schemas.exchange.services._2006.types.*;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.exchange.*;

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
public class ImapUidUpdater extends MessageUpdater{
	
	private static Log logger = LogFactory.getLog(IsReadUpdater.class);
	
	public ImapUidUpdater(User user){
		super(user);
		this.type = "ImapUidUpdater";
	}
	
	public boolean needsUpdate(Message message, MessageType messageType) throws MessagingException, IOException{
		String euid = MessageUtil.getExtProp(messageType, MessageUtil.IMAP_UID_URI).getValue();
		String iuid = String.valueOf(((UIDFolder)message.getFolder()).getUID(message));
//		logger.debug("Checking uids: " + euid + " vs " + iuid);
		return !euid.equals(iuid);  
	}
	public List<MessageType> update(List<Message> messagesToUpdate, List<MessageType> messageTypesToUpdate) throws MessagingException, IOException{
		logger.debug("Updating " + messagesToUpdate.size() + " messages with ImapUidUpdater");
		return MessageUtil.updateMessagesImapUid(messageTypesToUpdate, messagesToUpdate);
	}

}
