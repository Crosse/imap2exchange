
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
 * <p>Java class for SerializableTimeZone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SerializableTimeZone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Bias" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="StandardTime" type="{http://schemas.microsoft.com/exchange/services/2006/types}SerializableTimeZoneTime"/>
 *         &lt;element name="DaylightTime" type="{http://schemas.microsoft.com/exchange/services/2006/types}SerializableTimeZoneTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SerializableTimeZone", propOrder = {
    "bias",
    "standardTime",
    "daylightTime"
})
public class SerializableTimeZone {

    @XmlElement(name = "Bias")
    protected int bias;
    @XmlElement(name = "StandardTime", required = true)
    protected SerializableTimeZoneTime standardTime;
    @XmlElement(name = "DaylightTime", required = true)
    protected SerializableTimeZoneTime daylightTime;

    /**
     * Gets the value of the bias property.
     * 
     */
    public int getBias() {
        return bias;
    }

    /**
     * Sets the value of the bias property.
     * 
     */
    public void setBias(int value) {
        this.bias = value;
    }

    /**
     * Gets the value of the standardTime property.
     * 
     * @return
     *     possible object is
     *     {@link SerializableTimeZoneTime }
     *     
     */
    public SerializableTimeZoneTime getStandardTime() {
        return standardTime;
    }

    /**
     * Sets the value of the standardTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link SerializableTimeZoneTime }
     *     
     */
    public void setStandardTime(SerializableTimeZoneTime value) {
        this.standardTime = value;
    }

    /**
     * Gets the value of the daylightTime property.
     * 
     * @return
     *     possible object is
     *     {@link SerializableTimeZoneTime }
     *     
     */
    public SerializableTimeZoneTime getDaylightTime() {
        return daylightTime;
    }

    /**
     * Sets the value of the daylightTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link SerializableTimeZoneTime }
     *     
     */
    public void setDaylightTime(SerializableTimeZoneTime value) {
        this.daylightTime = value;
    }

}
