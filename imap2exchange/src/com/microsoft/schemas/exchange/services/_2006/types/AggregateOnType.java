
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 * 
 *         Represents the field of each item to aggregate on and the qualifier to apply to that
 *         field in determining which item will represent the group.
 *       
 * 
 * <p>Java class for AggregateOnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AggregateOnType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="FieldURI" type="{http://schemas.microsoft.com/exchange/services/2006/types}PathToUnindexedFieldType"/>
 *         &lt;element name="IndexedFieldURI" type="{http://schemas.microsoft.com/exchange/services/2006/types}PathToIndexedFieldType"/>
 *         &lt;element name="ExtendedFieldURI" type="{http://schemas.microsoft.com/exchange/services/2006/types}PathToExtendedFieldType"/>
 *       &lt;/choice>
 *       &lt;attribute name="Aggregate" use="required" type="{http://schemas.microsoft.com/exchange/services/2006/types}AggregateType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregateOnType", propOrder = {
    "fieldURI",
    "indexedFieldURI",
    "extendedFieldURI"
})
public class AggregateOnType {

    @XmlElement(name = "FieldURI")
    protected PathToUnindexedFieldType fieldURI;
    @XmlElement(name = "IndexedFieldURI")
    protected PathToIndexedFieldType indexedFieldURI;
    @XmlElement(name = "ExtendedFieldURI")
    protected PathToExtendedFieldType extendedFieldURI;
    @XmlAttribute(name = "Aggregate", required = true)
    protected AggregateType aggregate;

    /**
     * Gets the value of the fieldURI property.
     * 
     * @return
     *     possible object is
     *     {@link PathToUnindexedFieldType }
     *     
     */
    public PathToUnindexedFieldType getFieldURI() {
        return fieldURI;
    }

    /**
     * Sets the value of the fieldURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathToUnindexedFieldType }
     *     
     */
    public void setFieldURI(PathToUnindexedFieldType value) {
        this.fieldURI = value;
    }

    /**
     * Gets the value of the indexedFieldURI property.
     * 
     * @return
     *     possible object is
     *     {@link PathToIndexedFieldType }
     *     
     */
    public PathToIndexedFieldType getIndexedFieldURI() {
        return indexedFieldURI;
    }

    /**
     * Sets the value of the indexedFieldURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathToIndexedFieldType }
     *     
     */
    public void setIndexedFieldURI(PathToIndexedFieldType value) {
        this.indexedFieldURI = value;
    }

    /**
     * Gets the value of the extendedFieldURI property.
     * 
     * @return
     *     possible object is
     *     {@link PathToExtendedFieldType }
     *     
     */
    public PathToExtendedFieldType getExtendedFieldURI() {
        return extendedFieldURI;
    }

    /**
     * Sets the value of the extendedFieldURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link PathToExtendedFieldType }
     *     
     */
    public void setExtendedFieldURI(PathToExtendedFieldType value) {
        this.extendedFieldURI = value;
    }

    /**
     * Gets the value of the aggregate property.
     * 
     * @return
     *     possible object is
     *     {@link AggregateType }
     *     
     */
    public AggregateType getAggregate() {
        return aggregate;
    }

    /**
     * Sets the value of the aggregate property.
     * 
     * @param value
     *     allowed object is
     *     {@link AggregateType }
     *     
     */
    public void setAggregate(AggregateType value) {
        this.aggregate = value;
    }

}
