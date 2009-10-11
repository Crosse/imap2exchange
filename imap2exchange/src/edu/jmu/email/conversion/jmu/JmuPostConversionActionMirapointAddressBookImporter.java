package edu.jmu.email.conversion.jmu;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPLocalException;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPSearchResult;
import com.novell.ldap.util.LDIFReader;
import com.microsoft.schemas.exchange.services._2006.types.*;

import edu.yale.its.tp.email.conversion.*;
import edu.jmu.email.conversion.exchange.ContactsFolderUtil;

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
public class JmuPostConversionActionMirapointAddressBookImporter extends PluggableConversionAction {

//    private static Log logger = LogFactory.getLog(JmuPostConversionActionMirapointAddressBookImporter.class);
    private static final String importedContactsFolderName = "Imported Contacts";
    private static final int version = 1;

    @Override
        public boolean perform (ExchangeConversion conv) {
            User user = conv.getUser();

            ContactsFolderType contactsFolder = null;

            if (ContactsFolderUtil.folderExists(user, importedContactsFolderName)) {
                // TODO:  Handle the condition where the import folder already
                // exists.
            } else {
                // Create the "Imported Contacts" folder.
                DistinguishedFolderIdType contacts = new DistinguishedFolderIdType();
                contacts.setId(DistinguishedFolderIdNameType.CONTACTS);
                contactsFolder = ContactsFolderUtil.createFolder(user, importedContactsFolderName, contacts);
            }

            LDIFReader addrbook = exportAddressBookFromMirapoint(user);
            if (addrbook == null) {
                return false;
            }

            return importAddressBook(user, addrbook, contactsFolder);
        }

    protected LDIFReader exportAddressBookFromMirapoint(User user) {
        // Do stuff here.  Magic happens.
        LDIFReader reader = null;
        // The following just imports an address book from a file called
        // "addrbook.ldif" in the current directory.  This code will get
        // replaced with the real code once I get the Miraoint API.
        FileInputStream stream = null;
        String ldifFile = "addrbook.ldif";

        try { 
            stream = new FileInputStream(ldifFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            reader = new LDIFReader(stream);
        } catch (LDAPLocalException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reader;
    }

    protected boolean importAddressBook(User user, LDIFReader addrbook, ContactsFolderType contactsFolder) {
        boolean success = false;
        if (addrbook == null) {
            return success;
        }

        LDAPMessage msg = null;
        LDAPEntry entry = null;

        try {
            while ( (msg = addrbook.readMessage()) != null) {
                entry = ((LDAPSearchResult)msg).getEntry();
                success = createContact(user, entry, contactsFolder);
                if (success == false) {
                    return success;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return success;
        } catch (LDAPException e) {
            e.printStackTrace();
            return success;
        }

        return success;
    }

    protected boolean createContact(User user, LDAPEntry entry, ContactsFolderType contactsFolder) {
        boolean success = false;

        // Create the contact.

        return success;
    }
}
