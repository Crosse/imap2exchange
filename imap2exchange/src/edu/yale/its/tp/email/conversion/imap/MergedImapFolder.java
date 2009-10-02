package edu.yale.its.tp.email.conversion.imap;

import javax.mail.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.mail.imap.*;
import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.exchange.*;
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
 * Used to abstract merged Folders in Exchange, since it is not case-sensitive 
 */
public class MergedImapFolder {


    private static final Log logger = LogFactory.getLog(MergedImapFolder.class);

    String name;
    boolean subscribed;
    ExchangeConversion conv;
    List<Message> messages = new ArrayList<Message>();
    List<Message> deletedMessages = new ArrayList<Message>();
    List<Folder> folders = new ArrayList<Folder>();
    List<MergedImapFolder> mergedChildFolders;
    List<String> folderUids = new ArrayList<String>();
    List<String> excludedImapFolders;
    List<String> includedImapFolders;
    MergedImapFolder parent;

    static final FetchProfile IMAP_FETCH_PROFILE = new FetchProfile();
    static{
        IMAP_FETCH_PROFILE.add(FetchProfile.Item.FLAGS);
        IMAP_FETCH_PROFILE.add(FetchProfile.Item.CONTENT_INFO);
        IMAP_FETCH_PROFILE.add(IMAPFolder.FetchProfileItem.HEADERS);
        IMAP_FETCH_PROFILE.add(UIDFolder.FetchProfileItem.UID);
    }

