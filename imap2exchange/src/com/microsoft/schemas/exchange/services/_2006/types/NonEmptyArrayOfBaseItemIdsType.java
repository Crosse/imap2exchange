
package com.microsoft.schemas.exchange.services._2006.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 * <p>Java class for NonEmptyArrayOfBaseItemIdsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NonEmptyArrayOfBaseItemIdsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="ItemId" type="{http://schemas.microsoft.com/exchange/services/2006/types}ItemIdType"/>
 *         &lt;element name="OccurrenceItemId" type="{http://schemas.microsoft.com/exchange/services/2006/types}OccurrenceItemIdType"/>
 *         &lt;element name="RecurringMasterItemId" type="{http://schemas.microsoft.com/exchange/services/2006/types}RecurringMasterItemIdType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NonEmptyArrayOfBaseItemIdsType", propOrder = {
    "itemIdOrOccurrenceItemIdOrRecurringMasterItemId"
})
public class NonEmptyArrayOfBaseItemIdsType {

    @XmlElements({
        @XmlElement(name = "RecurringMasterItemId", type = RecurringMasterItemIdType.class),
        @XmlElement(name = "OccurrenceItemId", type = OccurrenceItemIdType.class),
        @XmlElement(name = "ItemId", type = ItemIdType.class)
    })
    protected List<BaseItemIdType> itemIdOrOccurrenceItemIdOrRecurringMasterItemId;

    /**
     * Gets the value of the itemIdOrOccurrenceItemIdOrRecurringMasterItemId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemIdOrOccurrenceItemIdOrRecurringMasterItemId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemIdOrOccurrenceItemIdOrRecurringMasterItemId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RecurringMasterItemIdType }
     * {@link OccurrenceItemIdType }
     * {@link ItemIdType }
     * 
     * 
     */
    public List<BaseItemIdType> getItemIdOrOccurrenceItemIdOrRecurringMasterItemId() {
        if (itemIdOrOccurrenceItemIdOrRecurringMasterItemId == null) {
            itemIdOrOccurrenceItemIdOrRecurringMasterItemId = new ArrayList<BaseItemIdType>();
        }
        return this.itemIdOrOccurrenceItemIdOrRecurringMasterItemId;
    }

}
