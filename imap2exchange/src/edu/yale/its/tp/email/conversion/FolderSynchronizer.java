
package edu.yale.its.tp.email.conversion;

import java.io.*;
import java.util.*;
import javax.mail.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.schemas.exchange.services._2006.types.*;

import edu.yale.its.tp.email.conversion.exchange.*;
import edu.yale.its.tp.email.conversion.imap.*;

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
 * Synchronizes Folders and Child Folders
 */
public class FolderSynchronizer {
	
	public static final Log logger = LogFactory.getLog(FolderSynchronizer.class);
	ExchangeConversion conv;

	/**
	 * Merges the Folders contents
	 * @param imapFolders
	 * @param exchangeFolders
	 * @param parentFolderId
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void syncFolders(Map<String, MergedImapFolder> imapFolders
                           , Map<String, BaseFolderType> exchangeFolders) throws MessagingException, IOException{

		/* 
		* Create Folders that don't exist on the dest host
		* Then merge the messages for all folders...
		*/
		for(String imapFolderName : imapFolders.keySet()){
			
			MergedImapFolder imapFolder = imapFolders.get(imapFolderName.toLowerCase());
		
			logger.info("Starting Conversion of " + imapFolder.getFolderNames());

			ExchangeSystemFolders esf = ExchangeSystemFolders.fromName(imapFolder.getFullName());
			if(esf != null && !esf.isMailFolder()){
				logger.warn("Not Syncronizing Messages in a Non-Mail Exchange System folder: " + esf.getFolderName());
				conv.warnings++;
				continue;
			}

			BaseFolderType exchangeFolder = exchangeFolders.get(imapFolder.getFullName().toLowerCase()); 

			if(exchangeFolder == null){

				MergedImapFolder parentImapFolder = imapFolder.getParent();
				FolderIdType exchangeParentFolderId = null;
				if(parentImapFolder != null){
					exchangeParentFolderId = exchangeFolders.get(parentImapFolder.getFullName().toLowerCase()).getFolderId();
				}else
					exchangeParentFolderId = exchangeFolders.get(ExchangeSystemFolders.INBOX.getFolderName().toLowerCase()).getParentFolderId();
					
				
				logger.info("Exchange Folder [" + imapFolder.getName() + "] does not exist, creating it");
				exchangeFolder = FolderUtil.createFolder(conv.getUser(), imapFolder.getName(), exchangeParentFolderId);
				
				// I need to add the props back to the returned folder since it only has the id in it 
				exchangeFolder.setDisplayName(imapFolder.getName());
				exchangeFolder.setParentFolderId(exchangeParentFolderId);
				exchangeFolders.put(imapFolder.getFullName().toLowerCase(), exchangeFolder);
				
			}
			
			syncMessages(imapFolder, exchangeFolder);
			
		}
	
	}

	/**
	 * Merges the messages for the two folders
	 * @param imapFolder
	 * @param exchangeFolder
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void syncMessages(MergedImapFolder imapFolder, BaseFolderType exchangeFolder) throws MessagingException, IOException{
		MessageSynchronizer syncer = new MessageSynchronizer();
		syncer.setUser(conv.getUser());
		syncer.setImapFolder(imapFolder);
		syncer.setExchangeFolder(exchangeFolder);
		syncer.setExchangeDeletedMessages(conv.getExchangeDeletedItems());
		syncer.setMaxMessageSize(conv.getMaxMessageSize());
		syncer.setMaxMessageGrpSize(conv.getMaxMessageGrpSize());
		syncer.sync();
	}

	/**
	 * handle to the conversion
	 * @return
	 */
	public ExchangeConversion getConv() {
		return conv;
	}

	/**
	 * handle to the conversion
	 * @return
	 */
	public void setConv(ExchangeConversion conv) {
		this.conv = conv;
	}
	
}
