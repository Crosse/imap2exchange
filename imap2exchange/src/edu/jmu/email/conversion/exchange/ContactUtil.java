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
import com.microsoft.schemas.exchange.services._2006.messages.GetItemResponseType;
import com.microsoft.schemas.exchange.services._2006.messages.GetItemType;
import com.microsoft.schemas.exchange.services._2006.messages.ItemInfoResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.messages.ResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.types.AlternateIdType;
import com.microsoft.schemas.exchange.services._2006.types.BaseFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.BodyType;
import com.microsoft.schemas.exchange.services._2006.types.BodyTypeType;
import com.microsoft.schemas.exchange.services._2006.types.ContactItemType;
import com.microsoft.schemas.exchange.services._2006.types.DefaultShapeNamesType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedPropertySetType;
import com.microsoft.schemas.exchange.services._2006.types.ExchangeVersionType;
import com.microsoft.schemas.exchange.services._2006.types.ExtendedPropertyType;
import com.microsoft.schemas.exchange.services._2006.types.FolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.IdFormatType;
import com.microsoft.schemas.exchange.services._2006.types.ItemIdType;
import com.microsoft.schemas.exchange.services._2006.types.ItemQueryTraversalType;
import com.microsoft.schemas.exchange.services._2006.types.ItemResponseShapeType;
import com.microsoft.schemas.exchange.services._2006.types.ItemType;
import com.microsoft.schemas.exchange.services._2006.types.MapiPropertyTypeType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfAllItemsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfAlternateIdsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfBaseFolderIdsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfBaseItemIdsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfPropertyValuesType;
import com.microsoft.schemas.exchange.services._2006.types.PathToExtendedFieldType;
import com.microsoft.schemas.exchange.services._2006.types.RequestServerVersion;
import com.microsoft.schemas.exchange.services._2006.types.ResponseClassType;
import com.microsoft.schemas.exchange.services._2006.types.ServerVersionInfo;
import com.microsoft.schemas.exchange.services._2006.types.TargetFolderIdType;
import com.novell.ldap.util.Base64;

