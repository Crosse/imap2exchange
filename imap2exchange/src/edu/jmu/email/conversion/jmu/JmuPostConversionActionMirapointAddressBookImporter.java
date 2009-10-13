package edu.jmu.email.conversion.jmu;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

import com.microsoft.schemas.exchange.services._2006.types.*;
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
import edu.yale.its.tp.email.conversion.imap.ImapServer;
import edu.yale.its.tp.email.conversion.imap.ImapServerFactory;

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
	private String loginUrl;
	private String addrBookUrl;

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
		// Get a session id.
		String sid = doLogin(user);
		logger.debug(String.format("Mirapoint Session Id (sid) is \"%s\"", sid));

		if (sid == "") {
			logger.warn("Could not log on to mail store");
			return null;
		}

		return getAddressBook(user, sid);
	}

	protected LDIFReader getAddressBook(User user, String sid) {
		LDIFReader reader = null;

		String postData = "";
		try {
			postData = String.format("%s=%s&%s=%s&%s=%s",
					URLEncoder.encode("sid", "UTF-8"),
					URLEncoder.encode(sid, "UTF-8"),
					URLEncoder.encode("format", "UTF-8"),
					URLEncoder.encode("ldif", "UTF-8"),
					URLEncoder.encode("cdata", "UTF-8"),
					URLEncoder.encode("false", "UTF-8"));
			logger.debug(String.format("postData: %s", postData));
		} catch (UnsupportedEncodingException e1) {
			logger.error("Error encoding POST data for address book ldif export");
		}

		String url = String.format("https://%s%s", user.getSourceImapPo(), addrBookUrl);

		InputStream response = submitHttpRequest(url, postData);

		// Add the "version: 1" line to the top of the response.  Dirty hack.
		StringBuilder sb = new StringBuilder();
		sb.append("version: 1" + "\n");

		BufferedReader br = new BufferedReader(new InputStreamReader(response));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) { }
		}

		InputStream ldifStream = new ByteArrayInputStream(sb.toString().getBytes());


		try {
			reader = new LDIFReader(ldifStream);
		} catch (LDAPLocalException e) {
			logger.error("Could not parse LDIF data:  " + e.getLDAPErrorMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Could not parse LDIF data:  " + e.getMessage());
			e.printStackTrace();
		}

		return reader;
	}

	protected String doLogin(User user) {
		String sid = "";

		// Use this to get the adminUid and adminPwd for the mail store.
		ImapServer server = ImapServerFactory.getInstance().getImapServer(user.getSourceImapPo());

		String loginData = "";

		// Encode the POST string in the form:
		// user=adminUid&password=adminPwd&caluser=migrateduser
		try {
			loginData = String.format("%s=%s&%s=%s&%s=%s",
					URLEncoder.encode("user", "UTF-8"), 
					URLEncoder.encode(server.getAdminUid(), "UTF-8"),
					URLEncoder.encode("password", "UTF-8"), 
					URLEncoder.encode(server.getAdminPwd(), "UTF-8"),
					URLEncoder.encode("caluser", "UTF-8"), 
					URLEncoder.encode(user.getPrimarySMTPAddress(), "UTF-8"));
			// logger.debug(String.format("loginData:  %s", loginData));
		} catch (UnsupportedEncodingException e1) {
			logger.error("Error encoding POST data for mail store logon");
		}

		String url = String.format("https://%s%s", user.getSourceImapPo(), loginUrl);

		InputStream response = submitHttpRequest(url, loginData);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response));
		String line;
		String startTag = "<sid>";
		String endTag = "</sid>";
		try {
			while ((line = rd.readLine()) != null) {
				logger.debug(String.format("Response: %s", line));
				if (line.contains((CharSequence)"<sid>")) {
					int start = line.indexOf(startTag) + startTag.length();
					int end = line.indexOf(endTag);
					sid = line.substring(start, end);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sid;
	}

	protected InputStream submitHttpRequest(String url, String postData) {
		InputStream response = null;
		URLConnection conn = null;
		OutputStreamWriter wr = null;
		URL requestUrl = null;

		// Submit the HTTP request.
		try {
			requestUrl = new URL(url);
			logger.debug(String.format("requestUrl = %s", requestUrl.toString()));
			conn = requestUrl.openConnection();
			if (postData != null) {
				conn.setDoOutput(true);
				wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(postData);
				wr.flush();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				wr.close();
			} catch (IOException e) { }
		}

		try {
			// Get the response.
			response = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	protected boolean importAddressBook(User user, LDIFReader addrbook, ContactsFolderType contactsFolder) {
		boolean success = false;
		if (addrbook == null) {
			return success;
		}

		LDAPMessage msg = null;
		LDAPEntry entry = null;
		List<LDAPEntry> groups = new ArrayList<LDAPEntry>();

		try {
			while ( (msg = addrbook.readMessage()) != null) {
				entry = ((LDAPSearchResult)msg).getEntry();

				boolean isGroup = false;
				for (String oclass : entry.getAttribute("objectclass").getStringValueArray()) {
					if ("groupofnames".equalsIgnoreCase(oclass)) {
						isGroup = true;
						break;
					}
				}
				if (isGroup) {
					groups.add(entry);
				}else {
					// success = true;
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

		// success = processGroups(user, groups, contactsFolder);

		return success;
	}

	private boolean processGroups(User user, List<LDAPEntry> groups,
			ContactsFolderType contactsFolder) {

		String startTag = "cn=";
		String endTag = ",mail=";
		String wrappedEntryIDPreamble = 
			ContactUtil.WRAPPED_ENTRYID_FLAGS + 
			ContactUtil.WRAPPED_ENTRYID_PROVIDER_UID + 
			ContactUtil.WRAPPED_ENTRYID_TYPE_CONTACT_ENTRYID;

		BASE64Encoder encoder = new BASE64Encoder();

		for (LDAPEntry group : groups) {
			logger.debug(String.format("Working on group %s", group.getDN()));
			ItemType distributionList = new ItemType();
			distributionList.setItemClass("IPM.DistList");
			
			String cn = getEntryAttribute(group, "cn");

			ExtendedPropertyType nameProperty = new ExtendedPropertyType();
			ExtendedPropertyType displaNameProperty = new ExtendedPropertyType();
			ExtendedPropertyType fileAsProperty = new ExtendedPropertyType();
			ExtendedPropertyType membersProperty = new ExtendedPropertyType();
			ExtendedPropertyType oneOffMembersProperty = new ExtendedPropertyType();

			PathToExtendedFieldType namePropertyType = new PathToExtendedFieldType();
			namePropertyType.setPropertyId(ContactUtil.PID_LID_DISTRIBUTION_LIST_NAME);
			namePropertyType.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
			namePropertyType.setPropertyType(MapiPropertyTypeType.STRING);
			
			PathToExtendedFieldType fileAsPropertyType = new PathToExtendedFieldType();
			fileAsPropertyType.setPropertyId(0x8005);
			fileAsPropertyType.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
			fileAsPropertyType.setPropertyType(MapiPropertyTypeType.STRING);
			
			PathToExtendedFieldType displayNamePropertyType = new PathToExtendedFieldType();
			displayNamePropertyType.setPropertyTag("0x3001");
			displayNamePropertyType.setPropertyType(MapiPropertyTypeType.STRING);

			PathToExtendedFieldType membersPropertyType = new PathToExtendedFieldType();
			membersPropertyType.setPropertyId(ContactUtil.PID_LID_DISTRIBUTION_LIST_MEMBERS);
			membersPropertyType.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
			membersPropertyType.setPropertyType(MapiPropertyTypeType.BINARY_ARRAY);

			PathToExtendedFieldType oneOffMembersPropertyType = new PathToExtendedFieldType();
			oneOffMembersPropertyType.setPropertyId(ContactUtil.PID_LID_DISTRIBUTION_LIST_ONE_OFF_MEMBERS);
			oneOffMembersPropertyType.setDistinguishedPropertySetId(DistinguishedPropertySetType.ADDRESS);
			oneOffMembersPropertyType.setPropertyType(MapiPropertyTypeType.BINARY_ARRAY);

			NonEmptyArrayOfPropertyValuesType members = new NonEmptyArrayOfPropertyValuesType();
			NonEmptyArrayOfPropertyValuesType oneOffMembers = new NonEmptyArrayOfPropertyValuesType();

			for (String member : group.getAttribute("member").getStringValueArray()) {
				int start = member.indexOf(startTag) + startTag.length();
				int end = member.indexOf(endTag);
				String membercn = member.substring(start, end);
				String mail = member.substring(end + endTag.length());
				if (mail.indexOf("@") < 0) {
					mail += "@" + JmuSite.getInstance().getMailDomain();
				}
				String displayName = String.format("%s (%s)", membercn, mail);
				

				ContactItemType contact = ContactUtil.getContact(user, displayName, contactsFolder);
				if (contact == null) {
					logger.debug(String.format("Constructing a One-Off Entry for %s", displayName));
					String oneOffMember = "00000000812B1FA4BEA310199D6E00DD010F540200000190";
					String pad = "0000";
					oneOffMember += convertToHex(membercn) + pad + convertToHex("SMTP") + pad + convertToHex(mail);
					oneOffMembers.getValue().add(0, encoder.encode(oneOffMember.getBytes()));
					members.getValue().add(0, encoder.encode(oneOffMember.getBytes()));
				} else {
					logger.debug(String.format("Found contact \"%s\" for member \"%s\"", contact.getDisplayName(), member));
					String wrappedEntryId = wrappedEntryIDPreamble + contact.getItemId().getId();
					members.getValue().add(encoder.encode(wrappedEntryId.getBytes()));
				}
				
			}
			logger.debug(String.format("Finished process members for group %s", group.getDN()));

			distributionList.setSubject(cn);

			nameProperty.setExtendedFieldURI(namePropertyType);
			nameProperty.setValue(cn);
			distributionList.getExtendedProperty().add(nameProperty);

			displaNameProperty.setExtendedFieldURI(displayNamePropertyType);
			displaNameProperty.setValue(cn);
			distributionList.getExtendedProperty().add(displaNameProperty);
			
			fileAsProperty.setExtendedFieldURI(fileAsPropertyType);
			fileAsProperty.setValue(cn);
			distributionList.getExtendedProperty().add(fileAsProperty);
			
			if (members.getValue().size() > 0) {
				membersProperty.setExtendedFieldURI(membersPropertyType);
				membersProperty.setValues(members);
				distributionList.getExtendedProperty().add(membersProperty);
			}
			
			if (oneOffMembers.getValue().size() > 0) {
				oneOffMembersProperty.setExtendedFieldURI(oneOffMembersPropertyType);
				oneOffMembersProperty.setValues(oneOffMembers);
				distributionList.getExtendedProperty().add(oneOffMembersProperty);
			}

			ContactUtil.createDistributionList(user, distributionList, contactsFolder);
		}
		return true;
	}

	private static String convertToHex(String asciiString) {
		String hex = "";
		for (char c : asciiString.toCharArray()) {
			hex = hex.concat(Integer.toHexString(c)) + "00";
		}
//		logger.debug(String.format("result of Hex conversion for \"%s\" is \"%s\"", asciiString, hex));
		return hex;
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

		// Short-circuit really quickly:  do a search for the FileAs field
		// to check if the contact already exists.  If so, just return that.
		if (ContactUtil.getContact(user, contactItem.getFileAs(), contactsFolder) != null) {
			logger.debug(String.format("Contact \"%s\" already exists.  Skipping", contactItem.getFileAs()));
			success = true;
			return success;
		}

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

		// Set the Body to "description".
		contactItem.setBody(new BodyType());
		contactItem.getBody().setBodyType(BodyTypeType.TEXT);
		contactItem.getBody().setValue(getEntryAttribute(entry, "description"));

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

		logger.debug(String.format("Creating contact %s", entry.getDN()));
		if (ContactUtil.createContact(user, contactItem, contactsFolder) != null) {
			success = true;
		} else {
			logger.warn(String.format("Error creating contact %s", entry.getDN()));
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
			calendar.setDay(Integer.parseInt(day + 1));
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

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getAddrBookUrl() {
		return addrBookUrl;
	}

	public void setAddrBookUrl(String addrBookUrl) {
		this.addrBookUrl = addrBookUrl;
	}


}
