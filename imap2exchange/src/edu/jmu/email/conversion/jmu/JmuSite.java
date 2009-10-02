package edu.jmu.email.conversion.jmu;

import org.apache.log4j.Logger;

/**
 * 
 * <pre>
 * Copyright (c) 2000-2003 James Madison University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL JAMES MADISON UNIVERSITY OR ITS EMPLOYEES BE
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
 * includes software developed by James Madison University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "JMU" and "James Madison University" must not be used to endorse
 * or promote products derived from this software.
 * </pre>
 *

 *
 */
public class JmuSite {

    private static Logger logger = Logger.getLogger(JmuSite.class);

    private static JmuSite instance;

    private String mailDomain;

    public JmuSite(){
        instance = this;
    }

    static public JmuSite getInstance(){
        return instance;
    }

    public String getMailDomain() {
        return mailDomain;
    }
    
    public void setMailDomain(String mailDomain) {
        this.mailDomain = mailDomain;
    }
}
