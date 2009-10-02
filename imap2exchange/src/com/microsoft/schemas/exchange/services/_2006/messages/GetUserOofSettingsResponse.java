
package com.microsoft.schemas.exchange.services._2006.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas.exchange.services._2006.types.ExternalAudience;
import com.microsoft.schemas.exchange.services._2006.types.UserOofSettings;


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
 * <p>Java class for GetUserOofSettingsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUserOofSettingsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element ref="{http://schemas.microsoft.com/exchange/services/2006/types}OofSettings" minOccurs="0"/>
 *         &lt;element name="AllowExternalOof" type="{http://schemas.microsoft.com/exchange/services/2006/types}ExternalAudience" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUserOofSettingsResponse", propOrder = {
    "responseMessage",
    "oofSettings",
    "allowExternalOof"
})
public class GetUserOofSettingsResponse {

    @XmlElement(name = "ResponseMessage", required = true)
    protected ResponseMessageType responseMessage;
    @XmlElement(name = "OofSettings", namespace = "http://schemas.microsoft.com/exchange/services/2006/types")
    protected UserOofSettings oofSettings;
    @XmlElement(name = "AllowExternalOof")
    protected ExternalAudience allowExternalOof;

    /**
     * Gets the value of the responseMessage property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseMessageType }
     *     
     */
    public ResponseMessageType getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets the value of the responseMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseMessageType }
     *     
     */
    public void setResponseMessage(ResponseMessageType value) {
        this.responseMessage = value;
    }

    /**
     * Gets the value of the oofSettings property.
     * 
     * @return
     *     possible object is
     *     {@link UserOofSettings }
     *     
     */
    public UserOofSettings getOofSettings() {
        return oofSettings;
    }

    /**
     * Sets the value of the oofSettings property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserOofSettings }
     *     
     */
    public void setOofSettings(UserOofSettings value) {
        this.oofSettings = value;
    }

    /**
     * Gets the value of the allowExternalOof property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalAudience }
     *     
     */
    public ExternalAudience getAllowExternalOof() {
        return allowExternalOof;
    }

    /**
     * Sets the value of the allowExternalOof property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalAudience }
     *     
     */
    public void setAllowExternalOof(ExternalAudience value) {
        this.allowExternalOof = value;
    }

}
