
package com.microsoft.schemas.exchange.services._2006.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas.exchange.services._2006.types.AffectedTaskOccurrencesType;
import com.microsoft.schemas.exchange.services._2006.types.CalendarItemCreateOrDeleteOperationType;
import com.microsoft.schemas.exchange.services._2006.types.DisposalType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfBaseItemIdsType;


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
 * <p>Java class for DeleteItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeleteItemType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element name="ItemIds" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyArrayOfBaseItemIdsType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="DeleteType" use="required" type="{http://schemas.microsoft.com/exchange/services/2006/types}DisposalType" />
 *       &lt;attribute name="SendMeetingCancellations" type="{http://schemas.microsoft.com/exchange/services/2006/types}CalendarItemCreateOrDeleteOperationType" />
 *       &lt;attribute name="AffectedTaskOccurrences" type="{http://schemas.microsoft.com/exchange/services/2006/types}AffectedTaskOccurrencesType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeleteItemType", propOrder = {
    "itemIds"
})
public class DeleteItemType
    extends BaseRequestType
{

    @XmlElement(name = "ItemIds", required = true)
    protected NonEmptyArrayOfBaseItemIdsType itemIds;
    @XmlAttribute(name = "DeleteType", required = true)
    protected DisposalType deleteType;
    @XmlAttribute(name = "SendMeetingCancellations")
    protected CalendarItemCreateOrDeleteOperationType sendMeetingCancellations;
    @XmlAttribute(name = "AffectedTaskOccurrences")
    protected AffectedTaskOccurrencesType affectedTaskOccurrences;

    /**
     * Gets the value of the itemIds property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyArrayOfBaseItemIdsType }
     *     
     */
    public NonEmptyArrayOfBaseItemIdsType getItemIds() {
        return itemIds;
    }

    /**
     * Sets the value of the itemIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyArrayOfBaseItemIdsType }
     *     
     */
    public void setItemIds(NonEmptyArrayOfBaseItemIdsType value) {
        this.itemIds = value;
    }

    /**
     * Gets the value of the deleteType property.
     * 
     * @return
     *     possible object is
     *     {@link DisposalType }
     *     
     */
    public DisposalType getDeleteType() {
        return deleteType;
    }

    /**
     * Sets the value of the deleteType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisposalType }
     *     
     */
    public void setDeleteType(DisposalType value) {
        this.deleteType = value;
    }

    /**
     * Gets the value of the sendMeetingCancellations property.
     * 
     * @return
     *     possible object is
     *     {@link CalendarItemCreateOrDeleteOperationType }
     *     
     */
    public CalendarItemCreateOrDeleteOperationType getSendMeetingCancellations() {
        return sendMeetingCancellations;
    }

    /**
     * Sets the value of the sendMeetingCancellations property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalendarItemCreateOrDeleteOperationType }
     *     
     */
    public void setSendMeetingCancellations(CalendarItemCreateOrDeleteOperationType value) {
        this.sendMeetingCancellations = value;
    }

    /**
     * Gets the value of the affectedTaskOccurrences property.
     * 
     * @return
     *     possible object is
     *     {@link AffectedTaskOccurrencesType }
     *     
     */
    public AffectedTaskOccurrencesType getAffectedTaskOccurrences() {
        return affectedTaskOccurrences;
    }

    /**
     * Sets the value of the affectedTaskOccurrences property.
     * 
     * @param value
     *     allowed object is
     *     {@link AffectedTaskOccurrencesType }
     *     
     */
    public void setAffectedTaskOccurrences(AffectedTaskOccurrencesType value) {
        this.affectedTaskOccurrences = value;
    }

}