    /**
     * Get all messages in folder group not flagged deleted
     * @return
     */
    public List<Message> getMessages() {
        return messages;
    }
    /**
     * Get all messages in folder group flagged deleted
     * @return
     */
    public List<Message> getDeletedMessages() {
        return deletedMessages;
    }
    /**
     * Returns the folderName of the Exchange Created Folder and a 
     * list of Folders that are sources of the messages.
     * @return
     */
    public String getFolderNames() {
        if(folders.size() > 1){
            StringBuilder sb = new StringBuilder();
            sb.append(name).append("[");
            for(Folder folder : folders){
                sb.append(folder.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("]");
            return sb.toString();
        } else {
            return name;
        }
    }

    /**
     * Returns the Name of the folder that will be created in Exchange
     * @return
     */
    public String getName(){
        return name;
    }

    /** 
     * Sets the name... Be carefull here...
     * @param name
     */
    protected void setName(String name){
        this.name = name;
    }

    /**
     * Maybe some day I can use this in Exchange
     * @return
     */
    public boolean isSubscribed() {
        return subscribed;
    }
    /**
     * Maybe some day I can use this in Exchange
     * @return
     */
    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
    /**
     * returns the list of folders
     * @return
     */
    public List<Folder> getFolders() {
        return folders;
    }
    /**
     * Add a folder to the group
     * @param folder
     * @throws MessagingException
     */
    public void addFolder(Folder folder) throws MessagingException{
        this.folders.add(folder);
        logger.info("Indexing Messages in " + folder.getFullName() + " for Merged Folder: " + this.getName());
        folderUids.add(MessageUtil.getFolderUid(conv.getUser(), folder));
        // Root Folder can not have messages and is never "OPEN"
        if(folder.isOpen()){
            Message[] messageArray = folder.getMessages();
            conv.getReport().start(Report.IMAP_META);
            folder.fetch(messageArray, IMAP_FETCH_PROFILE);
            conv.getReport().stop(Report.IMAP_META);
            for(Message message : messageArray){
                if(ImapUtil.isDeleted(message)){
                    deletedMessages.add(message);
                } else {
                    messages.add(message);
                }
            }
        }
    }
    /**
     * Handle to the convesion object
     * @return
     */
    public ExchangeConversion getConv() {
        return conv;
    }
    public void setConv(ExchangeConversion conv) {
        this.conv = conv;
    }

    /**
     * returns the cumulative list of child folders for all folder in the group...
     * @return
     * @throws MessagingException
     */
    public List<MergedImapFolder> getChildFolders() throws MessagingException{
        if (mergedChildFolders == null){
            mergedChildFolders = new ArrayList<MergedImapFolder>();
            List<Folder> childFolders = new ArrayList<Folder>();
            for(Folder folder : folders){
                conv.getReport().start(Report.IMAP_META);
                childFolders.addAll(getImapFolders(conv.getUser(), folder));
                conv.getReport().stop(Report.IMAP_META);
            }
            mergedChildFolders.addAll(mergeFolders(conv.getUser(), childFolders).values());
        }
        return mergedChildFolders; 
    }

    /**
     * Javamail/UW will return all files in the user home directory
     * This is an easy way to filter out the non-complient folders
     */ 
    private List<Folder> getImapFolders(User user, Folder parentFolder) throws MessagingException{

        if(parentFolder != null && parentFolder.getType() == Folder.HOLDS_MESSAGES && !parentFolder.isOpen()){
            parentFolder.open(Folder.READ_ONLY);
        }

        Folder[] allFolders = parentFolder.list();
        List<Folder> childFolders = new ArrayList<Folder>();
        for(Folder folder : allFolders){
            Folder imapFolder = folder;
            logger.debug("Trying imapFolder : " + folder.getFullName());
            if(!isIncluded(folder) || isExcluded(folder)){
                continue;
            }
            try{
                if(folder.list().length > 0){
                    childFolders.add(folder);
                    try{
                        imapFolder.open(Folder.READ_ONLY);
                    } catch (Exception e){
                        /*
                         *  Ignore this...
                         *  I have to try to open it, if the folder 
                         *  happens to contain messages too, such
                         *  as the case of cyrus, unlike UW.
                         */
                    }
                } else { 
                    long uid = ((IMAPFolder)imapFolder).getUIDValidity();
                    if(uid > 1){
                        imapFolder.open(Folder.READ_ONLY);
                        childFolders.add(imapFolder);
                    } else {
                        logger.debug("invalid folder uidvalidity: " + uid);
                        throw new MessagingException();
                    }
                }
            } catch (MessagingException me1){
                // not a real IMAP Folder...  
                logger.debug("Folder or File " + user.getUid() + "." + folder.getFullName() + " was not converted because it is not a mail folder." + me1.getMessage());
            }
        }
        return childFolders;
    }

    protected boolean isExcluded(Folder folder){
        if(this.getExcludedImapFolders() == null) return false;
        for(String exclude : this.getExcludedImapFolders()){
            if(exclude.equalsIgnoreCase(folder.getFullName())){
                logger.info("Excluding " + folder.getFullName() + " because it is in the exclusion list.");
                return true;
            }
        }
        return false;
    }

    protected boolean isIncluded(Folder folder){
        if(this.getIncludedImapFolders() == null) return true;
        for(String include : this.getIncludedImapFolders()){
            if(include.equalsIgnoreCase(folder.getFullName())){
                logger.info("Including " + folder.getFullName() + " because it is in the inclusion list.");
                return true;
            }
        }
        logger.info("Excluding " + folder.getFullName() + " because it is not in the inclusion list.");
        return false;
    }
    /**
     * This merges folders of the same name and if any folder of the same name
     * with the same parent is subscribed, the exchange folder will be subscribed.
     */
    private Map<String, MergedImapFolder> mergeFolders(User user, List<Folder> imapFolders) throws MessagingException{
        Map<String, MergedImapFolder> mergedFolders = new HashMap<String, MergedImapFolder>();
        for(Folder folder : imapFolders){
            // Special Case for Alt Folder Names
            if(folder.getParent().getFullName().equals(conv.getImapRootFolder().getFullName())
                    && conv.getAltNames() != null){
                FolderAltName altName = conv.getAltNames().getFolderAltName(folder.getName()); 
                if(altName != null){
                    logger.warn(String.format("Merging folder %s with Exchange folder %s", 
                                folder.getFullName(), altName.getExchangeFolderName()));
                    mergeFolder(mergedFolders, altName.getExchangeFolderName(), folder);
                    continue;
                }
                    }
            mergeFolder(mergedFolders, folder.getName().trim(), folder);
        }
        return mergedFolders;
    }

    /**
     * I create new MergedImapFolders here to control what is merged with what...
     * @param mergedFolders
     * @param mergedFolderName
     * @param folder
     * @throws MessagingException
     */
    private void mergeFolder(Map<String, MergedImapFolder> mergedFolders, String mergedFolderName, Folder folder) throws MessagingException{
        MergedImapFolder mergedFolder = mergedFolders.get(mergedFolderName.toLowerCase());
        if(mergedFolder == null){
            mergedFolder = new MergedImapFolder();
            mergedFolder.setConv(conv);
            mergedFolder.setName(mergedFolderName);
            mergedFolder.addFolder(folder);
            mergedFolder.setSubscribed(folder.isSubscribed());
            mergedFolder.setParent(this);
            mergedFolder.setExcludedImapFolders(this.getExcludedImapFolders());
            mergedFolders.put(mergedFolderName.toLowerCase(), mergedFolder);
        } else {
            mergedFolder.setSubscribed(mergedFolder.isSubscribed() || folder.isSubscribed());
            mergedFolder.addFolder(folder);
        }
    }

    /**
     * determines if this MessageType is from this folder
     * @param message
     * @return
     */
    public boolean originatedFromThisFolder(MessageType message){
        for(String uid : folderUids){
            String messageUid = MessageUtil.getMessageTypeUID(message); 
            if(messageUid == null) continue;
            if(messageUid.startsWith(uid)){
                return true;
            }
        }
        return false;
    }

    /**
     * Filters out messageTypes only from this folder
     * @param messages
     * @return
     */
    public List<MessageType> getMessageTypesFromThisFolder(List<MessageType> messages){
        List<MessageType> returnMessages = new ArrayList<MessageType>();
        returnMessages.addAll(messages);
        for(MessageType message : messages){
            if(!originatedFromThisFolder(message)){
                returnMessages.remove(message);
            }
        }
        return returnMessages;
    }

    public MergedImapFolder getParent() {
        return parent;
    }
    public void setParent(MergedImapFolder parent) {
        this.parent = parent;
    }

    public void close(boolean expunge) {
        for(Folder folder : folders){
            try{
                if(folder.isOpen()){
                    folder.close(expunge);
                }
            } catch (MessagingException me){
                logger.debug("Error closing imap folder: " + folder.getFullName() + " - " + me.getMessage());
            }
        }
    }
    public List<String> getExcludedImapFolders() {
        return excludedImapFolders;
    }
    public void setExcludedImapFolders(List<String> excludedImapFolders) {
        this.excludedImapFolders = excludedImapFolders;
    }
    public List<String> getIncludedImapFolders() {
        return includedImapFolders;
    }
    public void setIncludedImapFolders(List<String> includedImapFolders) {
        this.includedImapFolders = includedImapFolders;
    }	
}
