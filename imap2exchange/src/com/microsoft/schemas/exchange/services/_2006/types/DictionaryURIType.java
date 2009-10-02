
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


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
 * <p>Java class for DictionaryURIType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DictionaryURIType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="item:InternetMessageHeader"/>
 *     &lt;enumeration value="contacts:ImAddress"/>
 *     &lt;enumeration value="contacts:PhysicalAddress:Street"/>
 *     &lt;enumeration value="contacts:PhysicalAddress:City"/>
 *     &lt;enumeration value="contacts:PhysicalAddress:State"/>
 *     &lt;enumeration value="contacts:PhysicalAddress:CountryOrRegion"/>
 *     &lt;enumeration value="contacts:PhysicalAddress:PostalCode"/>
 *     &lt;enumeration value="contacts:PhoneNumber"/>
 *     &lt;enumeration value="contacts:EmailAddress"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DictionaryURIType")
@XmlEnum
public enum DictionaryURIType {

    @XmlEnumValue("item:InternetMessageHeader")
    ITEM_INTERNET_MESSAGE_HEADER("item:InternetMessageHeader"),
    @XmlEnumValue("contacts:ImAddress")
    CONTACTS_IM_ADDRESS("contacts:ImAddress"),
    @XmlEnumValue("contacts:PhysicalAddress:Street")
    CONTACTS_PHYSICAL_ADDRESS_STREET("contacts:PhysicalAddress:Street"),
    @XmlEnumValue("contacts:PhysicalAddress:City")
    CONTACTS_PHYSICAL_ADDRESS_CITY("contacts:PhysicalAddress:City"),
    @XmlEnumValue("contacts:PhysicalAddress:State")
    CONTACTS_PHYSICAL_ADDRESS_STATE("contacts:PhysicalAddress:State"),
    @XmlEnumValue("contacts:PhysicalAddress:CountryOrRegion")
    CONTACTS_PHYSICAL_ADDRESS_COUNTRY_OR_REGION("contacts:PhysicalAddress:CountryOrRegion"),
    @XmlEnumValue("contacts:PhysicalAddress:PostalCode")
    CONTACTS_PHYSICAL_ADDRESS_POSTAL_CODE("contacts:PhysicalAddress:PostalCode"),
    @XmlEnumValue("contacts:PhoneNumber")
    CONTACTS_PHONE_NUMBER("contacts:PhoneNumber"),
    @XmlEnumValue("contacts:EmailAddress")
    CONTACTS_EMAIL_ADDRESS("contacts:EmailAddress");
    private final String value;

    DictionaryURIType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DictionaryURIType fromValue(String v) {
        for (DictionaryURIType c: DictionaryURIType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
