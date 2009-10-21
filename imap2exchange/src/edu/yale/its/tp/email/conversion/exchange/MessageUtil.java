package edu.yale.its.tp.email.conversion.exchange;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.Holder;
import java.io.*;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.util.*;
import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.exchange.flags.*;
import com.microsoft.schemas.exchange.services._2006.types.*;
import com.microsoft.schemas.exchange.services._2006.messages.*;

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
 * This class is used to perform static calls to the 
 * Exchange Server to perform Message related operations
 */
public class MessageUtil {
	
	public static final com.microsoft.schemas.exchange.services._2006.types.ObjectFactory typesObjectFactory = new com.microsoft.schemas.exchange.services._2006.types.ObjectFactory(); 
	public static final com.microsoft.schemas.exchange.services._2006.messages.ObjectFactory messagesObjectFactory = new com.microsoft.schemas.exchange.services._2006.messages.ObjectFactory(); 

	private static String IPM_NOTE = "IPM.Note";
	
	public static final String UID_SEPERATOR = ":";
	
	private static final Log logger = LogFactory.getLog(MessageUtil.class);
	static JAXBElement<? extends BasePathToElementType> CONVERSION_UID_PATH; 
	static JAXBElement<? extends BasePathToElementType> IS_READ_PATH; 
	static JAXBElement<? extends BasePathToElementType> IS_IMPORTANT_PATH;
	static JAXBElement<? extends BasePathToElementType> IMAP_UID_PATH;
	public static final PathToExtendedFieldType IMAP_UID_URI;
	public static final String IMAP_UID_TAG = "0xe23"; 

	public static PathToExtendedFieldType conversionUidUri = new PathToExtendedFieldType();
	public static PathToExtendedFieldType deleteFlagUri = new PathToExtendedFieldType();
	static{
		UUID uidPropUID = UUID.nameUUIDFromBytes("edu.yale.its.tp.imap2exchange.conv".getBytes());
		conversionUidUri.setPropertySetId(uidPropUID.toString());
		conversionUidUri.setPropertyName("uid");
		conversionUidUri.setPropertyType(MapiPropertyTypeType.STRING);
		CONVERSION_UID_PATH = typesObjectFactory.createExtendedFieldURI(conversionUidUri);
		
		PathToUnindexedFieldType seenPath = new PathToUnindexedFieldType();
		seenPath.setFieldURI(UnindexedFieldURIType.MESSAGE_IS_READ);
		IS_READ_PATH = typesObjectFactory.createFieldURI(seenPath);
		
		PathToUnindexedFieldType importancePath = new PathToUnindexedFieldType();
		importancePath.setFieldURI(UnindexedFieldURIType.ITEM_IMPORTANCE);
		IS_IMPORTANT_PATH = typesObjectFactory.createFieldURI(importancePath);
		
		IMAP_UID_URI = new PathToExtendedFieldType();
		IMAP_UID_URI.setPropertyTag(IMAP_UID_TAG);
		IMAP_UID_URI.setPropertyType(MapiPropertyTypeType.INTEGER);
		IMAP_UID_PATH = typesObjectFactory.createExtendedFieldURI(IMAP_UID_URI);

	}

	static ThreadLocal<SimpleDateFormat> SMTP_DF = new ThreadLocal<SimpleDateFormat>(); 
	static final String SMTP_DATE_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";
	
	/**
	 * For com.microsoft.schemas.exchange.services._2006.types.MessageType
	 * @param messages
	 * @return
	 */
	public static List<String> getMessageTypeUIDs(List<MessageType> messages){
		List<String> ids = new ArrayList<String>();
		for(MessageType message : messages){
			String uid = getMessageTypeUID(message);
			// This ignores all messages w/o a ImapUID
			if(uid != null){
				ids.add(uid);
			} 
		}
		return ids;
	}
	
	/**
	 * For javax.mail.Message
	 * @param messages
	 * @return
	 * @throws MessagingException
	 */
	public static List<String> getMessageUIDs(List<Message> messages) throws MessagingException {
		List<String> ids = new ArrayList<String>();
		for(Message message : messages){
			ids.add(getMessageUID(message));
		}
		return ids;
	}

	public static String getFolderUid(Folder folder){
		return ExchangeConversion.getConv().getUser().getSourceImapPo()
		     + UID_SEPERATOR
		     + folder.getFullName() 
		     + UID_SEPERATOR;
	}
	
	/**
	 * Get the uid from a Message object
	 * @param message
	 * @return
	 * @throws MessagingException
	 */
	public static String getMessageUID(Message message) throws MessagingException{
		IMAPFolder folder = (IMAPFolder)message.getFolder();
		if(!folder.isOpen()) folder.open(Folder.READ_ONLY);
		return  getFolderUid(message.getFolder()) 
			  + String.valueOf(folder.getUID(message));
	}

	/**
	 * Gets the UID from a MessageType Object
	 * @param message
	 * @return
	 */
	public static String getMessageTypeUID(MessageType message){
		ExtendedPropertyType extProp = getExtProp(message, conversionUidUri);
		if(extProp == null) return null;
		return extProp.getValue();
	}
	
