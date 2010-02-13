
package com.microsoft.schemas.exchange.services._2006.types;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for RecipientTrackingEventType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RecipientTrackingEventType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Recipient" type="{http://schemas.microsoft.com/exchange/services/2006/types}EmailAddressType"/>
 *         &lt;element name="DeliveryStatus" type="{http://schemas.microsoft.com/exchange/services/2006/types}MessageTrackingDeliveryStatusType"/>
 *         &lt;element name="EventDescription" type="{http://schemas.microsoft.com/exchange/services/2006/types}MessageTrackingEventDescriptionType"/>
 *         &lt;element name="EventData" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfStringsType" minOccurs="0"/>
 *         &lt;element name="Server" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyStringType"/>
 *         &lt;element name="InternalId" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="BccRecipient" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HiddenRecipient" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="UniquePathId" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyStringType" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecipientTrackingEventType", propOrder = {

})
public class RecipientTrackingEventType {

    @XmlElement(name = "Date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar date;
    @XmlElement(name = "Recipient", required = true)
    protected EmailAddressType recipient;
    @XmlElement(name = "DeliveryStatus", required = true)
    protected MessageTrackingDeliveryStatusType deliveryStatus;
    @XmlElement(name = "EventDescription", required = true)
    protected MessageTrackingEventDescriptionType eventDescription;
    @XmlElement(name = "EventData")
    protected ArrayOfStringsType eventData;
    @XmlElement(name = "Server", required = true)
    protected String server;
    @XmlElement(name = "InternalId", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger internalId;
    @XmlElement(name = "BccRecipient")
    protected Boolean bccRecipient;
    @XmlElement(name = "HiddenRecipient")
    protected Boolean hiddenRecipient;
    @XmlElement(name = "UniquePathId")
    protected String uniquePathId;

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the recipient property.
     * 
     * @return
     *     possible object is
     *     {@link EmailAddressType }
     *     
     */
    public EmailAddressType getRecipient() {
        return recipient;
    }

    /**
     * Sets the value of the recipient property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailAddressType }
     *     
     */
    public void setRecipient(EmailAddressType value) {
        this.recipient = value;
    }

    /**
     * Gets the value of the deliveryStatus property.
     * 
     * @return
     *     possible object is
     *     {@link MessageTrackingDeliveryStatusType }
     *     
     */
    public MessageTrackingDeliveryStatusType getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * Sets the value of the deliveryStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageTrackingDeliveryStatusType }
     *     
     */
    public void setDeliveryStatus(MessageTrackingDeliveryStatusType value) {
        this.deliveryStatus = value;
    }

    /**
     * Gets the value of the eventDescription property.
     * 
     * @return
     *     possible object is
     *     {@link MessageTrackingEventDescriptionType }
     *     
     */
    public MessageTrackingEventDescriptionType getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the value of the eventDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageTrackingEventDescriptionType }
     *     
     */
    public void setEventDescription(MessageTrackingEventDescriptionType value) {
        this.eventDescription = value;
    }

    /**
     * Gets the value of the eventData property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStringsType }
     *     
     */
    public ArrayOfStringsType getEventData() {
        return eventData;
    }

    /**
     * Sets the value of the eventData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStringsType }
     *     
     */
    public void setEventData(ArrayOfStringsType value) {
        this.eventData = value;
    }

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServer(String value) {
        this.server = value;
    }

    /**
     * Gets the value of the internalId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getInternalId() {
        return internalId;
    }

    /**
     * Sets the value of the internalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setInternalId(BigInteger value) {
        this.internalId = value;
    }

    /**
     * Gets the value of the bccRecipient property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBccRecipient() {
        return bccRecipient;
    }

    /**
     * Sets the value of the bccRecipient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBccRecipient(Boolean value) {
        this.bccRecipient = value;
    }

    /**
     * Gets the value of the hiddenRecipient property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHiddenRecipient() {
        return hiddenRecipient;
    }

    /**
     * Sets the value of the hiddenRecipient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHiddenRecipient(Boolean value) {
        this.hiddenRecipient = value;
    }

    /**
     * Gets the value of the uniquePathId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniquePathId() {
        return uniquePathId;
    }

    /**
     * Sets the value of the uniquePathId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniquePathId(String value) {
        this.uniquePathId = value;
    }

}
