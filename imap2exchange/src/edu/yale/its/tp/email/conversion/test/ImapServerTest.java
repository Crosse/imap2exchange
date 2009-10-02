package edu.yale.its.tp.email.conversion.test;

import java.io.*;
import javax.mail.*;
import javax.mail.search.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.support.FileSystemXmlApplicationContext;


import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.imap.*;
import edu.yale.its.tp.email.conversion.yale.*;

/**
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
 */
public class ImapServerTest {
	
	private static Log logger = LogFactory.getLog(ImapServerTest.class);
	
	static Store imapStore;
	
	public static void main(String[] args) throws Exception{
		
		wireSpring();
		
//		YaleBatchLoader loader = new YaleBatchLoader();
//		List<User> users = loader.getUsers();
		
//		for(User user : users){
		
		User user = new YaleUser();
		user.setUid("lmk4");
		user.setSourceImapPo("pantheon-po21.its.yale.edu");
//		user.setUid("jjv6");
//		user.setSourceImapPo("pantheon-po09.its.yale.edu");
			
			logger.debug("user: " + user.getUid() + "@" + user.getSourceImapPo());
		 
			imapStore = ImapServerFactory.getInstance().getImapStore(user);
			
			Folder root = imapStore.getDefaultFolder();
			Folder folder = root.getFolder("inbox");
//			Folder[] folders = root.list();
//			for(Folder folder : folders){
				try{
//					long uid = ((IMAPFolder)folder).getUIDValidity();
//					if(uid > 1){
						folder.open(Folder.READ_ONLY);
//						logger.debug(folder.getName() + "[" + uid + "]: " + folder.getMessageCount());
//						SearchTerm search = new SubjectTerm("Wacked users");
						SearchTerm search = new SubjectTerm("NERC Display Table");
						Message[] messages = folder.search(search);
//						Message[] messages = folder.getMessages();
//						for(int i=0; i<4; i++){
						for(Message msg : messages){

							logger.debug("=====================================");
							logger.debug("Type: " + msg.getClass().getName());
							logger.debug("=====================================");
							

//?							Message msg = messages[i];
//							if(msg.getReceivedDate().equals(msg.getSentDate())){
//								continue;
//							}
							logger.debug("===================================================");
							logger.debug("===================================================");
							logger.debug("Subject:       " + msg.getSubject());
							logger.debug("Recieved Date: " + msg.getReceivedDate());
							logger.debug("Sent Date:     " + msg.getSentDate());
							logger.debug("===================================================");
							logger.debug("       Trace log");
							logger.debug("===================================================");
//							String b64String = MessageUtil.getBase64MimeContent(msg);
							ByteArrayOutputStream baout = new ByteArrayOutputStream();
							baout.write("\n===================================================\n".getBytes());
							baout.write("                 Mime Content\n".getBytes());
							baout.write("===================================================\n".getBytes());
							msg.writeTo(baout);
							baout.flush();
							logger.debug(baout.toString());
							logger.debug("===================================================");
							logger.debug("===================================================");
						}
						System.exit(0);
//					} else {
//						throw new MessagingException();
//					}
				} catch (MessagingException me1){
					// not a real IMAP Folder...  
					logger.debug("Folder or File " + user.getUid() + "." + folder.getFullName() + " was not converted because it is not a mail folder.");
				}

//			}
//		}

	}
	
	public static void wireSpring(){
		FileSystemXmlApplicationContext springContext = new FileSystemXmlApplicationContext(new String[]{"/config/imap2exchange-config.xml"
        		                                                        ,"/config/imap2exchange-yale-config.xml"
        		                                                        ,"/config/imapservers.xml"});
	}
	

}
