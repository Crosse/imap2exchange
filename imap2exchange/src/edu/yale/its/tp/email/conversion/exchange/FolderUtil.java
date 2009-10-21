package edu.yale.its.tp.email.conversion.exchange;

import java.util.*;
import com.sun.mail.imap.*;
import javax.xml.ws.Holder;
import javax.xml.bind.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.exchange.flags.*;
import com.microsoft.schemas.exchange.services._2006.messages.*;
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
public class FolderUtil {
	
	private static final Log logger = LogFactory.getLog(FolderUtil.class);
	
	public static String EXCHANGE_MAIL_FOLDER_CLASS = "IPF.Note";

	public static BaseFolderIdType getRootFolderId(){
		DistinguishedFolderIdType rootId = new DistinguishedFolderIdType();
		rootId.setId(DistinguishedFolderIdNameType.MSGFOLDERROOT);
		return rootId;
	}

	public static List<BaseFolderType> getRootMailFolders(){
		return getChildFolders(getRootFolderId());
	}

//	public static List<BaseFolderType> getChildFolders(User user, BaseFolderIdType parentFolderId){
//		return getFolders(user, findChildFolders(user, parentFolderId));
//	}

	public static List<BaseFolderType> getChildFolders(BaseFolderIdType parentFolderId){

		// Create FindFolder
		FindFolderType finder = new FindFolderType();
		
		NonEmptyArrayOfPathsToElementType paths = new NonEmptyArrayOfPathsToElementType();
		paths.getPath().add(PrStatus.PR_STATUS_PATH);
//		PathToUnindexedFieldType path = new PathToUnindexedFieldType();
//		path.setFieldURI(UnindexedFieldURIType.FOLDER_STATUS);
//		paths.getPath().add(Config.typesObjectFactory.createFieldURI(path));

		FolderResponseShapeType folderShape = new FolderResponseShapeType();
		folderShape.setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);
		folderShape.setAdditionalProperties(paths);
		
		finder.setFolderShape(folderShape);
		finder.setTraversal(FolderQueryTraversalType.SHALLOW);
		
		IndexedPageViewType index = new IndexedPageViewType();
		index.setBasePoint(IndexBasePointType.BEGINNING);
		index.setOffset(0);
		
		finder.setIndexedPageFolderView(index);
		
		NonEmptyArrayOfBaseFolderIdsType folderIds = new NonEmptyArrayOfBaseFolderIdsType();
		List<BaseFolderIdType> ids = folderIds.getFolderIdOrDistinguishedFolderId();
		ids.add(parentFolderId);
		finder.setParentFolderIds(folderIds);
		
		// define response Objects and their holders
		FindFolderResponseType findFolderResponse = new FindFolderResponseType();
		Holder<FindFolderResponseType> responseHolder = new Holder<FindFolderResponseType>(findFolderResponse);

