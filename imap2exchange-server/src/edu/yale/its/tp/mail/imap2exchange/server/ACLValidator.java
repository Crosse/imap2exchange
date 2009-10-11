package edu.yale.its.tp.mail.imap2exchange.server;

import java.sql.*;
import javax.sql.*;
import org.apache.log4j.*;

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
public class ACLValidator {
	
	public static final Logger logger = Logger.getLogger(ACLValidator.class);
	
	static ACLValidator instance;
	DataSource datasource;
	
	public static String sql = "SELECT netid FROM yubulk.managead_acl where netid = ?";
	
	public ACLValidator(){
		instance = this;
	}
	
	public static ACLValidator getInstance(){
		return instance;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public boolean isValid(String netid){
		
		logger.debug("Validating User: [" + netid + "]");
		
		boolean valid = false;
		
		if(netid == null
		|| netid.trim().equals("")) return false;			
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			conn = datasource.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, netid);
			rs = ps.executeQuery();
			valid = rs.next();
		} catch (SQLException sqle){
			logger.error("Error Validating netid [" + netid + "]", sqle);
		} finally {
			try{rs.close();}catch(Exception e){};
			try{ps.close();}catch(Exception e){};
			try{conn.close();}catch(Exception e){};
		}
		return valid;
	}
	

}
