package com.microsoft.schemas.exchange.services._2006.messages;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 * <p>Java class for ArrayOfResponseMessagesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfResponseMessagesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element name="CreateItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="DeleteItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="GetItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="UpdateItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="SendItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="DeleteFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="CreateFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="GetFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="FindFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FindFolderResponseMessageType"/>
 *         &lt;element name="UpdateFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="MoveFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="CopyFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *         &lt;element name="CreateAttachmentResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}AttachmentInfoResponseMessageType"/>
 *         &lt;element name="DeleteAttachmentResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}DeleteAttachmentResponseMessageType"/>
 *         &lt;element name="GetAttachmentResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}AttachmentInfoResponseMessageType"/>
 *         &lt;element name="FindItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FindItemResponseMessageType"/>
 *         &lt;element name="MoveItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="CopyItemResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ItemInfoResponseMessageType"/>
 *         &lt;element name="ResolveNamesResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResolveNamesResponseMessageType"/>
 *         &lt;element name="ExpandDLResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ExpandDLResponseMessageType"/>
 *         &lt;element name="GetEventsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}GetEventsResponseMessageType"/>
 *         &lt;element name="SubscribeResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SubscribeResponseMessageType"/>
 *         &lt;element name="UnsubscribeResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType"/>
 *         &lt;element name="SendNotificationResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SendNotificationResponseMessageType"/>
 *         &lt;element name="SyncFolderHierarchyResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SyncFolderHierarchyResponseMessageType"/>
 *         &lt;element name="SyncFolderItemsResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}SyncFolderItemsResponseMessageType"/>
 *         &lt;element name="CreateManagedFolderResponseMessage" type="{http://schemas.microsoft.com/exchange/services/2006/messages}FolderInfoResponseMessageType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfResponseMessagesType", propOrder = {
    "createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage"
})
public class ArrayOfResponseMessagesType {

    @XmlElementRefs({
        @XmlElementRef(name = "SendNotificationResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "UnsubscribeResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "GetEventsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "DeleteItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "DeleteFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "FindItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "MoveItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "ExpandDLResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "DeleteAttachmentResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "UpdateItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "CopyFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "SubscribeResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "SyncFolderHierarchyResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "MoveFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "CopyItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "UpdateFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "GetItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "CreateItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "SyncFolderItemsResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "GetAttachmentResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "CreateFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "ResolveNamesResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "GetFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "CreateAttachmentResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "SendItemResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "FindFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class),
        @XmlElementRef(name = "CreateManagedFolderResponseMessage", namespace = "http://schemas.microsoft.com/exchange/services/2006/messages", type = JAXBElement.class)
    })
    protected List<JAXBElement<? extends ResponseMessageType>> createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage;

    /**
     * Gets the value of the createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link SendNotificationResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link GetEventsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FindItemResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpandDLResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link DeleteAttachmentResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link SubscribeResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link SyncFolderHierarchyResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ItemInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link SyncFolderItemsResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttachmentInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResolveNamesResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link AttachmentInfoResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link ResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FindFolderResponseMessageType }{@code >}
     * {@link JAXBElement }{@code <}{@link FolderInfoResponseMessageType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends ResponseMessageType>> getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage() {
        if (createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage == null) {
            createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage = new ArrayList<JAXBElement<? extends ResponseMessageType>>();
        }
        return this.createItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage;
    }

}
