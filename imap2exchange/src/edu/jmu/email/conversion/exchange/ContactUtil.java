package edu.jmu.email.conversion.exchange;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.schemas.exchange.services._2006.messages.*;
import com.microsoft.schemas.exchange.services._2006.types.*;

import edu.yale.its.tp.email.conversion.Report;
import edu.yale.its.tp.email.conversion.User;
import edu.yale.its.tp.email.conversion.exchange.ExchangeServerPortFactory;

/**
 * <pre>
 * $URL$
 * $Author$
 * $Date$
 * $Rev$
 * 
 * Copyright (c) 2009 Seth Wright (wrightst@jmu.edu)
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * </pre>
 *
 */
public class ContactUtil {
	private static final Log logger = LogFactory.getLog(ContactUtil.class);
	public static final int PID_LID_FILE_UNDER = 0x8005;
	public static final int PID_LID_DISTRIBUTION_LIST_NAME = 0x8053;
	public static final int PID_LID_DISTRIBUTION_LIST_ONE_OFF_MEMBERS = 0X8054;
	public static final int PID_LID_DISTRIBUTION_LIST_MEMBERS = 0x8055;
	public static final String WRAPPED_ENTRYID_FLAGS = "00000000";
	public static final String WRAPPED_ENTRYID_PROVIDER_UID = "C091ADD3519DCF11A4A900AA0047FAA4";
	public static final String WRAPPED_ENTRYID_TYPE_CONTACT_ENTRYID = "C3";

	public static List<ItemType> createContact(User user, ContactItemType contact, ContactsFolderType contactsFolder) {
		CreateItemType creator = new CreateItemType();
		creator.setSavedItemFolderId(new TargetFolderIdType());
		creator.getSavedItemFolderId().setFolderId(contactsFolder.getFolderId());

		creator.setItems(new NonEmptyArrayOfAllItemsType());

		creator.getItems().getItemOrMessageOrCalendarItem().add(contact);

		return getResponse(user, creator);
	}

	public static ContactItemType getContact(User user, String fileAs, ContactsFolderType contactsFolder) {
		ContactItemType contact = null;

		// Form the FindItem request.
		FindItemType finder = new FindItemType();

		// Define which item properties are returned in the response.
		ItemResponseShapeType itemShape = new ItemResponseShapeType();		
		itemShape.setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);
		finder.setItemShape(itemShape);

		// Choose the traversal mode.
		finder.setTraversal(ItemQueryTraversalType.SHALLOW);

		// Define the folders to search.
		NonEmptyArrayOfBaseFolderIdsType folderIds = new NonEmptyArrayOfBaseFolderIdsType();
		List<BaseFolderIdType> ids = folderIds.getFolderIdOrDistinguishedFolderId();
		ids.add(contactsFolder.getFolderId());
		finder.setParentFolderIds(folderIds);

		/*
		// Identify the field to examine.
		PathToUnindexedFieldType pathToUnindexedField = new PathToUnindexedFieldType();
		pathToUnindexedField.setFieldURI(UnindexedFieldURIType.CONTACTS_FILE_AS);

		// Define the type of search filter to apply.
		ContainsExpressionType containsExpression = new ContainsExpressionType();
		containsExpression.setPath(typesObjectFactory.createPath(pathToUnindexedField));

		// Specify how the search expression is evaluated.
		containsExpression.setContainmentComparison(ContainmentComparisonType.IGNORE_CASE);
		containsExpression.setContainmentMode(ContainmentModeType.SUBSTRING);

		// Identify the value to compare to the examined field.
		ConstantValueType constantValue = new ConstantValueType();
		constantValue.setValue(fileAs);
		logger.debug(String.format("Searching for fileAs = %s", fileAs));

		// Add the value to the search expression.
		containsExpression.setConstant(constantValue);

		// Create a restriction.
		RestrictionType restriction = new RestrictionType();

		// Add the search expression to the restriction.
		restriction.setSearchExpression(typesObjectFactory.createContains(containsExpression));

		// Add the restriction to the request.
		finder.setRestriction(restriction);
		 */
		// define response Objects and their holders
		FindItemResponseType findItemResponse = new FindItemResponseType();
		Holder<FindItemResponseType> responseHolder = new Holder<FindItemResponseType>(findItemResponse);

		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

		ExchangeServicePortType proxy = null;
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			user.getConversion().getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
			user.getConversion().getReport().start(Report.EXCHANGE_META);
			proxy.findItem(finder, user.getImpersonation(), responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
			.getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			user.getConversion().getReport().stop(Report.EXCHANGE_META);

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
						if (((ContactItemType)item).getFileAs().equalsIgnoreCase(fileAs)) {
							contact = (ContactItemType)item;
						}
					}
				}
			}
		} catch (Exception e){
			throw new RuntimeException("Exception performing getContacts", e);
		} finally {
			if(user.getConversion().getReport().isStarted(Report.EXCHANGE_META))
				user.getConversion().getReport().stop(Report.EXCHANGE_META);
			if(user.getConversion().getReport().isStarted(Report.EXCHANGE_CONNECT))
				user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
		} 

		return (ContactItemType)contact;
	}

	public static List<ItemType> createDistributionList(User user, ItemType distributionList, ContactsFolderType contactsFolder) {
		CreateItemType creator = new CreateItemType();
		creator.setSavedItemFolderId(new TargetFolderIdType());
		creator.getSavedItemFolderId().setFolderId(contactsFolder.getFolderId());

		creator.setItems(new NonEmptyArrayOfAllItemsType());

		creator.getItems().getItemOrMessageOrCalendarItem().add(distributionList);
		
		return getResponse(user, creator);
	}

	public static List<ItemType> getResponse(User user, CreateItemType creator) {
		List<ItemType> items = new ArrayList<ItemType>();
		// define response Objects and their holders
		CreateItemResponseType createItemResponse = new CreateItemResponseType();
		Holder<CreateItemResponseType> responseHolder = new Holder<CreateItemResponseType>(createItemResponse);

		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

		ExchangeServicePortType proxy = null;
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			user.getConversion().getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
			user.getConversion().getReport().start(Report.EXCHANGE_MIME);
			proxy.createItem(creator, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
			.getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			user.getConversion().getReport().stop(Report.EXCHANGE_MIME);
			int i = 0;
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					try{
						logger.warn("Create Item in Exchange Response Error [" + response.getMessageText() + "]");
						user.getConversion().warnings++;
					} catch (Exception e1){
						e1.printStackTrace();
						logger.warn("Create Distribution List In Exchange Response Error - unable to determine source item.");
						user.getConversion().warnings++;
					}
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Create Distribution List In Exchange Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					for(ItemType item : ((ItemInfoResponseMessageType)response).getItems().getItemOrMessageOrCalendarItem()){
						items.add(item);
					}
				}
				i++;
			}
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException("Exception creating contact on Exchange Server: " + e.getMessage(), e);
		} finally {
			if(user.getConversion().getReport().isStarted(Report.EXCHANGE_MIME))
				user.getConversion().getReport().stop(Report.EXCHANGE_MIME);
			if(user.getConversion().getReport().isStarted(Report.EXCHANGE_CONNECT))
				user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
		} 

		return items;
	}
}
