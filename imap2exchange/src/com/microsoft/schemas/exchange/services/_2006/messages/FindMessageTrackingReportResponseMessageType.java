
package com.microsoft.schemas.exchange.services._2006.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas.exchange.services._2006.types.ArrayOfFindMessageTrackingSearchResultType;
import com.microsoft.schemas.exchange.services._2006.types.ArrayOfStringsType;


/**
 * <p>Java class for FindMessageTrackingReportResponseMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindMessageTrackingReportResponseMessageType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}ResponseMessageType">
 *       &lt;sequence>
 *         &lt;element name="Diagnostics" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfStringsType" minOccurs="0"/>
 *         &lt;element name="MessageTrackingSearchResults" type="{http://schemas.microsoft.com/exchange/services/2006/types}ArrayOfFindMessageTrackingSearchResultType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindMessageTrackingReportResponseMessageType", propOrder = {
    "diagnostics",
    "messageTrackingSearchResults"
})
public class FindMessageTrackingReportResponseMessageType
    extends ResponseMessageType
{

    @XmlElement(name = "Diagnostics")
    protected ArrayOfStringsType diagnostics;
    @XmlElement(name = "MessageTrackingSearchResults", required = true)
    protected ArrayOfFindMessageTrackingSearchResultType messageTrackingSearchResults;

    /**
     * Gets the value of the diagnostics property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfStringsType }
     *     
     */
    public ArrayOfStringsType getDiagnostics() {
        return diagnostics;
    }

    /**
     * Sets the value of the diagnostics property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfStringsType }
     *     
     */
    public void setDiagnostics(ArrayOfStringsType value) {
        this.diagnostics = value;
    }

    /**
     * Gets the value of the messageTrackingSearchResults property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFindMessageTrackingSearchResultType }
     *     
     */
    public ArrayOfFindMessageTrackingSearchResultType getMessageTrackingSearchResults() {
        return messageTrackingSearchResults;
    }

    /**
     * Sets the value of the messageTrackingSearchResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFindMessageTrackingSearchResultType }
     *     
     */
    public void setMessageTrackingSearchResults(ArrayOfFindMessageTrackingSearchResultType value) {
        this.messageTrackingSearchResults = value;
    }

}
