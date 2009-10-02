
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
 * <p>Java class for FileAsMappingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FileAsMappingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="LastCommaFirst"/>
 *     &lt;enumeration value="FirstSpaceLast"/>
 *     &lt;enumeration value="Company"/>
 *     &lt;enumeration value="LastCommaFirstCompany"/>
 *     &lt;enumeration value="CompanyLastFirst"/>
 *     &lt;enumeration value="LastFirst"/>
 *     &lt;enumeration value="LastFirstCompany"/>
 *     &lt;enumeration value="CompanyLastCommaFirst"/>
 *     &lt;enumeration value="LastFirstSuffix"/>
 *     &lt;enumeration value="LastSpaceFirstCompany"/>
 *     &lt;enumeration value="CompanyLastSpaceFirst"/>
 *     &lt;enumeration value="LastSpaceFirst"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FileAsMappingType")
@XmlEnum
public enum FileAsMappingType {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("LastCommaFirst")
    LAST_COMMA_FIRST("LastCommaFirst"),
    @XmlEnumValue("FirstSpaceLast")
    FIRST_SPACE_LAST("FirstSpaceLast"),
    @XmlEnumValue("Company")
    COMPANY("Company"),
    @XmlEnumValue("LastCommaFirstCompany")
    LAST_COMMA_FIRST_COMPANY("LastCommaFirstCompany"),
    @XmlEnumValue("CompanyLastFirst")
    COMPANY_LAST_FIRST("CompanyLastFirst"),
    @XmlEnumValue("LastFirst")
    LAST_FIRST("LastFirst"),
    @XmlEnumValue("LastFirstCompany")
    LAST_FIRST_COMPANY("LastFirstCompany"),
    @XmlEnumValue("CompanyLastCommaFirst")
    COMPANY_LAST_COMMA_FIRST("CompanyLastCommaFirst"),
    @XmlEnumValue("LastFirstSuffix")
    LAST_FIRST_SUFFIX("LastFirstSuffix"),
    @XmlEnumValue("LastSpaceFirstCompany")
    LAST_SPACE_FIRST_COMPANY("LastSpaceFirstCompany"),
    @XmlEnumValue("CompanyLastSpaceFirst")
    COMPANY_LAST_SPACE_FIRST("CompanyLastSpaceFirst"),
    @XmlEnumValue("LastSpaceFirst")
    LAST_SPACE_FIRST("LastSpaceFirst");
    private final String value;

    FileAsMappingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FileAsMappingType fromValue(String v) {
        for (FileAsMappingType c: FileAsMappingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