	/**
	 * Gets an extended property from a MessageType give a path
	 * @param message
	 * @param path
	 * @return
	 */
	public static ExtendedPropertyType getExtProp(MessageType message, PathToExtendedFieldType path){
		for(ExtendedPropertyType prop : message.getExtendedProperty()){
			// For Tagged Properties
			if(path.getPropertyTag() != null){
				if(prop.getExtendedFieldURI().getPropertyTag() != null
				&& prop.getExtendedFieldURI().getPropertyTag().equals(path.getPropertyTag())){
					return prop;
				}
			} 
			// For Namespace and Named Properties
			else {
				if(prop.getExtendedFieldURI().getPropertySetId() != null
				&& prop.getExtendedFieldURI().getPropertySetId().equals(path.getPropertySetId())
				&& prop.getExtendedFieldURI().getPropertyName().equals(path.getPropertyName())){
							return prop;
				}
			}
		}
		return null;
	}
	
	/**
	 * Removes MessagesTypes from a List of MessageTypes where the UID is the same 
	 * as the UID of the Messages in the Message List 
	 * @param messageTypes
	 * @param messages
	 * @return
	 * @throws MessagingException
	 */
	public static List<MessageType> removeMessagesFromMessageTypes(List<MessageType> messageTypes, List<Message> messages) throws MessagingException{
		return removeIdsFromMessageTypes(messageTypes, getMessageUIDs(messages));
	}
	
	/**
	 * Removes Messagesfrom a List of Messagewhere the UID is the same 
	 * as the UID of the MessageTypes in the MessageType List 
	 * @param messages
	 * @param messageTypes
	 * @return
	 * @throws MessagingException
	 */
	public static List<Message> removeMessageTypesFromMessages(List<Message> messages, List<MessageType> messageTypes) throws MessagingException{
		return removeIdsFromMessages(messages, getMessageTypeUIDs(messageTypes));
	}
	
	/**
	 * For com.microsoft.schemas.exchange.services._2006.types.MessageType
	 * @param messages
	 * @param ids
	 * @return
	 */
	public static List<MessageType> removeIdsFromMessageTypes(List<MessageType> messages, List<String> ids){
		List<MessageType> keptMessages = new ArrayList<MessageType>();
		for(MessageType message : messages){
			if(!ids.contains(getMessageTypeUID(message))){
				keptMessages.add(message);
			}
		}
		return keptMessages;
	}
	
	/**
	 * For com.microsoft.schemas.exchange.services._2006.types.MessageType
	 * @param messages
	 * @param ids
	 * @return
	 */
	public static List<MessageType> keepIdsFromMessageTypes(List<MessageType> messages, List<String> ids){
		List<MessageType> keptMessages = new ArrayList<MessageType>();
		for(MessageType message : messages){
			if(ids.contains(getMessageTypeUID(message))){
				keptMessages.add(message);
			}
		}
		return keptMessages;
	}

	/**
	 * For javax.mail.Message
	 * @param messages
	 * @param ids
	 * @return
	 * @throws MessagingException
	 */
	public static List<Message> removeIdsFromMessages(List<Message> messages, List<String> ids) throws MessagingException{
		List<Message> keptMessages = new ArrayList<Message>();
		for(Message message : messages){
			if(!ids.contains(getMessageUID(message))){
				keptMessages.add(message);
			}
		}
		return keptMessages;
	}

	/**
	 * For javax.mail.Message
	 * @param messages
	 * @param ids
	 * @return
	 * @throws MessagingException
	 */
	public static List<Message> keepIdsFromMessages(List<Message> messages, List<String> ids) throws MessagingException{
		List<Message> keptMessages = new ArrayList<Message>();
		for(Message message : messages){
			if(ids.contains(getMessageUID(message))){
				keptMessages.add(message);
			}
		}
		return keptMessages;
	}
	
