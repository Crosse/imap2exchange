package edu.yale.its.tp.email.conversion.yale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yale.its.tp.email.conversion.*;
import edu.yale.its.tp.email.conversion.imap.*;

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
public class YalePreConversionAction extends PluggableConversionAction {

	private static final Log logger = LogFactory.getLog(YalePreConversionAction.class);
	
	long maxMailboxSize = -1;
	
	@Override
	public boolean perform(ExchangeConversion conv) {
		return validMailboxSize(conv.getUser());
	}

	public boolean validMailboxSize(User user){
		
		if (maxMailboxSize == -1){
			logger.debug("Not Checking maxMailboxSize");
			return true;
		}
		
		ImapServer imapServer = ImapServerFactory.getInstance().getImapServer(user.getSourceImapPo());
		long size = imapServer.getSizer().getMailboxSize(user);

		if(size == -1){
			logger.error("Error Validating Mailbox Size. Sizer returned -1;");
			return false;
		} else if (size < maxMailboxSize) {
			logger.info("Mailbox size OK: actual[" + size + "] < max[" + maxMailboxSize + "]");
			return true;
		} else {
			logger.error("Mailbox size BAD: actual[" + size + "] > max[" + maxMailboxSize + "]");
			return false;
		}
	}

	public long getMaxMailboxSize() {
		return maxMailboxSize;
	}

	public void setMaxMailboxSize(long maxMailboxSize) {
		this.maxMailboxSize = maxMailboxSize;
	}

}
