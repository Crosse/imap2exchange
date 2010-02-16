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
package edu.jmu.email.conversion.jmu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.yale.its.tp.email.conversion.ExchangeConversion;
import edu.yale.its.tp.email.conversion.ExchangeConversionFactory;
import edu.yale.its.tp.email.conversion.Report;
import edu.yale.its.tp.email.conversion.User;
import edu.yale.its.tp.email.conversion.UserFactory;

/**
 * @author wrightst
 * 
 */
public class JmuContactsImport {
    private static final Log logger = LogFactory.getLog(JmuContactsImport.class);

    public static void main(String[] args) {
        wireSpring();
        
        if (args.length == 1) {
            User user = UserFactory.getInstance().createUser(args[0].trim(), "");

            ExchangeConversionFactory convFactory = ExchangeConversionFactory.getInstance();
            ExchangeConversion.setConv(convFactory.makeExchangeConversion(user));
            user.setConversion(ExchangeConversion.getConv());

            logger.info("pageSize = " + convFactory.getPageSize());
            
            user.getConversion().performUserSetupAction();
            user.getConversion().setUser(user);
            new Report();

            logger.debug(String.format("User: %s@%s", user.getUid(), user.getSourceImapPo()));
            user.getConversion().performPostConversionAction();
        }

    }

    public static void wireSpring() {
        @SuppressWarnings("unused")
        FileSystemXmlApplicationContext springContext = new FileSystemXmlApplicationContext(new String[] { "/config/imap2exchange-config.xml", "/config/imap2exchange-jmu-config.xml", "/config/imapservers.xml" });
    }
}
