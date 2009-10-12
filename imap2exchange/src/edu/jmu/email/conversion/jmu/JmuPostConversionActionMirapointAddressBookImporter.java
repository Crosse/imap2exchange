package edu.jmu.email.conversion.jmu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.schemas.exchange.services._2006.types.ArrayOfStringsType;
import com.microsoft.schemas.exchange.services._2006.types.ContactItemType;
import com.microsoft.schemas.exchange.services._2006.types.ContactsFolderType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdNameType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressDictionaryEntryType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressDictionaryType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressKeyType;
import com.microsoft.schemas.exchange.services._2006.types.PhoneNumberDictionaryEntryType;
import com.microsoft.schemas.exchange.services._2006.types.PhoneNumberDictionaryType;
import com.microsoft.schemas.exchange.services._2006.types.PhoneNumberKeyType;
import com.microsoft.schemas.exchange.services._2006.types.PhysicalAddressDictionaryEntryType;
import com.microsoft.schemas.exchange.services._2006.types.PhysicalAddressDictionaryType;
import com.microsoft.schemas.exchange.services._2006.types.PhysicalAddressKeyType;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPLocalException;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPSearchResult;
import com.novell.ldap.util.LDIFReader;

import edu.jmu.email.conversion.exchange.ContactUtil;
import edu.jmu.email.conversion.exchange.ContactsFolderUtil;
import edu.yale.its.tp.email.conversion.ExchangeConversion;
import edu.yale.its.tp.email.conversion.PluggableConversionAction;
import edu.yale.its.tp.email.conversion.User;

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

	private static Log logger = LogFactory.getLog(JmuPostConversionActionMirapointAddressBookImporter.class);
	private String importedContactsFolderName;
	private static final int version = 1;

	@Override
	public boolean perform (ExchangeConversion conv) {
		User user = conv.getUser();

		LDIFReader addrbook = exportAddressBookFromMirapoint(user);
		if (addrbook == null) {
			return false;
		}

		ContactsFolderType contactsFolder = createImportFolder(user);
		logger.info(String.format("Using folder \"%s\" as the Contacts import folder", contactsFolder.getDisplayName()));

		return importAddressBook(user, addrbook, contactsFolder);
	}

	protected ContactsFolderType createImportFolder(User user) {
		DistinguishedFolderIdType parentFolderId = new DistinguishedFolderIdType();
		parentFolderId.setId(DistinguishedFolderIdNameType.CONTACTS);

		ContactsFolderType contactsFolder = null;
		contactsFolder = ContactsFolderUtil.getFolder(user, importedContactsFolderName, parentFolderId);

		if (contactsFolder == null) {
			logger.info(String.format("Creating contacts folder \"%s\"", importedContactsFolderName));
			ContactsFolderUtil.createFolder(user, importedContactsFolderName, parentFolderId);
			contactsFolder = ContactsFolderUtil.getFolder(user, importedContactsFolderName, parentFolderId);
			if (contactsFolder == null) {
				logger.error(String.format("Could not create folder \"%s\"", importedContactsFolderName));
			} else {
				logger.debug(String.format("Created folder \"%s\"", contactsFolder.getDisplayName()));
			}
		}
		return contactsFolder;
	}

	protected LDIFReader exportAddressBookFromMirapoint(User user) {
		// Do stuff here.  Magic happens.
		LDIFReader reader = null;
		// The following just imports an address book from a file called
		// "addrbook.ldif" in the current directory.  This code will get
		// replaced with the real code once I get the Miraoint API.
		FileInputStream stream = null;

		String ldifFile = "config/addrbook.ldif";

		try { 
			stream = new FileInputStream(ldifFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			reader = new LDIFReader(stream, version);
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
				
				boolean isGroup = false;
				for (String oclass : entry.getAttribute("objectclass").getStringValueArray()) {
//					logger.debug(String.format("entry %s is of class %s", entry.getDN(), oclass));
					if ("groupofnames".equalsIgnoreCase(oclass)) {
						logger.debug(String.format("Skipping group %s", entry.getDN()));
						isGroup = true;
					}
				}
				if (!isGroup) {
					logger.debug(String.format("Creating contact %s", entry.getDN()));
					success = createContact(user, entry, contactsFolder);
				}
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
		ContactItemType contactItem = new ContactItemType();

		String email = getEntryAttribute(entry, "mail");
		// Fix an issue with certain users not adding "@jmu.edu" to a saved
		// contact's email address.
		if (email.length() > 0) {
			if (!(email.contains((CharSequence)"@"))) {
				email = email.concat(String.format("@%s", JmuSite.getInstance().getMailDomain()));
			}
		}
		// Set the FileAs attribute to "cn (mail)".
		contactItem.setFileAs(String.format("%s (%s)", 
				getEntryAttribute(entry, "cn"), email));

		// Set the Categories attribute to "category".
		ArrayOfStringsType categories = new ArrayOfStringsType();
		categories.getString().add(getEntryAttribute(entry, "category"));
		contactItem.setCategories(categories);

		// Set the Surname to "sn".
		contactItem.setSurname(getEntryAttribute(entry, "sn"));
		// Set the GivenName to "givenname".
		contactItem.setGivenName(getEntryAttribute(entry, "givenname"));

		// Set the EmailAddress1 to "mail".
		EmailAddressDictionaryEntryType emailAddressEntry = new EmailAddressDictionaryEntryType();
		emailAddressEntry.setKey(EmailAddressKeyType.EMAIL_ADDRESS_1);
		emailAddressEntry.setValue(email);
		EmailAddressDictionaryType emailAddressDict = new EmailAddressDictionaryType(); 
		emailAddressDict.getEntry().add(emailAddressEntry);
		contactItem.setEmailAddresses(emailAddressDict);

		// Set the CompanyName to "o".
		contactItem.setCompanyName(getEntryAttribute(entry, "o"));
		// Set the Department to "ou".
		contactItem.setDepartment(getEntryAttribute(entry, "ou"));
		// Set the JobTitle to "title".
		contactItem.setJobTitle(getEntryAttribute(entry, "title"));

		// Set the PhysicalAddress entry "Other" to the corresponding fields.
		PhysicalAddressDictionaryEntryType physicalAddressEntry = new PhysicalAddressDictionaryEntryType();
		physicalAddressEntry.setKey(PhysicalAddressKeyType.OTHER);
		physicalAddressEntry.setStreet(getEntryAttribute(entry, "postaladdress"));
		physicalAddressEntry.setCity(getEntryAttribute(entry, "l"));
		physicalAddressEntry.setState(getEntryAttribute(entry, "st"));
		physicalAddressEntry.setPostalCode(getEntryAttribute(entry, "postalcode"));
		physicalAddressEntry.setCountryOrRegion(getEntryAttribute(entry, "c"));
		PhysicalAddressDictionaryType physicalAddressDict = new PhysicalAddressDictionaryType();
		physicalAddressDict.getEntry().add(physicalAddressEntry);
		contactItem.setPhysicalAddresses(physicalAddressDict);

		PhoneNumberDictionaryType phoneNumberDict = new PhoneNumberDictionaryType();
		// Set the "Business" PhoneNumber to "telephonenumber".
		PhoneNumberDictionaryEntryType businessPhoneNumberEntry = new PhoneNumberDictionaryEntryType();
		businessPhoneNumberEntry.setKey(PhoneNumberKeyType.BUSINESS_PHONE);
		businessPhoneNumberEntry.setValue(getEntryAttribute(entry, "telephonenumber"));
		phoneNumberDict.getEntry().add(businessPhoneNumberEntry);
		// Set the "Home" PhoneNumber to "homephone".
		PhoneNumberDictionaryEntryType homePhoneNumberEntry = new PhoneNumberDictionaryEntryType();
		homePhoneNumberEntry.setKey(PhoneNumberKeyType.HOME_PHONE);
		homePhoneNumberEntry.setValue(getEntryAttribute(entry, "homephone"));
		phoneNumberDict.getEntry().add(homePhoneNumberEntry);
		// Set the "Mobile" PhoneNumber to "mobile".
		PhoneNumberDictionaryEntryType mobilePhoneNumberEntry = new PhoneNumberDictionaryEntryType();
		mobilePhoneNumberEntry.setKey(PhoneNumberKeyType.MOBILE_PHONE);
		mobilePhoneNumberEntry.setValue(getEntryAttribute(entry, "mobile"));
		phoneNumberDict.getEntry().add(mobilePhoneNumberEntry);
		// Set the "Pager" PhoneNumber to "pager".
		PhoneNumberDictionaryEntryType pagerPhoneNumberEntry = new PhoneNumberDictionaryEntryType();
		pagerPhoneNumberEntry.setKey(PhoneNumberKeyType.PAGER);
		pagerPhoneNumberEntry.setValue(getEntryAttribute(entry, "pager"));
		phoneNumberDict.getEntry().add(pagerPhoneNumberEntry);
		// Set the "Business Fax" PhoneNumber to "facimiletelephonenumber".
		PhoneNumberDictionaryEntryType faxPhoneNumberEntry = new PhoneNumberDictionaryEntryType();
		faxPhoneNumberEntry.setKey(PhoneNumberKeyType.BUSINESS_FAX);
		faxPhoneNumberEntry.setValue(getEntryAttribute(entry, "facimiletelephonenumber"));
		phoneNumberDict.getEntry().add(faxPhoneNumberEntry);
		// Finally, set phoneNumberDict on the contactItem.
		contactItem.setPhoneNumbers(phoneNumberDict);

		// Set the Nickname to "xmozillanickname".
		contactItem.setNickname(getEntryAttribute(entry, "xmozillanickname"));
		// Set the BusinessHomePage to "homeurl".
		contactItem.setBusinessHomePage(getEntryAttribute(entry, "homeurl"));
		// Set the DisplayName to "displayname".
		contactItem.setDisplayName(getEntryAttribute(entry, "displayname"));

		// Set the WeddingAnniversary to the corresponding fields.
		XMLGregorianCalendar weddingAnniversary = 
			convertToXMLGregorianCalendar(
					getEntryAttribute(entry, "anniversaryyear"),
					getEntryAttribute(entry, "anniversarymonth"),
					getEntryAttribute(entry, "anniversaryday"));

		if (weddingAnniversary != null) {
			contactItem.setWeddingAnniversary(weddingAnniversary);
		}


		// Set the Birthday to the corresponding fields.
		XMLGregorianCalendar birthdate = 
			convertToXMLGregorianCalendar(
					getEntryAttribute(entry, "birthyear"),
					getEntryAttribute(entry, "birthmonth"),
					getEntryAttribute(entry, "birthday"));

		if (birthdate != null) {
			contactItem.setBirthday(birthdate);
		}

		if (ContactUtil.createContact(user, contactItem, contactsFolder) != null) {
			success = true;
		} else {
			success = false;
		}

		return success;
	}

	protected String getEntryAttribute(LDAPEntry entry, String attribute) {
		if (entry.getAttribute(attribute) != null) {
			return entry.getAttribute(attribute).getStringValue();
		} else {
			return "";
		}
	}

	protected XMLGregorianCalendar convertToXMLGregorianCalendar(String year, String month, String day) {
		XMLGregorianCalendar calendar = null;

		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
			calendar = df.newXMLGregorianCalendar();
			calendar.setYear(Integer.parseInt(year));
			calendar.setMonth(Integer.parseInt(month));
			calendar.setDay(Integer.parseInt(day));
			calendar.setSecond(0);
			calendar.setMinute(0);
			calendar.setHour(0);
			
			return calendar;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return the importedContactsFolderName
	 */
	public String getImportedContactsFolderName() {
		return importedContactsFolderName;
	}

	/**
	 * @param importedContactsFolderName the importedContactsFolderName to set
	 */
	public void setImportedContactsFolderName(String importedContactsFolderName) {
		this.importedContactsFolderName = importedContactsFolderName;
	}
}