	/**
	 * Creates messages In Exchange given a list of Messages.   This does not check if they already exists.
	 * @param user
	 * @param messages
	 * @param folderId
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<MessageType> createMessagesInExchange(List<Message> messages,  BaseFolderIdType folderId) throws MessagingException, IOException{
		if(messages == null || messages.isEmpty()) return new ArrayList<MessageType>();
		NonEmptyArrayOfAllItemsType convertedMessages = convertMessagesToMessageTypesSerially(messages);
		return createMessagesInExchange(convertedMessages, folderId, messages);
	}
	
	/**
	 * Creates messages In Exchange given a NonEmptyArrayOfAllItemsType.   This does not check if they already exists.
	 * @param user
	 * @param itemsArray
	 * @param folderId
	 * @return
	 */
	private static List<MessageType> createMessagesInExchange(NonEmptyArrayOfAllItemsType itemsArray
			                                                , BaseFolderIdType folderId
			                                                , List<Message> sourceMessages){

		List<MessageType> messages = new ArrayList<MessageType>();

		TargetFolderIdType targetFolder = new TargetFolderIdType();
		if(folderId instanceof DistinguishedFolderIdType){
			targetFolder.setDistinguishedFolderId((DistinguishedFolderIdType)folderId);
		} else {
			targetFolder.setFolderId((FolderIdType)folderId);
		}
		
		CreateItemType creator = new CreateItemType();
		creator.setItems(itemsArray);
		creator.setSavedItemFolderId(targetFolder);
		creator.setMessageDisposition(MessageDispositionType.SAVE_ONLY);
		
		// define response Objects and their holders
		CreateItemResponseType createItemResponse = new CreateItemResponseType();
		Holder<CreateItemResponseType> responseHolder = new Holder<CreateItemResponseType>(createItemResponse);

		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

		User user = ExchangeConversion.getConv().getUser();
		ExchangeServicePortType proxy = null;
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_MIME);
			proxy.createItem(creator, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
                       .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_MIME);
			int i = 0;
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					try{
						MessageType errorMessage = (MessageType)itemsArray.getItemOrMessageOrCalendarItem().get(i);
						ExtendedPropertyType uidProp = MessageUtil.getExtProp(errorMessage, conversionUidUri);
						logger.warn("Create Message In Exchange Response Error[" + response.getMessageText() + "]: convid: [" + uidProp.getValue() + "]: subject:[" + sourceMessages.get(i).getSubject() + "]");
						user.getConversion().warnings++;
					} catch (Exception e1){
						logger.warn("Create Message In Exchange Response Error - unable to determine source message.");
						user.getConversion().warnings++;
					}
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Create Message In Exchange Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					for(ItemType item : ((ItemInfoResponseMessageType)response).getItems().getItemOrMessageOrCalendarItem()){
						messages.add((MessageType)item);
					}
				}
				i++;
			}
		} catch (Exception e){
			throw new RuntimeException("Exception creating messages on Exchange Server", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_MIME))
				Report.getReport().stop(Report.EXCHANGE_MIME);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		} 
		
		return messages;
	}

	/**
	 * Gets the complete MessageType for a Message
	 * @param user
	 * @param message
	 * @return
	 */
	public static MessageType getCompleteMessage(MessageType message){
		List<MessageType> items = new ArrayList<MessageType>();
		items.add(message);
		return getCompleteMessages(items).get(0);		
	}
	
	/**
	 * Gets a list of Complete MessageTypes from a list of Messages 
	 * @param user
	 * @param messages
	 * @return
	 */
	public static List<MessageType> getCompleteMessages(List<MessageType> messages){
		List<BaseItemIdType> ids = new ArrayList<BaseItemIdType>();
		for(ItemType item : messages){
			ids.add(item.getItemId());
		}
		return getCompleteMessagesFromIds(ids);
	}
	
	/**
	 * Gets complete Messages
	 * @param user
	 * @param messageIds
	 * @return
	 */
	public static List<MessageType> getCompleteMessagesFromIds(List<BaseItemIdType> messageIds){
		if(messageIds == null || messageIds.isEmpty()) return new ArrayList<MessageType>();
		NonEmptyArrayOfBaseItemIdsType idsArray = new NonEmptyArrayOfBaseItemIdsType();
		List<BaseItemIdType> ids = idsArray.getItemIdOrOccurrenceItemIdOrRecurringMasterItemId();
		ids.addAll(messageIds);
		return getCompleteMessages(idsArray);		
	}

	/**
	 * 	 Gets complete Messages
	 * @param user
	 * @param ids
	 * @return
	 */
	private static List<MessageType> getCompleteMessages(NonEmptyArrayOfBaseItemIdsType ids){
		List<MessageType> messages = new ArrayList<MessageType>();
		
		ItemResponseShapeType itemShape = new ItemResponseShapeType();		
		itemShape.setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);

		GetItemType getter = new GetItemType();
		getter.setItemShape(itemShape);
		getter.setItemIds(ids);
		
		// define response Objects and their holders
		GetItemResponseType getItemResponse = new GetItemResponseType();
		Holder<GetItemResponseType> responseHolder = new Holder<GetItemResponseType>(getItemResponse);

		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

		User user = ExchangeConversion.getConv().getUser();
		ExchangeServicePortType proxy = null;
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_META);
			proxy.getItem(getter, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
			           .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);
			
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Get Complete Message Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Get Complete Message Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					for(ItemType item : ((ItemInfoResponseMessageType)response).getItems().getItemOrMessageOrCalendarItem()){
						messages.add((MessageType)item);
					}
				}
			}
		} catch (Exception e){
			throw new RuntimeException("Exception gettting the Exchange Message body", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		} 
		
		return messages;
	}
	
	/**
	 * Used to create a MessageType from a Message Object
	 * @param message
	 * @return
	 * @throws MessagingException
	 */
	protected static MessageType createMessageType(Message message) throws MessagingException{

		MessageType messageType = new MessageType();
		
		/*
		 *  Add this so that multipart/report messages get through...
		 */
		messageType.setItemClass(IPM_NOTE);
		
		/*
		 * Define Exchange Extened Prop UID for the new Message 
		 */
		ExtendedPropertyType conversionUid = new ExtendedPropertyType();
		conversionUid.setExtendedFieldURI(conversionUidUri);
		conversionUid.setValue(MessageUtil.getMessageUID(message));
		
		/*
		 * Define Exchange Extened Prop UID for the new Message
		 * NOTE: this prop is not settable.. Exchange overwrites my values... 
		 */
//		ExtendedPropertyType uid = new ExtendedPropertyType();
//		uid.setExtendedFieldURI(IMAP_UID_URI);
//		uid.setValue(String.valueOf(((UIDFolder)message.getFolder()).getUID(message)));

		/*
		 * Flags to replicate: \Answered \Flagged \Deleted \Draft \Seen
		 */
		PrMessageFlags prMessageFlags = new PrMessageFlags();
		if(message.getFlags().contains(Flags.Flag.SEEN))
			prMessageFlags.add(PrMessageFlags.MSGFLAG_READ);
		if(message.getFlags().contains(Flags.Flag.DRAFT))
			prMessageFlags.add(PrMessageFlags.MSGFLAG_UNSENT);
		ExtendedPropertyType prMessageFlagsProp = new ExtendedPropertyType();
		prMessageFlagsProp.setExtendedFieldURI(PrMessageFlags.PR_MESSAGE_FLAGS_URI);
		prMessageFlagsProp.setValue(prMessageFlags.toIntString());

//		if(message.getFlags().contains(Flags.Flag.FLAGGED))
//			messageType.setImportance(ImportanceChoicesType.HIGH);
		
//		PrMsgStatus prMsgStatusFlags = new PrMsgStatus();
//		if(message.getFlags().contains(Flags.Flag.DELETED))
//			prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_DELMARKED);
//		if(message.getFlags().contains(Flags.Flag.ANSWERED))
//			prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_ANSWERED);
//		if(message.getFlags().contains(Flags.Flag.FLAGGED))
//			prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_TAGGED);
//		if(message.getFlags().contains(Flags.Flag.DRAFT))
//			prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_DRAFT);

//		logger.debug(getMessageUID(message) + ": " + prMsgStatusFlags.toString());
		
//		ExtendedPropertyType prMsgStatusFlagsProp = new ExtendedPropertyType();
//		prMsgStatusFlagsProp.setExtendedFieldURI(PrMsgStatus.PR_MSG_STATUS_URI);
//		prMsgStatusFlagsProp.setValue(prMsgStatusFlags.toLongString());
		
		// Set Properties that I can't modify after creation...
		List<ExtendedPropertyType> extProps = messageType.getExtendedProperty();
		extProps.add(conversionUid);
		extProps.add(prMessageFlagsProp);
//		extProps.add(uid);
//		extProps.add(prMsgStatusFlagsProp);
		
		return messageType;
	}
	
	/**
	 * Converts Messages to MessageTypes
	 * @param sourceMessages
	 * @return
 * @throws MessagingException	 * @throws IOException
	 */
	public static NonEmptyArrayOfAllItemsType convertMessagesToMessageTypesSerially(List<Message> sourceMessages) throws MessagingException, IOException{
		NonEmptyArrayOfAllItemsType returnList = new NonEmptyArrayOfAllItemsType();
		if(sourceMessages == null || sourceMessages.isEmpty()) throw new IllegalArgumentException("sourceMessages can not be null nor empty.");
		List<ItemType> destMessages = returnList.getItemOrMessageOrCalendarItem();
		ByteArrayOutputStream baos = null;
		BASE64EncoderStream b64es = null;
		ExchangeConversion conv = ExchangeConversion.getConv();
		for(Message sourceMessage : sourceMessages){
			try{
				
				// Create the Destination Message and set Props before I set the mime-content...
				MessageType destMessage = createMessageType(sourceMessage);

				// Convert MimeContent to Base64 and add it to the message.
				baos = new ByteArrayOutputStream();
				b64es = new  BASE64EncoderStream(baos);
				Report.getReport().start(Report.IMAP_MIME);
				
				// Check to see if the message has a received header, add one if missing
				// This makes Exchange set the received date to the correct date and not the
				// date of the conversion.  Sloppy, but they tie my hands.
				int size = sourceMessage.getSize();
				String[] receivedHeaders = sourceMessage.getHeader("Received");
				if(receivedHeaders == null || receivedHeaders.length == 0){
					StringBuilder sb = new StringBuilder();
					sb.append("Received: from imap.client.app.edu ([0.0.0.0]) \n\t");
					sb.append("by imap.to.exchange.edu with imap2exchange id 1234; \n\t");
					Date date = sourceMessage.getSentDate();
					if(date == null) date = sourceMessage.getReceivedDate();
					if(date == null){
						logger.debug("No send date, or recieved date found for message missing recieved header, using now()");
						date = new Date(System.currentTimeMillis());
					}
					sb.append(getSMTPDateFormat().format(date)).append("\n");	
					String rh = sb.toString();
					logger.debug("Adding imap2Exchange ReceivedHeader: " + rh);
					b64es.write(rh.getBytes("ASCII"));
					size += rh.length();
				}
				sourceMessage.writeTo(b64es);
				b64es.flush();
				
				Report.getReport().stop(Report.IMAP_MIME);
				
				MimeContentType mime = new MimeContentType();
				mime.setValue(baos.toString());

				destMessage.setMimeContent(mime);
				destMessages.add(destMessage);

				conv.addMessagesSize(size);
				conv.addMessagesCnt(1);
				
			} catch (Exception e){
				/* I only warn here because I might hit a race condition where
				 * I have indexed a folder, the user purges flagged deleted times
				 * and then I try to get the MimeContent...  Just needs to be 
				 * looked at post conversion.
				 */ 
				logger.warn("Error getting message source MIME content.", e);
				conv.warnings++;
			}

		}
		return returnList;
	}

	/**
	 * Deletes Messages in Exchange
	 * @param user
	 * @param messages
	 * @param disposalType
	 * @return
	 */
	public static List<MessageType> deleteMessages(List<MessageType> messages, DisposalType disposalType){

		if(messages == null || messages.isEmpty()) return null;
		
		DeleteItemType deleter = new DeleteItemType();
		
		NonEmptyArrayOfBaseItemIdsType idsArray = new NonEmptyArrayOfBaseItemIdsType();
		List<BaseItemIdType> ids = idsArray.getItemIdOrOccurrenceItemIdOrRecurringMasterItemId();
		for(MessageType message : messages){
			ids.add(message.getItemId());
		}
		
		deleter.setDeleteType(disposalType);
		deleter.setItemIds(idsArray);

		// define response Objects and their holders
		DeleteItemResponseType deleteItemResponse = new DeleteItemResponseType();
		Holder<DeleteItemResponseType> responseHolder = new Holder<DeleteItemResponseType>(deleteItemResponse);

		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

		User user = ExchangeConversion.getConv().getUser();
		ExchangeServicePortType proxy = null;
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_META);
			proxy.deleteItem(deleter, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
			           .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);
			                 
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Delete Messages Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.debug(response.getClass().getName());
					logger.warn("Delete Messages Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					//no messages to send back... 
				}
			}

		} catch (Exception e){
			throw new RuntimeException("Exception deleting messages", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		} 
		
		return messages;
	}
	
	/**
	 * Gets a message with minimal info for comparision with IMAP Messages
	 * @param user
	 * @param folderId
	 * @return
	 */
	public static List<MessageType> getMessages(BaseFolderIdType folderId){
		
		FindItemType finder = new FindItemType();
	
		NonEmptyArrayOfPathsToElementType paths = new NonEmptyArrayOfPathsToElementType();
		paths.getPath().add(CONVERSION_UID_PATH);
		paths.getPath().add(IS_READ_PATH);
//		paths.getPath().add(IS_IMPORTANT_PATH);
		paths.getPath().add(PrMessageFlags.PR_MESSAGE_FLAGS_PATH);
//		paths.getPath().add(PrMsgStatus.PR_MSG_STATUS_PATH);
//		paths.getPath().add(IMAP_UID_PATH);

	    ItemResponseShapeType itemShape = new ItemResponseShapeType();		
		itemShape.setBaseShape(DefaultShapeNamesType.ID_ONLY);
		itemShape.setAdditionalProperties(paths);
		finder.setItemShape(itemShape);
	
		finder.setTraversal(ItemQueryTraversalType.SHALLOW);
		
		IndexedPageViewType index = new IndexedPageViewType();
		index.setBasePoint(IndexBasePointType.BEGINNING);
		index.setOffset(0);
		
		finder.setIndexedPageItemView(index);
		
		NonEmptyArrayOfBaseFolderIdsType folderIds = new NonEmptyArrayOfBaseFolderIdsType();
		List<BaseFolderIdType> ids = folderIds.getFolderIdOrDistinguishedFolderId();
		ids.add(folderId);
		finder.setParentFolderIds(folderIds);
		
		// define response Objects and their holders
		FindItemResponseType findItemResponse = new FindItemResponseType();
		Holder<FindItemResponseType> responseHolder = new Holder<FindItemResponseType>(findItemResponse);
	
		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);
	
		User user = ExchangeConversion.getConv().getUser();
		ExchangeServicePortType proxy = null;
		List<MessageType> messages = new ArrayList<MessageType>();
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_META);
			proxy.findItem(finder, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
	                   .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);
			
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Get Messages Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Get Messages Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					FindItemResponseMessageType findResponse = (FindItemResponseMessageType)response;
					for(ItemType item : findResponse.getRootFolder().getItems().getItemOrMessageOrCalendarItem()){
						// This filters out all the messages not moved by an earlier conversion...
						if((item.getExtendedProperty() != null) 
						&& !(item.getExtendedProperty().isEmpty())
						&& item instanceof MessageType){
							messages.add((MessageType)item);
						}
					}
				}
			}
	
		} catch (Exception e){
			throw new RuntimeException("Exception performing getMessages", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		} 
		return messages;
	}
	

	/**
	 * Doesn't work!
	 * @param user
	 * @param destMessages
	 * @param sourceMessages
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<MessageType> updateMessagesImapUid(List<MessageType> destMessages, List<Message> sourceMessages) throws MessagingException, IOException{

		if(destMessages == null || sourceMessages == null
		|| destMessages.isEmpty() || sourceMessages.isEmpty()){
			throw new IllegalArgumentException("Both messages parameters can not be null nor empty");
		}
		
		UpdateItemType updater = new UpdateItemType();
				
		NonEmptyArrayOfItemChangesType allChangesNEA = new NonEmptyArrayOfItemChangesType();
		List<ItemChangeType> allChanges = allChangesNEA.getItemChange();
				
		int i = 0;
		for(MessageType destMessage : destMessages){
			
			ItemChangeType setter = new ItemChangeType();

			NonEmptyArrayOfItemChangeDescriptionsType changesNEA = new NonEmptyArrayOfItemChangeDescriptionsType();
			List<ItemChangeDescriptionType> changes = changesNEA.getAppendToItemFieldOrSetItemFieldOrDeleteItemField();

			MessageType messageWithChange = new MessageType();
			
			// Set the UID Extended Property
			SetItemFieldType uidSetter = new SetItemFieldType();
			uidSetter.setPath(IMAP_UID_PATH);

			ExtendedPropertyType uid = new ExtendedPropertyType();
			uid.setExtendedFieldURI(IMAP_UID_URI);
			uid.setValue(String.valueOf(((UIDFolder)sourceMessages.get(i).getFolder()).getUID(sourceMessages.get(i))));
			List<ExtendedPropertyType> extProps = messageWithChange.getExtendedProperty();
			extProps.add(uid);
			
			uidSetter.setMessage(messageWithChange);
			changes.add(uidSetter);

			// Add the Change for this message
			setter.setUpdates(changesNEA);
			setter.setItemId(destMessage.getItemId());
			allChanges.add(setter);
			i++;
		}
		
		updater.setItemChanges(allChangesNEA);
		updater.setConflictResolution(ConflictResolutionType.ALWAYS_OVERWRITE);
		updater.setMessageDisposition(MessageDispositionType.SAVE_ONLY);

		return updateMessagesMetadata(updater);
	}

	/**
	 * Be sure you did not getContent on Message Objects or javamail will set the
	 * seen flag to true in the object, even though you are in read_only state for the server. 
	 * @param user
	 * @param destMessages
	 * @param sourceMessages
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<MessageType> updateMessagesIsRead(List<MessageType> destMessages, List<Message> sourceMessages) throws MessagingException, IOException{
		if(destMessages == null || sourceMessages == null
		|| destMessages.isEmpty() || sourceMessages.isEmpty()){
			throw new IllegalArgumentException("Both messages parameters can not be null nor empty");
		}
		UpdateItemType updater = new UpdateItemType();
				
		NonEmptyArrayOfItemChangesType allChangesNEA = new NonEmptyArrayOfItemChangesType();
		List<ItemChangeType> allChanges = allChangesNEA.getItemChange();
				
		int i = 0;
		for(MessageType destMessage : destMessages){
			
			ItemChangeType setter = new ItemChangeType();

			NonEmptyArrayOfItemChangeDescriptionsType changesNEA = new NonEmptyArrayOfItemChangeDescriptionsType();
			List<ItemChangeDescriptionType> changes = changesNEA.getAppendToItemFieldOrSetItemFieldOrDeleteItemField();

			MessageType messageWithChange = new MessageType();
			
			/* 
			 * \Seen
			 */
			SetItemFieldType seenSetter = new SetItemFieldType();
			messageWithChange.setIsRead(sourceMessages.get(i).getFlags().contains(Flag.SEEN));
			destMessage.setIsRead(sourceMessages.get(i).getFlags().contains(Flag.SEEN));
			seenSetter.setMessage(messageWithChange);
			seenSetter.setPath(IS_READ_PATH);
			changes.add(seenSetter);

			// Add the Change for this message
			setter.setUpdates(changesNEA);
			setter.setItemId(destMessage.getItemId());
			allChanges.add(setter);
			i++;
		}
		
		updater.setItemChanges(allChangesNEA);
		updater.setConflictResolution(ConflictResolutionType.ALWAYS_OVERWRITE);
		updater.setMessageDisposition(MessageDispositionType.SAVE_ONLY);

		// This adds the new Id to the old updated messageType
		List<MessageType> messagesWithNewIds = updateMessagesMetadata(updater);
		for(i=0; i<messagesWithNewIds.size(); i++){
			destMessages.get(i).setItemId(messagesWithNewIds.get(i).getItemId());			
		}
		return destMessages;
	}
	
	/**
	 * Doesn't work property is read only...
	 * @param user
	 * @param destMessages
	 * @param sourceMessages
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<MessageType> updatePrMsgStatus(List<MessageType> destMessages, List<Message> sourceMessages) throws MessagingException, IOException{
		if(destMessages == null || sourceMessages == null
		|| destMessages.isEmpty() || sourceMessages.isEmpty()){
			throw new IllegalArgumentException("Both messages parameters can not be null nor empty");
		}
		UpdateItemType updater = new UpdateItemType();
				
		NonEmptyArrayOfItemChangesType allChangesNEA = new NonEmptyArrayOfItemChangesType();
		List<ItemChangeType> allChanges = allChangesNEA.getItemChange();
				
		int i = 0;
		for(MessageType destMessage : destMessages){
			
			ItemChangeType setter = new ItemChangeType();

			NonEmptyArrayOfItemChangeDescriptionsType changesNEA = new NonEmptyArrayOfItemChangeDescriptionsType();
			List<ItemChangeDescriptionType> changes = changesNEA.getAppendToItemFieldOrSetItemFieldOrDeleteItemField();

			MessageType messageWithChange = new MessageType();
			Message message = sourceMessages.get(i);

			/* 
			 * PR_MSG_STATUS 
			 */
			
			// Get the current value for flags
			ExtendedPropertyType prMsgStatusFlagsProp = MessageUtil.getExtProp(destMessage, PrMsgStatus.PR_MSG_STATUS_URI);
			long flagValue = 0;
			if(prMsgStatusFlagsProp != null){
				flagValue = Long.parseLong(prMsgStatusFlagsProp.getValue());
			} else {
				prMsgStatusFlagsProp = new ExtendedPropertyType();
				logger.debug("PR_MSG_STATUS was null when updating.");
			}
			PrMsgStatus prMsgStatusFlags = new PrMsgStatus();
			prMsgStatusFlags.setFlags(flagValue);
			logger.debug("OLD: " + getMessageUID(message) + ": " + prMsgStatusFlags.toString());
			
			if(message.getFlags().contains(Flags.Flag.DELETED))
				prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_DELMARKED);
			else 
				prMsgStatusFlags.remove(PrMsgStatus.MSGSTATUS_DELMARKED);
			
			if(message.getFlags().contains(Flags.Flag.ANSWERED))
				prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_ANSWERED);
			else
				prMsgStatusFlags.remove(PrMsgStatus.MSGSTATUS_ANSWERED);
			
			if(message.getFlags().contains(Flags.Flag.FLAGGED))
				prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_TAGGED);
			else 
				prMsgStatusFlags.remove(PrMsgStatus.MSGSTATUS_TAGGED);
				
			if(message.getFlags().contains(Flags.Flag.DRAFT))
				prMsgStatusFlags.add(PrMsgStatus.MSGSTATUS_DRAFT);
			else 
				prMsgStatusFlags.remove(PrMsgStatus.MSGSTATUS_DRAFT);

			logger.debug("NEW: " + getMessageUID(message) + ": " + prMsgStatusFlags.toString());
				

			prMsgStatusFlagsProp.setExtendedFieldURI(PrMsgStatus.PR_MSG_STATUS_URI);
			prMsgStatusFlagsProp.setValue(prMsgStatusFlags.toLongString());

			List<ExtendedPropertyType> extProps = messageWithChange.getExtendedProperty();
			extProps.add(prMsgStatusFlagsProp);
			
			SetItemFieldType prMsgStatusSetter = new SetItemFieldType();
			prMsgStatusSetter.setMessage(messageWithChange);
			prMsgStatusSetter.setPath(PrMsgStatus.PR_MSG_STATUS_PATH);
			changes.add(prMsgStatusSetter);

			// Add the Change for this message
			setter.setUpdates(changesNEA);
			setter.setItemId(destMessage.getItemId());
			allChanges.add(setter);
			i++;
		}
		
		updater.setItemChanges(allChangesNEA);
		updater.setConflictResolution(ConflictResolutionType.ALWAYS_OVERWRITE);
		updater.setMessageDisposition(MessageDispositionType.SAVE_ONLY);

		// This adds the new Id with new ChangeId to the old updated messageType
		List<MessageType> messagesWithNewIds = updateMessagesMetadata(updater);
		for(i=0; i<messagesWithNewIds.size(); i++){
			destMessages.get(i).setItemId(messagesWithNewIds.get(i).getItemId());			
		}
		return destMessages;
	}

	/**
	 * updates the IsImportant, Exchange will do this for you based on the Message Headers...
	 * @param user
	 * @param destMessages
	 * @param sourceMessages
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<MessageType> updateMessagesIsImportant(List<MessageType> destMessages, List<Message> sourceMessages) throws MessagingException, IOException{
		if(destMessages == null || sourceMessages == null
		|| destMessages.isEmpty() || sourceMessages.isEmpty()){
			throw new IllegalArgumentException("Both messages parameters can not be null nor empty");
		}
		
		UpdateItemType updater = new UpdateItemType();
		
		NonEmptyArrayOfItemChangesType allChangesNEA = new NonEmptyArrayOfItemChangesType();
		List<ItemChangeType> allChanges = allChangesNEA.getItemChange();
		
		int i = 0;
		for(MessageType destMessage : destMessages){
			
			ItemChangeType setter = new ItemChangeType();

			NonEmptyArrayOfItemChangeDescriptionsType changesNEA = new NonEmptyArrayOfItemChangeDescriptionsType();
			List<ItemChangeDescriptionType> changes = changesNEA.getAppendToItemFieldOrSetItemFieldOrDeleteItemField();

			MessageType messageWithChange = new MessageType();
			
			/* 
			 * \Flagged
			 */
			SetItemFieldType flaggedSetter = new SetItemFieldType();
			if(sourceMessages.get(i).getFlags().contains(Flag.FLAGGED)){
				messageWithChange.setImportance(ImportanceChoicesType.HIGH);
				destMessage.setImportance(ImportanceChoicesType.HIGH);
			} else { 
				messageWithChange.setImportance(ImportanceChoicesType.NORMAL);
				destMessage.setImportance(ImportanceChoicesType.NORMAL);
			}
			flaggedSetter.setMessage(messageWithChange);
			flaggedSetter.setPath(IS_IMPORTANT_PATH);
			changes.add(flaggedSetter);

			// Add the Change for this message
			setter.setUpdates(changesNEA);
			setter.setItemId(destMessage.getItemId());
			allChanges.add(setter);
			i++;
		}
		
		updater.setItemChanges(allChangesNEA);
		updater.setConflictResolution(ConflictResolutionType.ALWAYS_OVERWRITE);
		updater.setMessageDisposition(MessageDispositionType.SAVE_ONLY);

		// This adds the new Id to the old updated messageType
		List<MessageType> messagesWithNewIds = updateMessagesMetadata(updater);
		for(i=0; i<messagesWithNewIds.size(); i++){
			destMessages.get(i).setItemId(messagesWithNewIds.get(i).getItemId());			
		}
		return destMessages;
	}


	/** 
	 * Set System Flags
	 * From RFC 3501
	 *         \Seen
	 *            Message has been read
	 * 
	 *         \Answered
	 *            Message has been answered
	 * 
	 *         \Flagged
	 *            Message is "flagged" for urgent/special attention
	 * 
	 *         \Deleted
	 *            Message is "deleted" for removal by later EXPUNGE
	 * 
	 *         \Draft
	 *            Message has not completed composition (marked as a draft).
	 * 
	 *         \Recent
	 *            Message is "recently" arrived in this mailbox.  This session
	 *            is the first session to have been notified about this
	 *            message; if the session is read-write, subsequent sessions
	 *            will not see \Recent set for this message.  This flag can not
	 *            be altered by the client.
	 * 
	 *            If it is not possible to determine whether or not this
	 *            session is the first session to be notified about a message,
	 *            then that message SHOULD be considered recent.
	 * 
	 *            If multiple connections have the same mailbox selected
	 *            simultaneously, it is undefined which of these connections
	 *            will see newly-arrived messages with \Recent set and which
	 *            will see it without \Recent set.
	 * 			 
	 */
	protected static List<MessageType> updateMessagesMetadata(UpdateItemType updater) throws MessagingException, IOException{

		// define response Objects and their holders
		UpdateItemResponseType updateItemResponse = new UpdateItemResponseType();
		Holder<UpdateItemResponseType> responseHolder = new Holder<UpdateItemResponseType>(updateItemResponse);
	
		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);
	
		User user = ExchangeConversion.getConv().getUser();
		ExchangeServicePortType proxy = null;
		List<MessageType> messages = new ArrayList<MessageType>();
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_META);
			proxy.updateItem(updater, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
	                   .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);

			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Update Messages Metadata Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Update Messages Metadata Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					ItemInfoResponseMessageType infoResponse = (ItemInfoResponseMessageType)response;
					for(ItemType item : infoResponse.getItems().getItemOrMessageOrCalendarItem()){
						messages.add((MessageType)item);
					}
				}
			}
	
		} catch (Exception e){
			throw new RuntimeException("Exception performing UpdateMessageTypesMetadata", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		} 
		return messages;
		
	}

	/**
	 * Moves a message
	 * @param user
	 * @param messages
	 * @param folder
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<MessageType> moveMessages(User user, List<MessageType> messages, BaseFolderType folder) throws MessagingException, IOException{
		return moveMessages(messages, folder.getFolderId());
	}
	
	/**
	 * Moves a list of messages
	 * @param user
	 * @param messages
	 * @param folderId
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static List<MessageType> moveMessages(List<MessageType> messages, BaseFolderIdType folderId) throws MessagingException, IOException{

		MoveItemType mover = new MoveItemType();
		
		NonEmptyArrayOfBaseItemIdsType idsNEA = new NonEmptyArrayOfBaseItemIdsType(); 
		List<BaseItemIdType> itemIds = idsNEA.getItemIdOrOccurrenceItemIdOrRecurringMasterItemId();
		for(MessageType message : messages){
			itemIds.add(message.getItemId());
		}
		mover.setItemIds(idsNEA);
		
		TargetFolderIdType targetFolderId = new TargetFolderIdType();
		if(folderId instanceof DistinguishedFolderIdType){
			targetFolderId.setDistinguishedFolderId((DistinguishedFolderIdType)folderId);
		} else {
			targetFolderId.setFolderId((FolderIdType)folderId);
		}
		mover.setToFolderId(targetFolderId);
		
		// define response Objects and their holders
		MoveItemResponseType moveItemResponse = new MoveItemResponseType();
		Holder<MoveItemResponseType> responseHolder = new Holder<MoveItemResponseType>(moveItemResponse);
	
		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);
	
		User user = ExchangeConversion.getConv().getUser();
		ExchangeServicePortType proxy = null;
		List<MessageType> retMessages = new ArrayList<MessageType>();
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_META);
			proxy.moveItem(mover, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
	                   .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);

			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Update Messages Metadata Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Update Messages Metadata Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					ItemInfoResponseMessageType infoResponse = (ItemInfoResponseMessageType)response;
					for(ItemType item : infoResponse.getItems().getItemOrMessageOrCalendarItem()){
						retMessages.add((MessageType)item);
					}
				}
			}
	
		} catch (Exception e){
			throw new RuntimeException("Exception performing UpdateMessageTypesMetadata", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		} 
		
		return retMessages;
	}
	
	// Thread local instantiation of SimpleDateFormatter...
	protected static SimpleDateFormat getSMTPDateFormat(){
		SimpleDateFormat sdf = SMTP_DF.get();
		if(sdf == null){
			sdf = new SimpleDateFormat(SMTP_DATE_PATTERN);
			SMTP_DF.set(sdf);
		}
		return sdf;
	}
	
}