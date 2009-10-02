
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 * <p>Java class for ItemChangeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ItemChangeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="ItemId" type="{http://schemas.microsoft.com/exchange/services/2006/types}ItemIdType"/>
 *           &lt;element name="OccurrenceItemId" type="{http://schemas.microsoft.com/exchange/services/2006/types}OccurrenceItemIdType"/>
 *           &lt;element name="RecurringMasterItemId" type="{http://schemas.microsoft.com/exchange/services/2006/types}RecurringMasterItemIdType"/>
 *         &lt;/choice>
 *         &lt;element name="Updates" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyArrayOfItemChangeDescriptionsType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemChangeType", propOrder = {
    "itemId",
    "occurrenceItemId",
    "recurringMasterItemId",
    "updates"
})
public class ItemChangeType {

    @XmlElement(name = "ItemId")
    protected ItemIdType itemId;
    @XmlElement(name = "OccurrenceItemId")
    protected OccurrenceItemIdType occurrenceItemId;
    @XmlElement(name = "RecurringMasterItemId")
    protected RecurringMasterItemIdType recurringMasterItemId;
    @XmlElement(name = "Updates", required = true)
    protected NonEmptyArrayOfItemChangeDescriptionsType updates;

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link ItemIdType }
     *     
     */
    public ItemIdType getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemIdType }
     *     
     */
    public void setItemId(ItemIdType value) {
        this.itemId = value;
    }

    /**
     * Gets the value of the occurrenceItemId property.
     * 
     * @return
     *     possible object is
     *     {@link OccurrenceItemIdType }
     *     
     */
    public OccurrenceItemIdType getOccurrenceItemId() {
        return occurrenceItemId;
    }

    /**
     * Sets the value of the occurrenceItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link OccurrenceItemIdType }
     *     
     */
    public void setOccurrenceItemId(OccurrenceItemIdType value) {
        this.occurrenceItemId = value;
    }

    /**
     * Gets the value of the recurringMasterItemId property.
     * 
     * @return
     *     possible object is
     *     {@link RecurringMasterItemIdType }
     *     
     */
    public RecurringMasterItemIdType getRecurringMasterItemId() {
        return recurringMasterItemId;
    }

    /**
     * Sets the value of the recurringMasterItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecurringMasterItemIdType }
     *     
     */
    public void setRecurringMasterItemId(RecurringMasterItemIdType value) {
        this.recurringMasterItemId = value;
    }

    /**
     * Gets the value of the updates property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyArrayOfItemChangeDescriptionsType }
     *     
     */
    public NonEmptyArrayOfItemChangeDescriptionsType getUpdates() {
        return updates;
    }

    /**
     * Sets the value of the updates property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyArrayOfItemChangeDescriptionsType }
     *     
     */
    public void setUpdates(NonEmptyArrayOfItemChangeDescriptionsType value) {
        this.updates = value;
    }

}
