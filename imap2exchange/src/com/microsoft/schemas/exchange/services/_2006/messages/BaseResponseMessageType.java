
package com.microsoft.schemas.exchange.services._2006.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
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
 * <p>Java class for BaseResponseMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseResponseMessageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResponseMessages" type="{http://schemas.microsoft.com/exchange/services/2006/messages}ArrayOfResponseMessagesType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseResponseMessageType", propOrder = {
    "responseMessages"
})
@XmlSeeAlso({
    MoveFolderResponseType.class,
    CreateAttachmentResponseType.class,
    DeleteFolderResponseType.class,
    SendNotificationResponseType.class,
    UnsubscribeResponseType.class,
    SubscribeResponseType.class,
    SendItemResponseType.class,
    GetItemResponseType.class,
    GetAttachmentResponseType.class,
    GetEventsResponseType.class,
    CreateManagedFolderResponseType.class,
    SyncFolderHierarchyResponseType.class,
    UpdateItemResponseType.class,
    FindItemResponseType.class,
    CopyItemResponseType.class,
    SyncFolderItemsResponseType.class,
    CopyFolderResponseType.class,
    UpdateFolderResponseType.class,
    MoveItemResponseType.class,
    CreateItemResponseType.class,
    GetFolderResponseType.class,
    DeleteAttachmentResponseType.class,
    CreateFolderResponseType.class,
    ExpandDLResponseType.class,
    DeleteItemResponseType.class,
    ResolveNamesResponseType.class,
    FindFolderResponseType.class
})
public class BaseResponseMessageType {

    @XmlElement(name = "ResponseMessages", required = true)
    protected ArrayOfResponseMessagesType responseMessages;

    /**
     * Gets the value of the responseMessages property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfResponseMessagesType }
     *     
     */
    public ArrayOfResponseMessagesType getResponseMessages() {
        return responseMessages;
    }

    /**
     * Sets the value of the responseMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfResponseMessagesType }
     *     
     */
    public void setResponseMessages(ArrayOfResponseMessagesType value) {
        this.responseMessages = value;
    }

}
