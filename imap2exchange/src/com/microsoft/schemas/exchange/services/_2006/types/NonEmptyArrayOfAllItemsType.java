
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
 * <p>Java class for NonEmptyArrayOfAllItemsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NonEmptyArrayOfAllItemsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="Item" type="{http://schemas.microsoft.com/exchange/services/2006/types}ItemType"/>
 *           &lt;element name="Message" type="{http://schemas.microsoft.com/exchange/services/2006/types}MessageType"/>
 *           &lt;element name="CalendarItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}CalendarItemType"/>
 *           &lt;element name="Contact" type="{http://schemas.microsoft.com/exchange/services/2006/types}ContactItemType"/>
 *           &lt;element name="DistributionList" type="{http://schemas.microsoft.com/exchange/services/2006/types}DistributionListType"/>
 *           &lt;element name="MeetingMessage" type="{http://schemas.microsoft.com/exchange/services/2006/types}MeetingMessageType"/>
 *           &lt;element name="MeetingRequest" type="{http://schemas.microsoft.com/exchange/services/2006/types}MeetingRequestMessageType"/>
 *           &lt;element name="MeetingResponse" type="{http://schemas.microsoft.com/exchange/services/2006/types}MeetingResponseMessageType"/>
 *           &lt;element name="MeetingCancellation" type="{http://schemas.microsoft.com/exchange/services/2006/types}MeetingCancellationMessageType"/>
 *           &lt;element name="Task" type="{http://schemas.microsoft.com/exchange/services/2006/types}TaskType"/>
 *           &lt;element name="ReplyToItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}ReplyToItemType"/>
 *           &lt;element name="ForwardItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}ForwardItemType"/>
 *           &lt;element name="ReplyAllToItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}ReplyAllToItemType"/>
 *           &lt;element name="AcceptItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}AcceptItemType"/>
 *           &lt;element name="TentativelyAcceptItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}TentativelyAcceptItemType"/>
 *           &lt;element name="DeclineItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}DeclineItemType"/>
 *           &lt;element name="CancelCalendarItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}CancelCalendarItemType"/>
 *           &lt;element name="RemoveItem" type="{http://schemas.microsoft.com/exchange/services/2006/types}RemoveItemType"/>
 *           &lt;element name="SuppressReadReceipt" type="{http://schemas.microsoft.com/exchange/services/2006/types}SuppressReadReceiptType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NonEmptyArrayOfAllItemsType", propOrder = {
    "itemOrMessageOrCalendarItem"
})
public class NonEmptyArrayOfAllItemsType {

    @XmlElements({
        @XmlElement(name = "SuppressReadReceipt", type = SuppressReadReceiptType.class),
        @XmlElement(name = "Message", type = MessageType.class),
        @XmlElement(name = "MeetingMessage", type = MeetingMessageType.class),
        @XmlElement(name = "MeetingResponse", type = MeetingResponseMessageType.class),
        @XmlElement(name = "Contact", type = ContactItemType.class),
        @XmlElement(name = "Item"),
        @XmlElement(name = "AcceptItem", type = AcceptItemType.class),
        @XmlElement(name = "DistributionList", type = DistributionListType.class),
        @XmlElement(name = "MeetingRequest", type = MeetingRequestMessageType.class),
        @XmlElement(name = "DeclineItem", type = DeclineItemType.class),
        @XmlElement(name = "Task", type = TaskType.class),
        @XmlElement(name = "TentativelyAcceptItem", type = TentativelyAcceptItemType.class),
        @XmlElement(name = "MeetingCancellation", type = MeetingCancellationMessageType.class),
        @XmlElement(name = "CalendarItem", type = CalendarItemType.class),
        @XmlElement(name = "ReplyToItem", type = ReplyToItemType.class),
        @XmlElement(name = "ReplyAllToItem", type = ReplyAllToItemType.class),
        @XmlElement(name = "RemoveItem", type = RemoveItemType.class),
        @XmlElement(name = "CancelCalendarItem", type = CancelCalendarItemType.class),
        @XmlElement(name = "ForwardItem", type = ForwardItemType.class)
    })
    protected List<ItemType> itemOrMessageOrCalendarItem;

    /**
     * Gets the value of the itemOrMessageOrCalendarItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemOrMessageOrCalendarItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItemOrMessageOrCalendarItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SuppressReadReceiptType }
     * {@link MessageType }
     * {@link MeetingMessageType }
     * {@link MeetingResponseMessageType }
     * {@link ContactItemType }
     * {@link ItemType }
     * {@link AcceptItemType }
     * {@link DistributionListType }
     * {@link MeetingRequestMessageType }
     * {@link DeclineItemType }
     * {@link TaskType }
     * {@link TentativelyAcceptItemType }
     * {@link MeetingCancellationMessageType }
     * {@link CalendarItemType }
     * {@link ReplyToItemType }
     * {@link ReplyAllToItemType }
     * {@link RemoveItemType }
     * {@link CancelCalendarItemType }
     * {@link ForwardItemType }
     * 
     * 
     */
    public List<ItemType> getItemOrMessageOrCalendarItem() {
        if (itemOrMessageOrCalendarItem == null) {
            itemOrMessageOrCalendarItem = new ArrayList<ItemType>();
        }
        return this.itemOrMessageOrCalendarItem;
    }

}
