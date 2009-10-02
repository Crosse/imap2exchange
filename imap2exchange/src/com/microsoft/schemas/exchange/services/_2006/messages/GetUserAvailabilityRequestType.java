
package com.microsoft.schemas.exchange.services._2006.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas.exchange.services._2006.types.ArrayOfMailboxData;
import com.microsoft.schemas.exchange.services._2006.types.FreeBusyViewOptionsType;
import com.microsoft.schemas.exchange.services._2006.types.SerializableTimeZone;
import com.microsoft.schemas.exchange.services._2006.types.SuggestionsViewOptionsType;


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
 * <p>Java class for GetUserAvailabilityRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetUserAvailabilityRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.microsoft.com/exchange/services/2006/types}TimeZone"/>
 *         &lt;element name="MailboxDataArray" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfMailboxData"/>
 *         &lt;element ref="{http://schemas.microsoft.com/exchange/services/2006/types}FreeBusyViewOptions" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.microsoft.com/exchange/services/2006/types}SuggestionsViewOptions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetUserAvailabilityRequestType", propOrder = {
    "timeZone",
    "mailboxDataArray",
    "freeBusyViewOptions",
    "suggestionsViewOptions"
})
public class GetUserAvailabilityRequestType
    extends BaseRequestType
{

    @XmlElement(name = "TimeZone", namespace = "http://schemas.microsoft.com/exchange/services/2006/types", required = true)
    protected SerializableTimeZone timeZone;
    @XmlElement(name = "MailboxDataArray", required = true)
    protected ArrayOfMailboxData mailboxDataArray;
    @XmlElement(name = "FreeBusyViewOptions", namespace = "http://schemas.microsoft.com/exchange/services/2006/types")
    protected FreeBusyViewOptionsType freeBusyViewOptions;
    @XmlElement(name = "SuggestionsViewOptions", namespace = "http://schemas.microsoft.com/exchange/services/2006/types")
    protected SuggestionsViewOptionsType suggestionsViewOptions;

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link SerializableTimeZone }
     *     
     */
    public SerializableTimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link SerializableTimeZone }
     *     
     */
    public void setTimeZone(SerializableTimeZone value) {
        this.timeZone = value;
    }

    /**
     * Gets the value of the mailboxDataArray property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMailboxData }
     *     
     */
    public ArrayOfMailboxData getMailboxDataArray() {
        return mailboxDataArray;
    }

    /**
     * Sets the value of the mailboxDataArray property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMailboxData }
     *     
     */
    public void setMailboxDataArray(ArrayOfMailboxData value) {
        this.mailboxDataArray = value;
    }

    /**
     * Gets the value of the freeBusyViewOptions property.
     * 
     * @return
     *     possible object is
     *     {@link FreeBusyViewOptionsType }
     *     
     */
    public FreeBusyViewOptionsType getFreeBusyViewOptions() {
        return freeBusyViewOptions;
    }

    /**
     * Sets the value of the freeBusyViewOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link FreeBusyViewOptionsType }
     *     
     */
    public void setFreeBusyViewOptions(FreeBusyViewOptionsType value) {
        this.freeBusyViewOptions = value;
    }

    /**
     * Gets the value of the suggestionsViewOptions property.
     * 
     * @return
     *     possible object is
     *     {@link SuggestionsViewOptionsType }
     *     
     */
    public SuggestionsViewOptionsType getSuggestionsViewOptions() {
        return suggestionsViewOptions;
    }

    /**
     * Sets the value of the suggestionsViewOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link SuggestionsViewOptionsType }
     *     
     */
    public void setSuggestionsViewOptions(SuggestionsViewOptionsType value) {
        this.suggestionsViewOptions = value;
    }

}