		ServerVersionInfo serverVersion = new ServerVersionInfo();
		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);

		ExchangeServicePortType proxy = null;
		List<BaseFolderType> mailFolders = new ArrayList<BaseFolderType>();
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		User user = ExchangeConversion.getConv().getUser();
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_META);
			proxy.findFolder(finder, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
			                                    .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);
			                                

			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Get Child Folders Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Get Child Folders Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					FindFolderResponseMessageType findResponse = (FindFolderResponseMessageType)response;
					List<BaseFolderType> allFolders =  findResponse.getRootFolder().getFolders().getFolderOrCalendarFolderOrContactsFolder();
					for(BaseFolderType folder : allFolders){
//						if(folder.getFolderClass() == null
//						|| folder.getFolderClass().equals(EXCHANGE_MAIL_FOLDER_CLASS)){
//							logger.debug("Adding " + folder.getDisplayName() + " to list of Exchange Mail Folders.");
							mailFolders.add(folder);
//						} 
//						else {
//							logger.debug("Not including " + folder.getDisplayName() + " in list of Mail Folders.");
//						}
					}
				}
			}

		} catch (Exception e){
			throw new RuntimeException("Exception Performing getChildFolders", e);
		} finally{
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		}
		return mailFolders;
		
	}
	
	public static FolderType createRootFolder(User user, String folderName){
		DistinguishedFolderIdType root = new DistinguishedFolderIdType();
		root.setId(DistinguishedFolderIdNameType.MSGFOLDERROOT);
		return createFolder(user, folderName, root);
	}
		
	public static List<FolderType> createRootFolder(List<String> folderNames){
		DistinguishedFolderIdType root = new DistinguishedFolderIdType();
		root.setId(DistinguishedFolderIdNameType.MSGFOLDERROOT);
		return createFolders(folderNames, root);
	}

	public static FolderType createFolder(User user, String folderName, BaseFolderIdType parentFolderId){
		List<String> folderNames = new ArrayList<String>();
		folderNames.add(folderName);
		List<FolderType> folders = createFolders(folderNames, parentFolderId);
		if(folders.size() == 1){
			return folders.get(0);
		} else {
			return null;
		}
	}
	
	public static List<FolderType> createFolders(List<String> folderNames, BaseFolderIdType parentFolderId){

		List<FolderType> returnList = new ArrayList<FolderType>();
		CreateFolderType creator = new CreateFolderType();

		// Make the NonEmptyArrayOfFolders 
		NonEmptyArrayOfFoldersType folderArray = new NonEmptyArrayOfFoldersType();
		List<BaseFolderType> folders = folderArray.getFolderOrCalendarFolderOrContactsFolder();
		for(String folderName : folderNames){
			BaseFolderType folder = new FolderType();
			folder.setDisplayName(folderName);
			folder.setFolderClass(EXCHANGE_MAIL_FOLDER_CLASS);
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

		User user = ExchangeConversion.getConv().getUser();
		ExchangeServicePortType proxy = null;
		List<JAXBElement <? extends ResponseMessageType>> responses = null;
		try{
			Report.getReport().start(Report.EXCHANGE_CONNECT);
			proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
			Report.getReport().stop(Report.EXCHANGE_CONNECT);
			Report.getReport().start(Report.EXCHANGE_META);
			proxy.createFolder(creator, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
                       .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);
			
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
						returnList.add((FolderType)folder);
					}
				}
			}
		} catch (Exception e){
			throw new RuntimeException("Exception performing CreateFolder", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		}
		
		return returnList;
	}
	
	public static void deleteFolder(FolderIdType folderId){
		List<FolderIdType> ids = new ArrayList<FolderIdType>();
		ids.add(folderId);
		deleteFolders(ids);
	}
	
	public static void deleteFolders(List<FolderIdType> folderIds){
		
		DeleteFolderType deleter = new DeleteFolderType();
		
		NonEmptyArrayOfBaseFolderIdsType idsArray = new NonEmptyArrayOfBaseFolderIdsType();
		List<BaseFolderIdType> ids = idsArray.getFolderIdOrDistinguishedFolderId();
		for(FolderIdType id : folderIds){
			ids.add(id);
		}
		
		deleter.setFolderIds(idsArray);
		deleter.setDeleteType(DisposalType.HARD_DELETE);
		
		// define response Objects and their holders
		DeleteFolderResponseType deleteFolderResponse = new DeleteFolderResponseType();
		Holder<DeleteFolderResponseType> responseHolder = new Holder<DeleteFolderResponseType>(deleteFolderResponse);

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
			proxy.deleteFolder(deleter, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
                       .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);
			
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Delete Folder Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Delete Folder Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					// not much to do... don't need a handle to anything...
				}
			}
		} catch (Exception e){
			throw new RuntimeException("Exception Performing DeleteFolder", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		} 
	}
	
	/**
	 * This doesn't work.  EWS does not expose the status bit flag from getStatus which looks up the status
	 * on a user by user basis.  This was an attempt to set that flag via ExtendedProperties...  NO GOOD...
	 * 
	 * @param user
	 * @param folderType
	 * @param imapFolder
	 * @return
	 */
