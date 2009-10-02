package edu.yale.its.tp.email.conversion.yale;

import com.microsoft.schemas.exchange.services._2006.types.*;
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
 */
public class YaleUser implements User{

	String uid;
	String upn;
	String email;
	String sourceImapPo;
	ExchangeImpersonationType imp;
	ExchangeConversion conv;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void setUPN(String upn){
		this.upn = upn;
	}
	
	public void setPrimarySMTPAddress(String email){
		this.email = email;
	}
	
	public String getUPN(){
		return upn;
	}
	
	public String getPrimarySMTPAddress(){
		return email;
	}
	
	public String getSourceImapPo() {
		return sourceImapPo;
	}

	public void setSourceImapPo(String po) {
		this.sourceImapPo = po;
	}

	public ExchangeImpersonationType getImpersonation(){
		if(imp == null){
			
			ConnectingSIDType sid = new ConnectingSIDType();
			sid.setPrincipalName(this.getUPN());
			sid.setPrimarySmtpAddress(this.getPrimarySMTPAddress());

			imp = new ExchangeImpersonationType();
			imp.setConnectingSID(sid);
		}
		return imp;
	}

	public ExchangeConversion getConversion() {
		return conv;
	}

	public void setConversion(ExchangeConversion conv) {
		this.conv = conv;		
	}
	
	
	
}
