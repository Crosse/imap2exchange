package edu.jmu.email.conversion.exchange;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.schemas.exchange.services._2006.messages.CreateFolderResponseType;
import com.microsoft.schemas.exchange.services._2006.messages.CreateFolderType;
import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServicePortType;
import com.microsoft.schemas.exchange.services._2006.messages.FolderInfoResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.messages.ResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.types.BaseFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.BaseFolderType;
import com.microsoft.schemas.exchange.services._2006.types.ContactsFolderType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdNameType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.FolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfFoldersType;
import com.microsoft.schemas.exchange.services._2006.types.ResponseClassType;
import com.microsoft.schemas.exchange.services._2006.types.ServerVersionInfo;
import com.microsoft.schemas.exchange.services._2006.types.TargetFolderIdType;

import edu.yale.its.tp.email.conversion.Report;
import edu.yale.its.tp.email.conversion.User;
import edu.yale.its.tp.email.conversion.exchange.ExchangeServerPortFactory;
import edu.yale.its.tp.email.conversion.exchange.FolderUtil;

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
public class ContactsFolderUtil {
    private static final Log logger = LogFactory.getLog(ContactsFolderUtil.class);

    public static boolean folderExists(User user, String folderName) {
        DistinguishedFolderIdType contacts = new DistinguishedFolderIdType();
        contacts.setId(DistinguishedFolderIdNameType.CONTACTS);
        return folderExists(user, folderName, contacts);
    }

    public static boolean folderExists(User user, String folderName, BaseFolderIdType parentFolderId) {
        boolean exists = false;
        if (getFolder(user, folderName, parentFolderId) != null) {
            exists = true;
        }
        return exists;
    }

    public static BaseFolderIdType getRootContactsFolderId(User user) {
        DistinguishedFolderIdType parentFolderId = new DistinguishedFolderIdType();
        parentFolderId.setId(DistinguishedFolderIdNameType.CONTACTS);

        return parentFolderId;
    }

    public static FolderIdType getFolder(User user, String folderName, BaseFolderIdType parentFolderId) {
        ContactsFolderType folder = null;

        List<BaseFolderType> childFolders = FolderUtil.getChildFolders(user, parentFolderId);
        for (BaseFolderType bFolder : childFolders) {
            if (folderName.equalsIgnoreCase(bFolder.getDisplayName())) {
                logger.debug(String.format("Found requested folder \"%s\"", bFolder.getDisplayName()));
                folder = (ContactsFolderType) bFolder;
                break;
            }
        }
        if (folder == null) {
            logger.debug(String.format("Folder \"%s\" does not exist", folderName));
        }
        return folder.getFolderId();
    }

    public static ContactsFolderType createFolder(User user, String folderName, BaseFolderIdType parentFolderId) {
        List<String> folderNames = new ArrayList<String>();
        folderNames.add(folderName);
        List<ContactsFolderType> folders = createFolders(user, folderNames, parentFolderId);
        if (folders.size() == 1) {
            return folders.get(0);
        } else {
            return null;
        }
    }

    public static List<ContactsFolderType> createFolders(User user, List<String> folderNames, BaseFolderIdType parentFolderId) {
        List<ContactsFolderType> returnList = new ArrayList<ContactsFolderType>();
        CreateFolderType creator = new CreateFolderType();

        // Make the NonEmptyArrayOfFolders
        NonEmptyArrayOfFoldersType folderArray = new NonEmptyArrayOfFoldersType();
        List<BaseFolderType> folders = folderArray.getFolderOrCalendarFolderOrContactsFolder();
        for (String folderName : folderNames) {
            BaseFolderType folder = new ContactsFolderType();
            folder.setDisplayName(folderName);
            // folder.setFolderClass(EXCHANGE_MAIL_FOLDER_CLASS);
            folders.add(folder);
        }

        // Make the target folder id
        TargetFolderIdType targetFolderId = new TargetFolderIdType();
        if (parentFolderId instanceof FolderIdType) {
            targetFolderId.setFolderId((FolderIdType) parentFolderId);
        } else if (parentFolderId instanceof DistinguishedFolderIdType) {
            targetFolderId.setDistinguishedFolderId((DistinguishedFolderIdType) parentFolderId);
        }

        creator.setFolders(folderArray);
        creator.setParentFolderId(targetFolderId);

        // define response Objects and their holders
        CreateFolderResponseType createFolderResponse = new CreateFolderResponseType();
        Holder<CreateFolderResponseType> responseHolder = new Holder<CreateFolderResponseType>(createFolderResponse);

        ServerVersionInfo serverVersion = new ServerVersionInfo();
        Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

        ExchangeServicePortType proxy = null;
        List<JAXBElement<? extends ResponseMessageType>> responses = null;
        try {
            user.getConversion().getReport().start(Report.EXCHANGE_CONNECT);
            proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
            user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
            user.getConversion().getReport().start(Report.EXCHANGE_META);
            proxy.createFolder(creator, user.getImpersonation(), responseHolder, serverVersionHolder);
            responses = responseHolder.value.getResponseMessages().getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
            user.getConversion().getReport().stop(Report.EXCHANGE_META);

            for (JAXBElement<? extends ResponseMessageType> jaxResponse : responses) {
                ResponseMessageType response = jaxResponse.getValue();
                if (response.getResponseClass().equals(ResponseClassType.ERROR)) {
                    logger.warn("Create Folder Response Error: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.WARNING)) {
                    logger.warn("Create Folder Response Warning: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if (response.getResponseClass().equals(ResponseClassType.SUCCESS)) {
                    FolderInfoResponseMessageType findResponse = (FolderInfoResponseMessageType) response;
                    List<BaseFolderType> allFolders = findResponse.getFolders().getFolderOrCalendarFolderOrContactsFolder();
                    for (BaseFolderType folder : allFolders) {
                        returnList.add((ContactsFolderType) folder);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception performing CreateFolder", e);
        } finally {
            if (user.getConversion().getReport().isStarted(Report.EXCHANGE_META))
                user.getConversion().getReport().stop(Report.EXCHANGE_META);
            if (user.getConversion().getReport().isStarted(Report.EXCHANGE_CONNECT))
                user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
        }

        return returnList;
    }
}