//	public static BaseFolderType updateFolderStatus(User user, BaseFolderType folderType, MergedImapFolder imapFolder){
//		List<BaseFolderType> folderTypes = new ArrayList<BaseFolderType>();
//		List<MergedImapFolder> imapFolders = new ArrayList<MergedImapFolder>();
//		folderTypes.add(folderType);
//		imapFolders.add(imapFolder);
//		if (updateFoldersStatus(user, folderTypes, imapFolders).isEmpty()) return null;
//		return updateFoldersStatus(user, folderTypes, imapFolders).get(0);
//	}
	
	/**
	 * This doesn't work.  EWS does not expose the status bit flag from getStatus which looks up the status
	 * on a user by user basis.  This was an attempt to set that flag via ExtendedProperties...  NO GOOD...
	 * 
	 * @param user
	 * @param folderTypes
	 * @param imapFolders
	 * @return
	 */
//	public static List<BaseFolderType> updateFoldersStatus(User user, List<BaseFolderType> folderTypes, List<MergedImapFolder> imapFolders){
//
//		UpdateFolderType updater = new UpdateFolderType();
//	    
//		NonEmptyArrayOfFolderChangesType changesNEA = new NonEmptyArrayOfFolderChangesType();
//		List<FolderChangeType> changes = changesNEA.getFolderChange();
//	    
//		int i = 0;
//		for(BaseFolderType folder : folderTypes){
//
//			// Create the container for the set of updates to be made to the folders.
//		    FolderChangeType change = new FolderChangeType();
//
//		    // Identify the folder to change.
//		    change.setFolderId(folder.getFolderId());
//	
//		    // Create the update. Identify the field to update and the value to set for it.
//		    SetFolderFieldType setter = new SetFolderFieldType();
//
//		    PrStatus prStatusFlags = new PrStatus();
//		    if(imapFolders.get(i).isSubscribed()){
//		    	prStatusFlags.remove(PrStatus.FLDSTATUS_HIDDEN);
//		    } else {
//		    	prStatusFlags.add(PrStatus.FLDSTATUS_HIDDEN);
//		    }
//		    logger.debug(imapFolders.get(i).getName() + ":" + prStatusFlags.toString());
//
//		    ExtendedPropertyType prStatus = new ExtendedPropertyType();
//		    prStatus.setExtendedFieldURI(PrStatus.PR_STATUS_URI);
//		    prStatus.setValue(prStatusFlags.toLongString());
//		    
//		    FolderType updatedFolder = new FolderType();
//		    List<ExtendedPropertyType> props = updatedFolder.getExtendedProperty();
//		    props.add(prStatus);
//		    setter.setPath(PrStatus.PR_STATUS_PATH); 
//		    setter.setFolder(updatedFolder);
//		    
//		    NonEmptyArrayOfFolderChangeDescriptionsType changeDescsNEA = new NonEmptyArrayOfFolderChangeDescriptionsType();
//		    List<FolderChangeDescriptionType> changeDescs = changeDescsNEA.getAppendToFolderFieldOrSetFolderFieldOrDeleteFolderField();
//		    changeDescs.add(setter);
//		    change.setUpdates(changeDescsNEA);
//		    
//		    // Add the array of changes; in this case, a single element array.
//		    changes.add(change);
//		    i++;
//		}
//		
//		updater.setFolderChanges(changesNEA);
//		
//		// define response Objects and their holders
//		UpdateFolderResponseType updateFolderResponse = new UpdateFolderResponseType();
//		Holder<UpdateFolderResponseType> responseHolder = new Holder<UpdateFolderResponseType>(updateFolderResponse);
//
//		ServerVersionInfo serverVersion = new ServerVersionInfo();
//		Holder<ServerVersionInfo> serverVersionHolder = new Holder<ServerVersionInfo>(serverVersion);
//
//		ExchangeServicePortType proxy = null;
//		List<JAXBElement <? extends ResponseMessageType>> responses = null;
//		try{
//      	proxy = ExchangeServerPortFactory.getInstance().getExchangeServerPort();
//			proxy.updateFolder(updater, user.getImpersonation() ,responseHolder, serverVersionHolder);
//			responses = responseHolder.value.getResponseMessages()
//                       .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
//
//			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
//				ResponseMessageType response = jaxResponse.getValue();
//				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
//					throw new RuntimeException("Update Folder Response Error: " + response.getMessageText());
//				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
//					logger.warn("Update Folder Response Warning: " + response.getMessageText());
//              	user.getConversion().warnings++;
//				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
//					FolderInfoResponseMessageType findResponse = (FolderInfoResponseMessageType)response;
//					List<BaseFolderType> updatedFolders =  findResponse.getFolders().getFolderOrCalendarFolderOrContactsFolder();
//					i = 0;
//					for(BaseFolderType folder : updatedFolders){
//						folderTypes.get(i++).setFolderId(folder.getFolderId());
//					}
//				}
//			}
//		} catch (Exception e){
//			throw new RuntimeException("Exception Performing UpdateFolderStatus", e);
//		} 
//	}

	public static List<BaseFolderType> moveFolders(List<FolderIdType> folderIds, TargetFolderIdType targetFolderId){
		
		List<BaseFolderType> returnList = new ArrayList<BaseFolderType>();

		MoveFolderType mover = new MoveFolderType();
		
		NonEmptyArrayOfBaseFolderIdsType idsArray = new NonEmptyArrayOfBaseFolderIdsType();
		List<BaseFolderIdType> ids = idsArray.getFolderIdOrDistinguishedFolderId();
		for(FolderIdType id : folderIds){
			ids.add(id);
		}
		
		mover.setFolderIds(idsArray);
		mover.setToFolderId(targetFolderId);
		
		// define response Objects and their holders
		MoveFolderResponseType moveFolderResponse = new MoveFolderResponseType();
		Holder<MoveFolderResponseType> responseHolder = new Holder<MoveFolderResponseType>(moveFolderResponse);

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
			proxy.moveFolder(mover, user.getImpersonation() ,responseHolder, serverVersionHolder);
			responses = responseHolder.value.getResponseMessages()
                       .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			Report.getReport().stop(Report.EXCHANGE_META);
			
			for(JAXBElement <? extends ResponseMessageType> jaxResponse : responses){
				ResponseMessageType response = jaxResponse.getValue();
				if(response.getResponseClass().equals(ResponseClassType.ERROR)){
					logger.warn("Move Folder Response Error: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.WARNING)){
					logger.warn("Move Folder Response Warning: " + response.getMessageText());
					user.getConversion().warnings++;
				} else if(response.getResponseClass().equals(ResponseClassType.SUCCESS)){
					FolderInfoResponseMessageType findResponse = (FolderInfoResponseMessageType)response;
					List<BaseFolderType> movedFolders =  findResponse.getFolders().getFolderOrCalendarFolderOrContactsFolder();
					for(BaseFolderType folder : movedFolders){
						returnList.add(folder);
					}
				}
			}
		} catch (Exception e){
			throw new RuntimeException("Exception Performing MoveFolder", e);
		} finally {
			if(Report.getReport().isStarted(Report.EXCHANGE_META))
				Report.getReport().stop(Report.EXCHANGE_META);
			if(Report.getReport().isStarted(Report.EXCHANGE_CONNECT))
				Report.getReport().stop(Report.EXCHANGE_CONNECT);
		}
		return returnList;
	}

	public static BaseFolderType getFolderTypeFromList(List<BaseFolderType> folders, String folderName){
		for(BaseFolderType folder : folders){
			if(folderName.equalsIgnoreCase(folder.getDisplayName())){
				return folder;
			}
		}
		return null;
	}

	public static IMAPFolder getFolderFromList(List<IMAPFolder> folders, String folderName){
		for(IMAPFolder folder : folders){
			if(folderName.equalsIgnoreCase(folder.getName())){
				return folder;
			}
		}
		return null;
	}
	
}