import edu.yale.its.tp.email.conversion.ExchangeConversion;
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
    public static final int PID_LID_DISTRIBUTION_LIST_MEMBERS_MAX_LENGTH = 15000;
    public static final int PID_LID_DISTRIBUTION_LIST_ONE_OFF_MEMBERS = 0X8054;
    public static final int PID_LID_DISTRIBUTION_LIST_MEMBERS = 0x8055;
    public static final int PID_LID_EMAIL1_DISPLAY_NAME = 0x8080;
    public static final int PID_LID_EMAIL1_ADDRESS_TYPE = 0x8082;
    public static final int PID_LID_EMAIL1_EMAIL_ADDRESS = 0x8083;
    public static final int PID_LID_EMAIL1_ORIGINAL_DISPLAY_NAME = 0x8084;
    public static final int PID_LID_EMAIL1_ORIGINAL_ENTRY_ID = 0x8085;
    public static final String ENTRYID_FLAGS = "00000000";
    public static final String WRAPPED_ENTRYID_PAD = "00000000";
    public static final String WRAPPED_ENTRYID_PROVIDER_UID = "C091ADD3519DCF11A4A900AA0047FAA4";
    public static final String WRAPPED_ENTRYID_TYPE_CONTACT_ENTRYID = "C3";
    public static final String ONEOFF_ENTRYID_PROVIDER_UID = "812B1FA4BEA310199D6E00DD010F5402";
    public static final String ONEOFF_ENTRYID_VERSION = "0000";
    public static final String ONEOFF_ENTRYID_FLAGS = "0190";
    public static final String ONEOFF_ENTRYID_PAD = "0000";
    public static final String DISTRIBUTION_LIST_ITEM_CLASS = "IPM.DistList";
    public static final PathToExtendedFieldType ptefEmail1DisplayName = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefEmail1AddressType = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefEmail1EmailAddress = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefEmail1OriginalDisplayName = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefEmail1OriginalEntryID = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefDisplayName = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefDistributionListName = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefFileUnder = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefMembers = new PathToExtendedFieldType();
    public static final PathToExtendedFieldType ptefOneOffMembers = new PathToExtendedFieldType();

    static {
        ptefEmail1DisplayName.setPropertyId(PID_LID_EMAIL1_DISPLAY_NAME);
        ptefEmail1DisplayName.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefEmail1DisplayName.setPropertyType(MapiPropertyTypeType.STRING);

        ptefEmail1AddressType.setPropertyId(PID_LID_EMAIL1_ADDRESS_TYPE);
        ptefEmail1AddressType.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefEmail1AddressType.setPropertyType(MapiPropertyTypeType.STRING);

        ptefEmail1EmailAddress.setPropertyId(PID_LID_EMAIL1_EMAIL_ADDRESS);
        ptefEmail1EmailAddress.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefEmail1EmailAddress.setPropertyType(MapiPropertyTypeType.STRING);

        ptefEmail1OriginalDisplayName.setPropertyId(PID_LID_EMAIL1_ORIGINAL_DISPLAY_NAME);
        ptefEmail1OriginalDisplayName.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefEmail1OriginalDisplayName.setPropertyType(MapiPropertyTypeType.STRING);

        ptefEmail1OriginalEntryID.setPropertyId(PID_LID_EMAIL1_ORIGINAL_ENTRY_ID);
        ptefEmail1OriginalEntryID.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
        ptefEmail1OriginalEntryID.setPropertyType(MapiPropertyTypeType.BINARY);

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

        return getCreateItemResponse(user, creator);
    }

    public static List<ItemType> createContact(User user, ContactItemType contact, DistinguishedFolderIdType folderId) {
        CreateItemType creator = getCreator(folderId);
        creator.getItems().getItemOrMessageOrCalendarItem().add(contact);

        return getCreateItemResponse(user, creator);
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

    public static ArrayList<ItemType> getContacts(BaseFolderIdType contactsFolderId) {
        ArrayList<ItemType> contacts = new ArrayList<ItemType>();

        // Form the FindItem request.
        FindItemType finder = new FindItemType();

        // Define which item properties are returned in the response.
        ItemResponseShapeType itemShape = new ItemResponseShapeType();
        itemShape.setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);
        finder.setItemShape(itemShape);

        // Choose the traversal mode.
        finder.setTraversal(ItemQueryTraversalType.SHALLOW);

        NonEmptyArrayOfBaseFolderIdsType folderIds = new NonEmptyArrayOfBaseFolderIdsType();
        List<BaseFolderIdType> ids = folderIds.getFolderIdOrDistinguishedFolderId();
        ids.add(contactsFolderId);
        finder.setParentFolderIds(folderIds);

        // define response Objects and their holders
        FindItemResponseType findItemResponse = new FindItemResponseType();
        Holder<FindItemResponseType> responseHolder = new Holder<FindItemResponseType>(findItemResponse);

        ServerVersionInfo serverVersion = new ServerVersionInfo();
        Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

        ExchangeServicePortType proxy = null;
        List<JAXBElement<? extends ResponseMessageType>> responses = null;
        User user = ExchangeConversion.getConv().getUser();
        try {
            Report.getReport().start(Report.EXCHANGE_CONNECT);
            proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
            Report.getReport().stop(Report.EXCHANGE_CONNECT);
            Report.getReport().start(Report.EXCHANGE_META);
            proxy.findItem(finder, user.getImpersonation(), responseHolder, serverVersionHolder);
            responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
            Report.getReport().stop(Report.EXCHANGE_META);

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
                        contacts.add(item);
                    }
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            //e.printStackTrace();
            throw new RuntimeException("Exception performing getContacts", e);
        } finally {
            if (Report.getReport().isStarted(Report.EXCHANGE_META))
                Report.getReport().stop(Report.EXCHANGE_META);
            if (Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
                Report.getReport().stop(Report.EXCHANGE_CONNECT);
        }

        return contacts;
    }

    public static ContactItemType findContactByEntryId(User user, ItemIdType id) {
        ContactItemType contact = null;

        GetItemType getItem = new GetItemType();
        ItemResponseShapeType itemShape = new ItemResponseShapeType();
        itemShape.setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);
        getItem.setItemShape(itemShape);

        NonEmptyArrayOfBaseItemIdsType ids = new NonEmptyArrayOfBaseItemIdsType();
        ids.getItemIdOrOccurrenceItemIdOrRecurringMasterItemId().add(id);
        getItem.setItemIds(ids);

        // define response Objects and their holders
        GetItemResponseType findItemResponse = new GetItemResponseType();
        Holder<GetItemResponseType> responseHolder = new Holder<GetItemResponseType>(findItemResponse);

        ServerVersionInfo serverVersion = new ServerVersionInfo();
        Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

        ExchangeServicePortType proxy = null;
        List<JAXBElement<? extends ResponseMessageType>> responses = null;
        try {
            Report.getReport().start(Report.EXCHANGE_CONNECT);
            proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
            Report.getReport().stop(Report.EXCHANGE_CONNECT);
            Report.getReport().start(Report.EXCHANGE_META);
            proxy.getItem(getItem, user.getImpersonation(), responseHolder, serverVersionHolder);
            responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
            Report.getReport().stop(Report.EXCHANGE_META);

            for (JAXBElement<? extends ResponseMessageType> jaxResponse : responses) {
                ResponseMessageType response = jaxResponse.getValue();
                if (response.getResponseClass().equals(ResponseClassType.ERROR)) {
                    logger.warn("Get Items by ItemId Response Error: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.WARNING)) {
                    logger.warn("Get Items by ItemId Response Warning: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.SUCCESS)) {
                    ItemInfoResponseMessageType getResponse = (ItemInfoResponseMessageType) response;
                    for (ItemType item : getResponse.getItems().getItemOrMessageOrCalendarItem()) {
                        if (item instanceof ContactItemType) {
                            contact = (ContactItemType)item;
                            break;
                        }
                    }
                }
                if (contact != null) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            //e.printStackTrace();
            throw new RuntimeException("Exception performing getContactsByItemId", e);
        } finally {
            if (Report.getReport().isStarted(Report.EXCHANGE_META))
                Report.getReport().stop(Report.EXCHANGE_META);
            if (Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
                Report.getReport().stop(Report.EXCHANGE_CONNECT);
        }

        return contact;
    }

    public static ItemType createDistributionList(User user, String dlName, List<ContactItemType> members, BaseFolderIdType contactsFolderId) {
        dlName = sanitizeString(dlName);

        ItemType list = new ItemType();

        list.setItemClass(DISTRIBUTION_LIST_ITEM_CLASS);
        list.setSubject(dlName);
        list.setBody(new BodyType());
        list.getBody().setValue("");
        list.getBody().setBodyType(BodyTypeType.TEXT);

        NonEmptyArrayOfPropertyValuesType wrappedEntryIds = new NonEmptyArrayOfPropertyValuesType();
        NonEmptyArrayOfPropertyValuesType oneOffEntryIds = new NonEmptyArrayOfPropertyValuesType();

        int wrappedEntryIdsLength = 0;
        int oneOffEntryIdsLength = 0;
        for (ContactItemType member : members) {
            logger.debug(String.format("ADD: [%-16s] to group [%s]", member.getEmailAddresses().getEntry().get(0).getValue(), dlName));

            String entryId = createWrappedEntryId(user, member);
            wrappedEntryIds.getValue().add(Base64.encode(hexStringToByteArray(entryId)));
            wrappedEntryIdsLength += entryId.length();
            if (wrappedEntryIdsLength > PID_LID_DISTRIBUTION_LIST_MEMBERS_MAX_LENGTH) {
                logger.warn(String.format(
                                          "DL [%s] has grown to %d bytes in size, which is larger than the allowed limit (%d bytes); refusing to create.", 
                                          dlName, 
                                          wrappedEntryIdsLength,
                                          PID_LID_DISTRIBUTION_LIST_MEMBERS_MAX_LENGTH));
                return null;
            }

            String oneoffEntryId = createOneOffEntryId(member);
            oneOffEntryIds.getValue().add(Base64.encode(hexStringToByteArray(oneoffEntryId)));
            oneOffEntryIdsLength += oneoffEntryId.length();
            if (oneOffEntryIdsLength > PID_LID_DISTRIBUTION_LIST_MEMBERS_MAX_LENGTH) {
                logger.warn(String.format(
                                          "DL [%s] has grown to %d bytes in size, which is larger than the allowed limit (%d bytes); refusing to create.", 
                                          dlName, 
                                          oneOffEntryIdsLength,
                                          PID_LID_DISTRIBUTION_LIST_MEMBERS_MAX_LENGTH));
                return null;
            }

        }

        logger.info(String.format("DL [%s] contains %d members", dlName, wrappedEntryIds.getValue().size()));

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

        ExtendedPropertyType dlOneOffMembers = new ExtendedPropertyType();
        dlOneOffMembers.setExtendedFieldURI(ptefOneOffMembers);
        dlOneOffMembers.setValues(oneOffEntryIds);
        props.add(dlOneOffMembers);

        list.getExtendedProperty().addAll(props);

        CreateItemType creator = getCreator(contactsFolderId);
        creator.getItems().getItemOrMessageOrCalendarItem().add(list);

        return getCreateItemResponse(user, creator).get(0);
    }

    public static String createWrappedEntryId(User user, ItemType entry) {
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
                Report.getReport().start(Report.EXCHANGE_CONNECT);
                proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
                Report.getReport().stop(Report.EXCHANGE_CONNECT);
                Report.getReport().start(Report.EXCHANGE_MIME);
                proxy.convertId(convertReq, requestVersion, responseHolder, serverVersionHolder);
                // proxy.convertId(convertReq, responseHolder, serverVersionHolder);
                responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
                Report.getReport().stop(Report.EXCHANGE_MIME);
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
                throw new RuntimeException("Exception calling ConvertId on Exchange Server: " + e.getMessage(), e);
            } finally {
                if (Report.getReport().isStarted(Report.EXCHANGE_MIME))
                    Report.getReport().stop(Report.EXCHANGE_MIME);
                if (Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
                    Report.getReport().stop(Report.EXCHANGE_CONNECT);
            }
        } catch (Exception e) {
            logger.warn("Could not create WrappedEntryId: " + e.getMessage());
        }

        return retval;
    }


    public static String createOneOffEntryId(String displayName, String emailAddress) {
        StringBuilder sb = new StringBuilder();

        emailAddress = sanitizeString(emailAddress);
        String first = sanitizeString(displayName);
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
        return sb.toString();
    }

    public static String createOneOffEntryId(ContactItemType entry) {
        String emailAddress = sanitizeString(entry.getEmailAddresses().getEntry().get(0).getValue());
        String displayName = sanitizeString(entry.getDisplayName());

        return createOneOffEntryId(displayName, emailAddress);
    }

    public static String createOneOffEntryIdInBase64(String displayName, String emailAddress) {
        return Base64.encode(hexStringToByteArray(createOneOffEntryId(displayName, emailAddress)));
    }

    public static String createOneOffEntryIdInBase64(ContactItemType entry) {
        return Base64.encode(hexStringToByteArray(createOneOffEntryId(entry)));
    }

    private static String convertToHexString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c < 0x20) {
                // Somehow the user encoded a control character into a string.
                // Replace it with a space (0x20).
                sb.append("20");
            } else {
                sb.append(Integer.toHexString(c));
            }
            // Append "00" because the string is supposed to be Unicode or something.
            sb.append("00");
        }
        return sb.toString();
    }

    public static List<ItemType> getCreateItemResponse(User user, CreateItemType creator) {
        List<ItemType> items = new ArrayList<ItemType>();
        // define response Objects and their holders
        CreateItemResponseType createItemResponse = new CreateItemResponseType();
        Holder<CreateItemResponseType> responseHolder = new Holder<CreateItemResponseType>(createItemResponse);

        ServerVersionInfo serverVersion = new ServerVersionInfo();
        Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

        ExchangeServicePortType proxy = null;
        List<JAXBElement<? extends ResponseMessageType>> responses = null;
        try {
            Report.getReport().start(Report.EXCHANGE_CONNECT);
            proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
            Report.getReport().stop(Report.EXCHANGE_CONNECT);
            Report.getReport().start(Report.EXCHANGE_MIME);
            proxy.createItem(creator, user.getImpersonation(), responseHolder, serverVersionHolder);
            responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
            Report.getReport().stop(Report.EXCHANGE_MIME);
            int i = 0;
            for (JAXBElement<? extends ResponseMessageType> jaxResponse : responses) {
                ResponseMessageType response = jaxResponse.getValue();
                if (response.getResponseClass().equals(ResponseClassType.ERROR)) {
                    try {
                        logger.warn("Create Item in Exchange Response Error [" + response.getMessageText() + "]");
                        user.getConversion().warnings++;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        logger.warn("Create Item in Exchange Response Error - unable to determine source item.");
                        user.getConversion().warnings++;
                    }
                } else if (response.getResponseClass().equals(ResponseClassType.WARNING)) {
                    logger.warn("Create Item in Exchange Response Warning: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.SUCCESS)) {
                    for (ItemType item : ((ItemInfoResponseMessageType) response).getItems().getItemOrMessageOrCalendarItem()) {
                        items.add(item);
                    }
                }
                i++;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new RuntimeException("Exception creating contact on Exchange Server: " + e.getMessage(), e);
        } finally {
            if (Report.getReport().isStarted(Report.EXCHANGE_MIME))
                Report.getReport().stop(Report.EXCHANGE_MIME);
            if (Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
                Report.getReport().stop(Report.EXCHANGE_CONNECT);
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

    public static String sanitizeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c < 0x20) {
                sb.append(" ");
            } else {
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }
}
