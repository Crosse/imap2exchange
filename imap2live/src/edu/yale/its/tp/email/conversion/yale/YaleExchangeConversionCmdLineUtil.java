
package edu.yale.its.tp.email.conversion.yale;

import java.io.File;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.event.*;
import edu.yale.its.tp.email.conversion.imap.*;
import org.springframework.context.support.FileSystemXmlApplicationContext;
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
 * Yales Exchange Conversion Command Line Util.
 */
public class YaleExchangeConversionCmdLineUtil implements NoMoreConversionsListener
//                                             , ExchangeConversionStartListener
//                                             , ExchangeConversionCompleteListener 
                                             {
	
	public static final Log logger = LogFactory.getLog(YaleExchangeConversionCmdLineUtil.class);
	
	public static final String CONVERSION_HOME = "EXCHANGE_CONVERSION_HOME";
	public static final String runDir = new File(System.getenv(CONVERSION_HOME)).getAbsolutePath();

	boolean isCleaner = false;
	boolean isFinal = false;
	List<User> users = new ArrayList<User>();
	
	FileSystemXmlApplicationContext springContext;
	
	public static final String CLEAN_FLAG = "-clean";
	public static final String BATCH_FLAG = "-batch";
	public static final String FINAL_FLAG = "-final";
	
	FolderAltNames altNames;
	
	
	/**
	 * Start things up
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		YaleExchangeConversionCmdLineUtil yc = new YaleExchangeConversionCmdLineUtil(args);
		yc.startConversion();
	}
	
	/** 
	 * I want only one of these...
	 * @param args
	 */
	private YaleExchangeConversionCmdLineUtil(String[] args){
		wireSpring();
		processArgs(args);
	}

	public void wireSpring(){
        springContext = new FileSystemXmlApplicationContext(new String[]{"/config/imap2exchange-config.xml"
        		                                                        ,"/config/imap2exchange-yale-config.xml"
        		                                                        ,"/config/imapservers.xml"});
	}
	
	/**
	 * Starts the ConversionManager and add the users defined at the command line
	 * or ones that werwe batch loaded via config file defined user file.
	 *
	 */
	public void startConversion(){
		
		ExchangeConversionManager convManager = ExchangeConversionManager.getInstance();
		convManager.addNoMoreConversionsListener(this);
//		convManager.addExchangeConversionStartListener(this);
//		convManager.addExchangeConversionCompleteListener(this);
		
		ExchangeConversionFactory convFactory = ExchangeConversionFactory.getInstance();
		convFactory.setCleaner(isCleaner);
		for(User user : getUsers()){
			ExchangeConversion conv = convFactory.makeExchangeConversion(user);
			conv.setFinalRun(isFinal);
			convManager.addConversion(conv);
		}
		
	}
	
	/**
	 * Process the arguments and let the user know if I like them.
	 * @param argsAndFlags
	 */
	void processArgs(String[] argsAndFlags){
		
		List<String> flags = new ArrayList<String>();
		List<String> args =  new ArrayList<String>();

		for(String s : argsAndFlags){
			if(s.startsWith("-")){
				flags.add(s);
			} else {
				args.add(s);
			}
		}
		
		for(String flag : flags){
			if(!(   flag.equals(CLEAN_FLAG)
			     || flag.equals(BATCH_FLAG)
			     || flag.equals(FINAL_FLAG)) ){
				System.err.println("Invalid flag: " + flag);
				printUsage();
			}
		}
		
		if(flags.contains(CLEAN_FLAG)){
			this.isCleaner = true;
		}
		
		if(flags.contains(FINAL_FLAG)){
			this.isFinal = true;
		}

		if(flags.contains(BATCH_FLAG)){
			try{
				batchLoadUsers();
			} catch (RuntimeException e){
				e.printStackTrace();
				printUsage();
			}
		} else {

			String uid = null;
			String po = null; 
			switch (args.size()){
			case 1:
				if(args.size() == 1 && flags.contains(CLEAN_FLAG)){
					uid= args.get(0);
					break;
				} else {
					printUsage();
					break;
				}
			case 2:
				uid = args.get(0);
				po = args.get(1);
				break;
			default:
				printUsage();
				break;
			
			}
			logger.debug("Adding User: " + uid + "@" + po);
			addUser(UserFactory.getInstance().createUser(uid.trim(), po.trim()));

		}
		
	}
	
	/**
	 * Echo usage to Standard Error
	 *
	 */
	static void printUsage(){
		System.err.println("Usage: [" + CLEAN_FLAG + "] [" + BATCH_FLAG + "] [" + FINAL_FLAG + "] username legacyPo");
		System.err.println("       -batch - uses batch file defined in config/config.properties");
		System.err.println("       -clean - for development use only, this will HARD DELETE all mail and Non-System Folders.");
		System.err.println("       -final - for YALE use only, this is used by the YalePostConversionActionACSUpdater class to determine if ACS should be updated.");
		System.exit(0);
	}
	
	/**
	 * batch Load users via the batchLoader
	 *
	 */
	public void batchLoadUsers(){
		YaleBatchLoader loader = (YaleBatchLoader)springContext.getBean("yaleBatchLoader");
		logger.debug("userFile: " + loader.getUserFile());
		this.addUsers(loader.getUsers());
	}

	/**
	 * NoMoreConvesionListener Impl.
	 */
	public void noMoreConversions() {
		logger.info("Caught \"NoMoreConversions\" event, exiting YaleExchangeConversion.");
		System.exit(0);		
	}
	
	/**
	 * ExchangeConversionStartedListener Impl.
	 */
//	public void ExchangeConversionStarted(ExchangeConversionEvent event) {
//		logger.info("Conversion started for " + event.getExchangeConversion().getId());
//	}

	/**
	 * ExchangeConversionCompletedListener Impl.
	 */
//	public void ExchangeConversionCompleted(ExchangeConversionEvent event) {
//		logger.info("Conversion completed for " + event.getExchangeConversion().getId());
//	}

	public List<User> getUsers(){
		return this.users;
	}
	
	public void addUser(User user){
		this.users.add(user);
	}
	
	public void addUsers(List<User> users){
		this.users.addAll(users);
	}

	public FolderAltNames getAltNames() {
		return altNames;
	}

	public void setAltNames(FolderAltNames altNames) {
		this.altNames = altNames;
	}
	
}
