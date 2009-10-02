
package com.microsoft.schemas.exchange.services._2006.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas.exchange.services._2006.types.CalendarItemUpdateOperationType;
import com.microsoft.schemas.exchange.services._2006.types.ConflictResolutionType;
import com.microsoft.schemas.exchange.services._2006.types.MessageDispositionType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfItemChangesType;
import com.microsoft.schemas.exchange.services._2006.types.TargetFolderIdType;


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
 * <p>Java class for UpdateItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateItemType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element name="SavedItemFolderId" type="{http://schemas.microsoft.com/exchange/services/2006/types}TargetFolderIdType" minOccurs="0"/>
 *         &lt;element name="ItemChanges" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyArrayOfItemChangesType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ConflictResolution" use="required" type="{http://schemas.microsoft.com/exchange/services/2006/types}ConflictResolutionType" />
 *       &lt;attribute name="MessageDisposition" type="{http://schemas.microsoft.com/exchange/services/2006/types}MessageDispositionType" />
 *       &lt;attribute name="SendMeetingInvitationsOrCancellations" type="{http://schemas.microsoft.com/exchange/services/2006/types}CalendarItemUpdateOperationType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateItemType", propOrder = {
    "savedItemFolderId",
    "itemChanges"
})
public class UpdateItemType
    extends BaseRequestType
{

    @XmlElement(name = "SavedItemFolderId")
    protected TargetFolderIdType savedItemFolderId;
    @XmlElement(name = "ItemChanges", required = true)
    protected NonEmptyArrayOfItemChangesType itemChanges;
    @XmlAttribute(name = "ConflictResolution", required = true)
    protected ConflictResolutionType conflictResolution;
    @XmlAttribute(name = "MessageDisposition")
    protected MessageDispositionType messageDisposition;
    @XmlAttribute(name = "SendMeetingInvitationsOrCancellations")
    protected CalendarItemUpdateOperationType sendMeetingInvitationsOrCancellations;

    /**
     * Gets the value of the savedItemFolderId property.
     * 
     * @return
     *     possible object is
     *     {@link TargetFolderIdType }
     *     
     */
    public TargetFolderIdType getSavedItemFolderId() {
        return savedItemFolderId;
    }

    /**
     * Sets the value of the savedItemFolderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetFolderIdType }
     *     
     */
    public void setSavedItemFolderId(TargetFolderIdType value) {
        this.savedItemFolderId = value;
    }

    /**
     * Gets the value of the itemChanges property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyArrayOfItemChangesType }
     *     
     */
    public NonEmptyArrayOfItemChangesType getItemChanges() {
        return itemChanges;
    }

    /**
     * Sets the value of the itemChanges property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyArrayOfItemChangesType }
     *     
     */
    public void setItemChanges(NonEmptyArrayOfItemChangesType value) {
        this.itemChanges = value;
    }

    /**
     * Gets the value of the conflictResolution property.
     * 
     * @return
     *     possible object is
     *     {@link ConflictResolutionType }
     *     
     */
    public ConflictResolutionType getConflictResolution() {
        return conflictResolution;
    }

    /**
     * Sets the value of the conflictResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConflictResolutionType }
     *     
     */
    public void setConflictResolution(ConflictResolutionType value) {
        this.conflictResolution = value;
    }

    /**
     * Gets the value of the messageDisposition property.
     * 
     * @return
     *     possible object is
     *     {@link MessageDispositionType }
     *     
     */
    public MessageDispositionType getMessageDisposition() {
        return messageDisposition;
    }

    /**
     * Sets the value of the messageDisposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageDispositionType }
     *     
     */
    public void setMessageDisposition(MessageDispositionType value) {
        this.messageDisposition = value;
    }

    /**
     * Gets the value of the sendMeetingInvitationsOrCancellations property.
     * 
     * @return
     *     possible object is
     *     {@link CalendarItemUpdateOperationType }
     *     
     */
    public CalendarItemUpdateOperationType getSendMeetingInvitationsOrCancellations() {
        return sendMeetingInvitationsOrCancellations;
    }

    /**
     * Sets the value of the sendMeetingInvitationsOrCancellations property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalendarItemUpdateOperationType }
     *     
     */
    public void setSendMeetingInvitationsOrCancellations(CalendarItemUpdateOperationType value) {
        this.sendMeetingInvitationsOrCancellations = value;
    }

}
