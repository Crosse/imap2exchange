package edu.yale.its.tp.email.conversion.imap;

import javax.mail.*;

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
 * Some Imap Utilities
 */
public class ImapUtil {

	public static boolean isDeleted(Message m) throws MessagingException{
		return m.getFlags().contains(Flags.Flag.DELETED); 
	}

	/**
	 * Prints the flags for an Imap message
	 * @param message
	 * @return
	 * @throws MessagingException
	 */
	public static String printFlags(Message message) throws MessagingException{
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Flags.Flag flag : message.getFlags().getSystemFlags()){
			if(flag.equals(Flags.Flag.ANSWERED))
				sb.append("\\Answered");
			if(flag.equals(Flags.Flag.DELETED))
				sb.append("\\Deleted");
			if(flag.equals(Flags.Flag.DRAFT))
				sb.append("\\Draft");
			if(flag.equals(Flags.Flag.FLAGGED))
				sb.append("\\Flagged");
			if(flag.equals(Flags.Flag.RECENT))
				sb.append("\\Recent");
			if(flag.equals(Flags.Flag.SEEN))
				sb.append("\\Seen");
			sb.append(", ");
		}
		for(String flag : message.getFlags().getUserFlags()){
			sb.append(flag.toString()).append(", ");
		}
		if(sb.length() > 1)
			sb.delete(sb.length()-2, sb.length());
		sb.append("]");		
		return sb.toString();
	}
	
}
