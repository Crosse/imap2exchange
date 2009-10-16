package edu.jmu.email.conversion.exchange;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.schemas.exchange.services._2006.messages.ConvertIdResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.messages.ConvertIdResponseType;
import com.microsoft.schemas.exchange.services._2006.messages.ConvertIdType;
import com.microsoft.schemas.exchange.services._2006.messages.CreateItemResponseType;
import com.microsoft.schemas.exchange.services._2006.messages.CreateItemType;
import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServicePortType;
import com.microsoft.schemas.exchange.services._2006.messages.FindItemResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.messages.FindItemResponseType;
import com.microsoft.schemas.exchange.services._2006.messages.FindItemType;
import com.microsoft.schemas.exchange.services._2006.messages.ItemInfoResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.messages.ResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.types.AlternateIdType;
import com.microsoft.schemas.exchange.services._2006.types.BaseFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.BodyType;
import com.microsoft.schemas.exchange.services._2006.types.BodyTypeType;
import com.microsoft.schemas.exchange.services._2006.types.ConstantValueType;
import com.microsoft.schemas.exchange.services._2006.types.ContactItemType;
import com.microsoft.schemas.exchange.services._2006.types.DefaultShapeNamesType;
import com.microsoft.schemas.exchange.services._2006.types.DictionaryURIType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedPropertySetType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressDictionaryEntryType;
import com.microsoft.schemas.exchange.services._2006.types.ExchangeVersionType;
import com.microsoft.schemas.exchange.services._2006.types.ExtendedPropertyType;
import com.microsoft.schemas.exchange.services._2006.types.FieldURIOrConstantType;
import com.microsoft.schemas.exchange.services._2006.types.FolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.IdFormatType;
import com.microsoft.schemas.exchange.services._2006.types.IsEqualToType;
import com.microsoft.schemas.exchange.services._2006.types.ItemQueryTraversalType;
import com.microsoft.schemas.exchange.services._2006.types.ItemResponseShapeType;
import com.microsoft.schemas.exchange.services._2006.types.ItemType;
import com.microsoft.schemas.exchange.services._2006.types.MapiPropertyTypeType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfAllItemsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfAlternateIdsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfBaseFolderIdsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfPropertyValuesType;
import com.microsoft.schemas.exchange.services._2006.types.PathToExtendedFieldType;
import com.microsoft.schemas.exchange.services._2006.types.PathToIndexedFieldType;
import com.microsoft.schemas.exchange.services._2006.types.RequestServerVersion;
import com.microsoft.schemas.exchange.services._2006.types.ResponseClassType;
import com.microsoft.schemas.exchange.services._2006.types.RestrictionType;
import com.microsoft.schemas.exchange.services._2006.types.ServerVersionInfo;
import com.microsoft.schemas.exchange.services._2006.types.TargetFolderIdType;
import com.novell.ldap.util.Base64;

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
    public static final com.microsoft.schemas.exchange.services._2006.types.ObjectFactory typesObjectFactory = new com.microsoft.schemas.exchange.services._2006.types.ObjectFactory();
    public static final int PID_LID_FILE_UNDER = 0x8005;
    public static final int PID_LID_DISTRIBUTION_LIST_NAME = 0x8053;
    public static final int PID_LID_DISTRIBUTION_LIST_ONE_OFF_MEMBERS = 0X8054;
    public static final int PID_LID_DISTRIBUTION_LIST_MEMBERS = 0x8055;
    public static final String ENTRYID_FLAGS = "00000000";
    public static final String WRAPPED_ENTRYID_PAD = "00000000";
    public static final String WRAPPED_ENTRYID_PROVIDER_UID = "C091ADD3519DCF11A4A900AA0047FAA4";
    public static final String WRAPPED_ENTRYID_TYPE_CONTACT_ENTRYID = "C3";
    public static final String ONEOFF_ENTRYID_PROVIDER_UID = "812B1FA4BEA310199D6E00DD010F5402";
    public static final String ONEOFF_ENTRYID_VERSION = "0000";
    public static final String ONEOFF_ENTRYID_FLAGS = "0190";
    public static final String ONEOFF_ENTRYID_PAD = "0000";
    public static final PathToExtendedFieldType ptefDisplayName = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefDistributionListName = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefFileUnder = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefMembers = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefOneOffMembers = new PathToExtendedFieldType();
    
    static {
        ptefDisplayName.setPropertyTag("0x3001");
        ptefDisplayName.setPropertyType(MapiPropertyTypeType.STRING);
        
        ptefDistributionListName.setPropertyId(PID_LID_DISTRIBUTION_LIST_NAME);
        ptefDistributionListName.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefDistributionListName.setPropertyType(MapiPropertyTypeType.STRING);
        
        ptefFileUnder.setPropertyId(PID_LID_FILE_UNDER);
        ptefFileUnder.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefFileUnder.setPropertyType(MapiPropertyTypeType.STRING);
        
        ptefMembers.setPropertyId(PID_LID_DISTRIBUTION_LIST_MEMBERS);
        ptefMembers.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefMembers.setPropertyType(MapiPropertyTypeType.BINARY_ARRAY);
        
        ptefOneOffMembers.setPropertyId(PID_LID_DISTRIBUTION_LIST_ONE_OFF_MEMBERS);
        ptefOneOffMembers.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefOneOffMembers.setPropertyType(MapiPropertyTypeType.BINARY_ARRAY);
    }

    public static List<ItemType> createContact(User user, ContactItemType contact, BaseFolderIdType contactsFolderId) {
        CreateItemType creator = getCreator(contactsFolderId);
        creator.getItems().getItemOrMessageOrCalendarItem().add(contact);

        return getResponse(user, creator);
    }

    public static List<ItemType> createContact(User user, ContactItemType contact, DistinguishedFolderIdType folderId) {
        CreateItemType creator = getCreator(folderId);
        creator.getItems().getItemOrMessageOrCalendarItem().add(contact);

        return getResponse(user, creator);
    }

    private static CreateItemType getCreator(BaseFolderIdType contactsFolderId) {
        CreateItemType creator = new CreateItemType();
        creator.setSavedItemFolderId(new TargetFolderIdType());
        creator.setItems(new NonEmptyArrayOfAllItemsType());

        if (contactsFolderId instanceof DistinguishedFolderIdType) {
            creator.getSavedItemFolderId().setDistinguishedFolderId((DistinguishedFolderIdType) contactsFolderId);
        } else {
            creator.getSavedItemFolderId().setFolderId((FolderIdType) contactsFolderId);
        }

        return creator;
    }

    public static ContactItemType getContact(User user, String emailAddress, BaseFolderIdType contactsFolderId) {
        ContactItemType contact = null;
        emailAddress = sanitizeString(emailAddress);

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
        ids.add(contactsFolderId);
        finder.setParentFolderIds(folderIds);

        // Identify the field to examine.
        PathToIndexedFieldType pathToIndexedField = new PathToIndexedFieldType();
        pathToIndexedField.setFieldURI(DictionaryURIType.CONTACTS_EMAIL_ADDRESS);
        pathToIndexedField.setFieldIndex("EmailAddress1");

        // Define the type of search filter to apply.
        IsEqualToType equalsExpression = new IsEqualToType();
        equalsExpression.setPath(typesObjectFactory.createPath(pathToIndexedField));

        // Identify the value to compare to the examined field.
        ConstantValueType constantValue = new ConstantValueType();
        constantValue.setValue(emailAddress);
        FieldURIOrConstantType fieldUriOrConstantValue = new FieldURIOrConstantType();
        fieldUriOrConstantValue.setConstant(constantValue);

        // Add the value to the search expression.
        equalsExpression.setFieldURIOrConstant(fieldUriOrConstantValue);

        // Create a restriction.
        RestrictionType restriction = new RestrictionType();

        // Add the search expression to the restriction.
        restriction.setSearchExpression(typesObjectFactory.createIsEqualTo(equalsExpression));

        // Add the restriction to the request.
        // finder.setRestriction(restriction);

        // define response Objects and their holders
        FindItemResponseType findItemResponse = new FindItemResponseType();
        Holder<FindItemResponseType> responseHolder = new Holder<FindItemResponseType>(findItemResponse);

        ServerVersionInfo serverVersion = new ServerVersionInfo();
        Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

        ExchangeServicePortType proxy = null;
        List<JAXBElement<? extends ResponseMessageType>> responses = null;
        try {
            user.getConversion().getReport().start(Report.EXCHANGE_CONNECT);
            proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
            user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
            user.getConversion().getReport().start(Report.EXCHANGE_META);
            proxy.findItem(finder, user.getImpersonation(), responseHolder, serverVersionHolder);
            responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
            user.getConversion().getReport().stop(Report.EXCHANGE_META);

            for (JAXBElement<? extends ResponseMessageType> jaxResponse : responses) {
                ResponseMessageType response = jaxResponse.getValue();
                if (response.getResponseClass().equals(ResponseClassType.ERROR)) {
                    logger.warn("Get Messages Response Error: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.WARNING)) {
                    logger.warn("Get Messages Response Warning: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.SUCCESS)) {
                    FindItemResponseMessageType findResponse = (FindItemResponseMessageType) response;
                    for (ItemType item : findResponse.getRootFolder().getItems().getItemOrMessageOrCalendarItem()) {
                        if (item instanceof ContactItemType) {
                            ContactItemType cTmp = (ContactItemType) item;
                            for (EmailAddressDictionaryEntryType e : cTmp.getEmailAddresses().getEntry()) {
                                if (e.getValue().equalsIgnoreCase(emailAddress)) {
                                    contact = (ContactItemType) item;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new RuntimeException("Exception performing getContacts", e);
        } finally {
            if (user.getConversion().getReport().isStarted(Report.EXCHANGE_META))
                user.getConversion().getReport().stop(Report.EXCHANGE_META);
            if (user.getConversion().getReport().isStarted(Report.EXCHANGE_CONNECT))
                user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
        }

        return contact;
    }

    public static ItemType createDistributionList(User user, String dlName, List<ContactItemType> members, BaseFolderIdType contactsFolderId) {
        dlName = sanitizeString(dlName);
        
        ItemType list = new ItemType();
        
        list.setItemClass("IPM.DistList");
        list.setSubject(dlName);
        list.setBody(new BodyType());
        list.getBody().setValue("");
        list.getBody().setBodyType(BodyTypeType.TEXT);
        
        NonEmptyArrayOfPropertyValuesType wrappedEntryIds = new NonEmptyArrayOfPropertyValuesType();
        // NonEmptyArrayOfPropertyValuesType oneOffEntryIds = new NonEmptyArrayOfPropertyValuesType();
        
        for (ContactItemType member : members) {
            String entryId = createWrappedEntryId(user, member);
            logger.info(String.format("ADD: [ %-16s ] to group [ %s ]", member.getEmailAddresses().getEntry().get(0).getValue(), dlName));
            wrappedEntryIds.getValue().add(entryId);
            
            /*
            String oneoffEntryId = createOneOffMemberEntryId(member);
            oneOffEntryIds.getValue().add(oneoffEntryId);
            wrappedEntryIds.getValue().add(oneoffEntryId);
            */
        }
        
        List<ExtendedPropertyType> props = new ArrayList<ExtendedPropertyType>();
        
        ExtendedPropertyType displayName = new ExtendedPropertyType();
        displayName.setExtendedFieldURI(ptefDisplayName);
        displayName.setValue(dlName);
        props.add(displayName);
        
        ExtendedPropertyType distributionListName = new ExtendedPropertyType();
        distributionListName.setExtendedFieldURI(ptefDistributionListName);
        distributionListName.setValue(dlName);
        props.add(distributionListName);
        
        ExtendedPropertyType fileUnder = new ExtendedPropertyType();
        fileUnder.setExtendedFieldURI(ptefFileUnder);
        fileUnder.setValue(dlName);
        props.add(fileUnder);
        
        ExtendedPropertyType dlMembers = new ExtendedPropertyType();
        dlMembers.setExtendedFieldURI(ptefMembers);
        dlMembers.setValues(wrappedEntryIds);
        props.add(dlMembers);
        
        /*
        ExtendedPropertyType dlOneOffMembers = new ExtendedPropertyType();
        dlOneOffMembers.setExtendedFieldURI(ptefOneOffMembers);
        dlOneOffMembers.setValues(oneOffEntryIds);
        props.add(dlOneOffMembers);
        */
        
        list.getExtendedProperty().addAll(props);
        
        CreateItemType creator = getCreator(contactsFolderId);
        creator.getItems().getItemOrMessageOrCalendarItem().add(list);

        return getResponse(user, creator).get(0);
    }
    
    private static String createWrappedEntryId(User user, ItemType entry) {
        String retval = "";
        String wrappedEntryIDPreamble = 
            ContactUtil.ENTRYID_FLAGS + 
            ContactUtil.WRAPPED_ENTRYID_PROVIDER_UID + 
            ContactUtil.WRAPPED_ENTRYID_TYPE_CONTACT_ENTRYID;
        
        ConvertIdType convertReq = new ConvertIdType();
        convertReq.setDestinationFormat(IdFormatType.HEX_ENTRY_ID);
        convertReq.setSourceIds(new NonEmptyArrayOfAlternateIdsType());
        
        AlternateIdType altId = new AlternateIdType();
        altId.setFormat(IdFormatType.ENTRY_ID);
        altId.setId(entry.getItemId().getId());
        altId.setMailbox(user.getPrimarySMTPAddress());
        
        convertReq.getSourceIds().getAlternateIdOrAlternatePublicFolderIdOrAlternatePublicFolderItemId().add(altId);
        
        try {
            ConvertIdResponseType convertIdResponse = new ConvertIdResponseType();
            Holder<ConvertIdResponseType> responseHolder = new Holder<ConvertIdResponseType>(convertIdResponse);
            
            ServerVersionInfo serverVersion = new ServerVersionInfo();
            Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);
            
            RequestServerVersion requestVersion = new RequestServerVersion();
            requestVersion.setVersion(ExchangeVersionType.EXCHANGE_2007_SP_1);
            
            ExchangeServicePortType proxy = null;
            List<JAXBElement<? extends ResponseMessageType>> responses = null;
            try {
                user.getConversion().getReport().start(Report.EXCHANGE_CONNECT);
                proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
                user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
                user.getConversion().getReport().start(Report.EXCHANGE_MIME);
                proxy.convertId(convertReq, requestVersion, responseHolder, serverVersionHolder);
                // proxy.convertId(convertReq, responseHolder, serverVersionHolder);
                responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
                user.getConversion().getReport().stop(Report.EXCHANGE_MIME);
                for (JAXBElement<? extends ResponseMessageType> jaxResponse : responses) {
                    ResponseMessageType response = jaxResponse.getValue();
                    if (response.getResponseClass().equals(ResponseClassType.ERROR)) {
                        try {
                            logger.warn("ConvertId Response Error [" + response.getMessageText() + "]");
                            user.getConversion().warnings++;
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            logger.warn("ConvertId In Exchange Response Error - unable to determine source message.");
                            user.getConversion().warnings++;
                        }
                    } else if (response.getResponseClass().equals(ResponseClassType.WARNING)) {
                        logger.warn("ConvertId In Exchange Response Warning: " + response.getMessageText());
                        user.getConversion().warnings++;
                    } else if (response.getResponseClass().equals(ResponseClassType.SUCCESS)) {
                        ConvertIdResponseMessageType cirmt = (ConvertIdResponseMessageType) response;
                        AlternateIdType myId = (AlternateIdType) cirmt.getAlternateId();
                        if (myId != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(wrappedEntryIDPreamble);
                            sb.append(myId.getId().substring(44));
                            retval = sb.toString();
                        }
                    }
                } 
            } catch (Exception e) {
                logger.warn(e.getMessage());
                //throw new RuntimeException("Exception calling ConvertId on Exchange Server: " + e.getMessage(), e);
            } finally {
                if (user.getConversion().getReport().isStarted(Report.EXCHANGE_MIME))
                    user.getConversion().getReport().stop(Report.EXCHANGE_MIME);
                if (user.getConversion().getReport().isStarted(Report.EXCHANGE_CONNECT))
                    user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
            }
        } catch (Exception e) {
            logger.warn("Could not create WrappedEntryId: " + e.getMessage());
        }
        
        return Base64.encode(hexStringToByteArray(retval));
    }

    
    private static String createOneOffMemberEntryId(ContactItemType entry) {
        StringBuilder sb = new StringBuilder();
        
        String emailAddress = sanitizeString(entry.getEmailAddresses().getEntry().get(0).getValue());
        String first = sanitizeString(entry.getDisplayName());
        String middle = "SMTP";
        String last = emailAddress;
        
        sb.append(ENTRYID_FLAGS);
        sb.append(ONEOFF_ENTRYID_PROVIDER_UID);
        sb.append(ONEOFF_ENTRYID_VERSION);
        sb.append(ONEOFF_ENTRYID_FLAGS);
        sb.append(convertToHexString(first));
        sb.append(ONEOFF_ENTRYID_PAD);
        sb.append(convertToHexString(middle));
        sb.append(ONEOFF_ENTRYID_PAD);
        sb.append(convertToHexString(last));
        sb.append(ONEOFF_ENTRYID_PAD);
        
        //logger.debug(sb.toString());
        return Base64.encode(hexStringToByteArray(sb.toString()));
    }
    
    
    private static String convertToHexString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(Integer.toHexString(c));
            sb.append("00");
        }
        return sb.toString();
    }
    
    public static List<ItemType> getResponse(User user, CreateItemType creator) {
        List<ItemType> items = new ArrayList<ItemType>();
        // define response Objects and their holders
        CreateItemResponseType createItemResponse = new CreateItemResponseType();
        Holder<CreateItemResponseType> responseHolder = new Holder<CreateItemResponseType>(createItemResponse);

        ServerVersionInfo serverVersion = new ServerVersionInfo();
        Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

        ExchangeServicePortType proxy = null;
        List<JAXBElement<? extends ResponseMessageType>> responses = null;
        try {
            user.getConversion().getReport().start(Report.EXCHANGE_CONNECT);
            proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
            user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
            user.getConversion().getReport().start(Report.EXCHANGE_MIME);
            proxy.createItem(creator, user.getImpersonation(), responseHolder, serverVersionHolder);
            responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
            user.getConversion().getReport().stop(Report.EXCHANGE_MIME);
            int i = 0;
            for (JAXBElement<? extends ResponseMessageType> jaxResponse : responses) {
                ResponseMessageType response = jaxResponse.getValue();
                if (response.getResponseClass().equals(ResponseClassType.ERROR)) {
                    try {
                        logger.warn("Create Item in Exchange Response Error [" + response.getMessageText() + "]");
                        user.getConversion().warnings++;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        logger.warn("Create Distribution List In Exchange Response Error - unable to determine source item.");
                        user.getConversion().warnings++;
                    }
                } else if (response.getResponseClass().equals(ResponseClassType.WARNING)) {
                    logger.warn("Create Distribution List In Exchange Response Warning: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.SUCCESS)) {
                    for (ItemType item : ((ItemInfoResponseMessageType) response).getItems().getItemOrMessageOrCalendarItem()) {
                        items.add(item);
                    }
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception creating contact on Exchange Server: " + e.getMessage(), e);
        } finally {
            if (user.getConversion().getReport().isStarted(Report.EXCHANGE_MIME))
                user.getConversion().getReport().stop(Report.EXCHANGE_MIME);
            if (user.getConversion().getReport().isStarted(Report.EXCHANGE_CONNECT))
                user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
        }

        return items;
    }
    
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte [len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)(
                    (Character.digit(s.charAt(i), 16) << 4) + 
                     Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    private static String sanitizeString(String s) {
        return s.trim();
    }

}
