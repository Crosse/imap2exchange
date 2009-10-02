package edu.yale.its.tp.email.conversion.imap;

import java.util.*;
import javax.mail.*;
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
 * I would pool this object, but that will do no good...
 * A new connection is needed every time...
 */
public abstract class ImapServerFactory {

	static Log logger = LogFactory.getLog(ImapServerFactory.class);
	
	public static final String UID = "uid";
	public static final String PWD = "pwd";
	public static final String PORT = "port";
	public static final String PROTOCOL = "protocol";
	public static final String SASL = "SASL";

	Map<String, ImapServer> servers = new HashMap<String, ImapServer>();
	
	static ImapServerFactory singleton;

	protected int imapTimeout = 1000*60*4;  // 4 Minutes Default
	protected int imapConnectionTimeout = 1000*60*4; // 4 Minutes Default
	protected int connectionPoolSize = 100; // 10 is the deafault
	
	
	public ImapServerFactory () {
		singleton = this;
	}
	
	public static ImapServerFactory getInstance(){
		return singleton;
	}
	
	/** 
	 * Get me an IMAP Store
	 * @param user
	 * @return
	 */
	public abstract Store getImapStore(final User user);
	
	public void setImapServers(List<ImapServer> servers){
		this.servers = new HashMap<String, ImapServer>();
		for(ImapServer server : servers){
			this.servers.put(server.getUri(), server);
		}
	}
	
	public List<ImapServer> getImapServers() {
		return new ArrayList<ImapServer>(servers.values());
	}

	public ImapServer getImapServer(String uri){
		return servers.get(uri);
	}

	public boolean contains(String uri){
		return servers.containsKey(uri);
	}

	public int getImapConnectionTimeout() {
		return imapConnectionTimeout;
	}

	public void setImapConnectionTimeout(int imapConnectionTimeout) {
		this.imapConnectionTimeout = imapConnectionTimeout;
	}

	public int getImapTimeout() {
		return imapTimeout;
	}

	public void setImapTimeout(int imapTimeout) {
		this.imapTimeout = imapTimeout;
	}

	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}
	
	
}
