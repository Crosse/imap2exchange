package edu.yale.its.tp.email.conversion.imap;

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
 * Repesents an IMAP Server
 */
public class ImapServer implements Comparable<ImapServer>{

	public static final String IMAP_PROTOCOL = "imap";
	public static final String IMAPS_PROTOCOL = "imaps";
	
	String uri;
	String adminUid;
	String adminPwd;
	String port;
	String protocol = IMAPS_PROTOCOL;  // default
	ImapMailboxSizer sizer;
	String group;
	
	boolean sasl = false;
	
	public String getAdminPwd() {
		return adminPwd;
	}
	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}
	public String getAdminUid() {
		return adminUid;
	}
	public void setAdminUid(String adminUid) {
		this.adminUid = adminUid;
	}
	public String getPort() {
		return this.port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getProtocol() {
		return protocol;
	}
	/**
	 * imap or imaps
	 * @param protocol
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public boolean isSasl() {
		return sasl;
	}
	public void setSasl(boolean sasl) {
		this.sasl = sasl;
	}
	public ImapMailboxSizer getSizer() {
		return sizer;
	}
	public void setSizer(ImapMailboxSizer sizer) {
		this.sizer = sizer;
	}
	public int compareTo(ImapServer server) {
		return this.getUri().compareTo(server.getUri());
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
}
