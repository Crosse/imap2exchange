package edu.yale.its.tp.email.conversion.yale;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;

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
 * Batch Load Users.
 */ 
public class YaleBatchLoader {
	
	public static final Log logger = LogFactory.getLog(YaleBatchLoader.class);
	
	public String userFile;
	
	/**
	 * Get the users from the config defined user file.
	 * @return
	 */
	public List<User> getUsers(){
		
		List<User> users = new ArrayList<User>();
		
		try{
			// Get users from current run file
			logger.debug("userFile: " + userFile);
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(userFile)));
			
			String line = "";
			while(null != (line = fileReader.readLine())){
				
				line = line.trim();
				
				// don't process comments or empty lines...
				if(   line.startsWith("#")
				   || line.length() == 0) continue;

				logger.debug("line: " + line);
				// Create Users
				users.add(getUser(line.split("@")));

			}
			
		} catch (IOException e){
			logger.error("Error Reading Yale User File", e);
			System.exit(-1);
		}
		return users;
	}
	
	/**
	 * defines the format of each line...
	 * in our case:
	 *    uid, sourcePo
	 * @param values
	 * @return
	 */
	protected User getUser(String[] values){
		
		if(values.length != 2)
			throw new RuntimeException("Yale user file must the following format: uid@po");
		
		// Create Users
		return UserFactory.getInstance().createUser(values[0].trim(), values[1].trim());
	}

	public String getUserFile() {
		return userFile;
	}

	public void setUserFile(String userFile) {
		this.userFile = userFile;
	}
	
}
