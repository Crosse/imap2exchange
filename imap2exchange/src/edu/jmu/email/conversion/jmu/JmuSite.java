package edu.jmu.email.conversion.jmu;

//import org.apache.log4j.Logger;

/**
 * <pre>
 * $URL$
 * $Author$
 * $Date$
 * $Rev$
 * 
 * Copyright (c) 2009 Seth Wright (wrightst@jmu.edu)
 *
 * Permission to use, copy, modify, and distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * </pre>
 *
 */
public class JmuSite {

//    private static Logger logger = Logger.getLogger(JmuSite.class);

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
