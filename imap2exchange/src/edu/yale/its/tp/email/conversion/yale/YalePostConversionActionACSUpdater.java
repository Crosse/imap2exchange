package edu.yale.its.tp.email.conversion.yale;

import java.sql.*;
import javax.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
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
 */
public class YalePostConversionActionACSUpdater extends PluggableConversionAction {

	private static Log logger = LogFactory.getLog(YalePostConversionActionACSUpdater.class);
	
	static final String CONNECT_SUFFIX =  "@connect.yale.edu";
	static final String YALE_SUFFIX =  ".yale.edu";
	
	// Update SQL
	static final String repointSql = "{?=call smart.email_api.repoint_mailbox(?, ?)}"; 
	DataSource datasource;

	@Override
	public boolean perform(ExchangeConversion conv) {

		// Don't do anything if this is the not the final run.
		if(!conv.isFinalRun()){
			logger.info("Non-final conversion, Not updating ACS.");
			return true;
		}
		
		User user = conv.getUser();
		ImapServer imapServer = ImapServerFactory.getInstance().getImapServer(user.getSourceImapPo());
		if(imapServer == null){
			logger.error("Error reteieving imap server properties for " + conv.getId());
			return false;
		}

		String connectMailbox = user.getUid() + CONNECT_SUFFIX;
		String sourceMailbox = user.getUid() + "@" + imapServer.getGroup() + YALE_SUFFIX;
		return repointMailbox(sourceMailbox, connectMailbox);
	}
	
	protected boolean repointMailbox(String sourcePo, String destPo){
		boolean success = false;
		Connection conn = null;
		CallableStatement cs = null;
		try{

			logger.info("Updating dir_online_info: [" + sourcePo + "] to [" + destPo + "]");
			conn = datasource.getConnection();
			cs = conn.prepareCall(repointSql);
			cs.registerOutParameter(1, Types.INTEGER);
			cs.setString(2, sourcePo);
			cs.setString(3, destPo);
			cs.executeQuery();
			int updatedRowCnt = cs.getInt(1);
			if (updatedRowCnt < 0) updatedRowCnt = 0;
			logger.debug("Updated " + updatedRowCnt + " records in ACS.");
			success = true;

		} catch (Exception e){
			logger.error("Error updating Mailbox in ACS", e);
		} finally {
			try{cs.close();} catch(Exception e){}
			try{conn.close();} catch(Exception e){}
		}
		return success;
	}
	
	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

}
