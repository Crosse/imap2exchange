package edu.jmu.email.conversion.exchange;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.Holder;
import javax.xml.bind.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.exchange.ExchangeServerPortFactory;
import edu.yale.its.tp.email.conversion.exchange.FolderUtil;
import edu.yale.its.tp.email.conversion.exchange.flags.*;
import com.microsoft.schemas.exchange.services._2006.messages.*;
import com.microsoft.schemas.exchange.services._2006.types.*;

public class ContactsFolderUtil {
    private static final Log logger = LogFactory.getLog(ContactsFolderUtil.class);

    public static boolean folderExists(User user, String folderName) {
        DistinguishedFolderIdType contacts = new DistinguishedFolderIdType();
        contacts.setId(DistinguishedFolderIdNameType.CONTACTS);
        return folderExists(user, folderName, contacts);
    }

    public static boolean folderExists(User user, String folderName, BaseFolderIdType parentFolderId) {
        List<BaseFolderType> childFolders = FolderUtil.getChildFolders(user, parentFolderId);
        boolean exists = false;
        for (BaseFolderType folder : childFolders) {
            if (folder.getDisplayName() == folderName) {
                exists = true;
                break;
            }
        }
        return exists;
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
        for(String folderName : folderNames){
            BaseFolderType folder = new ContactsFolderType();
            folder.setDisplayName(folderName);
            // folder.setFolderClass(EXCHANGE_MAIL_FOLDER_CLASS);
            folders.add(folder);
        }

        // Make the target folder id
        TargetFolderIdType targetFolderId = new TargetFolderIdType();
        if(parentFolderId instanceof FolderIdType){
            targetFolderId.setFolderId((FolderIdType)parentFolderId);
        } else if (parentFolderId instanceof DistinguishedFolderIdType){
            targetFolderId.setDistinguishedFolderId((DistinguishedFolderIdType)parentFolderId);
        }

        creator.setFolders(folderArray);
        creator.setParentFolderId(targetFolderId);

        // define response Objects and their holders
        CreateFolderResponseType createFolderResponse = new CreateFolderResponseType();
        Holder<CreateFolderResponseType> responseHolder = new Holder<CreateFolderResponseType>(createFolderResponse);

        ServerVersionInfo serverVersion = new ServerVersionInfo();
        Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

        ExchangeServicePortType proxy = null;
        List<JAXBElement <? extends ResponseMessageType>> responses = null;
        try{
            user.getConversion().getReport().start(Report.EXCHANGE_CONNECT);
            proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
            user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
            user.getConversion().getReport().start(Report.EXCHANGE_META);
            proxy.createFolder(creator, user.getImpersonation() ,responseHolder, serverVersionHolder);
            responses = responseHolder.value.getResponseMessages()
                .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
            user.getConversion().getReport().stop(Report.EXCHANGE_META);

            for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
                ResponseMessageType response = jaxResponse.getValue();
                if(response.getResponseClass().equals(ResponseClassType.ERROR)){
                    logger.warn("Create Folder Response Error: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
                    logger.warn("Create Folder Response Warning: " + response.getMessageText());
                    user.getConversion().warnings++;
                } else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
                    FolderInfoResponseMessageType findResponse = (FolderInfoResponseMessageType)response;
                    List<BaseFolderType> allFolders =  findResponse.getFolders().getFolderOrCalendarFolderOrContactsFolder();
                    for(BaseFolderType folder : allFolders){
                        returnList.add((ContactsFolderType)folder);
                    }
                }
            }
        } catch (Exception e){
            throw new RuntimeException("Exception performing CreateFolder", e);
        } finally {
            if(user.getConversion().getReport().isStarted(Report.EXCHANGE_META))
                user.getConversion().getReport().stop(Report.EXCHANGE_META);
            if(user.getConversion().getReport().isStarted(Report.EXCHANGE_CONNECT))
                user.getConversion().getReport().stop(Report.EXCHANGE_CONNECT);
        }

        return returnList;
    }
}
