package edu.yale.its.tp.email.conversion.exchange;

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
public enum ExchangeSystemFolders {
	
	

	INBOX("Inbox"),
	DELETED_ITEMS("Deleted Items"),
	DRAFTS("Drafts"),
	JUNK("Junk E-Mail"),
	OUTBOX("Outbox"),
	SENT_ITEMS("Sent Items"),
	SYNC_ISSUES("Sync Issues"),
	JOURNAL("Journal", false),
	NOTES("Notes", false),
	CALENDAR("Calendar", false),
	CONTACTS("Contacts", false),
	TASKS("Tasks", false);
	
	
	private final String name;
	private boolean mailFolder = true;
	
	ExchangeSystemFolders(String name){
		this(name, true);
	}

	ExchangeSystemFolders(String name, boolean mailFolder){
		this.name = name;		
		this.mailFolder = mailFolder;
	}

	public String getFolderName() {
		return name;
	}
	
    public boolean isMailFolder() {
		return mailFolder;
	}

	public static ExchangeSystemFolders fromName(String n) {
        for (ExchangeSystemFolders f : ExchangeSystemFolders.values()) {
            if (f.name.equalsIgnoreCase(n)) {
                return f;
            }
        }
        return null;
    }
    
    public static boolean isExchangeSystemFolders(String n){
    	if(fromName(n) != null)	return true;
    	else return false;
    }

}
