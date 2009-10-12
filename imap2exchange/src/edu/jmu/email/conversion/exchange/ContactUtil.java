package edu.jmu.email.conversion.exchange;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.schemas.exchange.services._2006.messages.CreateItemResponseType;
import com.microsoft.schemas.exchange.services._2006.messages.CreateItemType;
import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServicePortType;
import com.microsoft.schemas.exchange.services._2006.messages.ItemInfoResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.messages.ResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.types.ContactItemType;
import com.microsoft.schemas.exchange.services._2006.types.ContactsFolderType;
import com.microsoft.schemas.exchange.services._2006.types.ItemType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfAllItemsType;
import com.microsoft.schemas.exchange.services._2006.types.PathToExtendedFieldType;
import com.microsoft.schemas.exchange.services._2006.types.ResponseClassType;
import com.microsoft.schemas.exchange.services._2006.types.ServerVersionInfo;
import com.microsoft.schemas.exchange.services._2006.types.TargetFolderIdType;

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
	public static PathToExtendedFieldType conversionUidUri = new PathToExtendedFieldType();
	
	public static List<ContactItemType> createContact(User user, ContactItemType contact, ContactsFolderType contactsFolder) {
		ArrayList<ContactItemType> contacts = new ArrayList<ContactItemType>();
		
		CreateItemType creator = new CreateItemType();
		creator.setSavedItemFolderId(new TargetFolderIdType());
		creator.getSavedItemFolderId().setFolderId(contactsFolder.getFolderId());

		creator.setItems(new NonEmptyArrayOfAllItemsType());

		creator.getItems().getItemOrMessageOrCalendarItem().add(contact);

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
//						ContactItemType errorMessage = (ContactItemType)creator.getItems().getItemOrMessageOrCalendarItem().get(i);
//						ExtendedPropertyType uidProp = MessageUtil.getExtProp(errorMessage, conversionUidUri);
						logger.warn("Create Contact In Exchange Response Error [" + response.getMessageText() + "]: contact:[" + contact.getDisplayName() + "]");
						user.getConversion().warnings++;
					} catch (Exception e1){
						e1.printStackTrace();
						logger.warn("Create Message In Exchange Response Error - unable to determine source message.");
						user.getConversion().warnings++;
					}
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Create Message In Exchange Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					for(ItemType item : ((ItemInfoResponseMessageType)response).getItems().getItemOrMessageOrCalendarItem()){
						contacts.add((ContactItemType)item);
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

		return contacts;
	}
}
