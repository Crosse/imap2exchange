
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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

 * 
 * <p>Java class for TwoOperandExpressionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TwoOperandExpressionType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}SearchExpressionType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.microsoft.com/exchange/services/2006/types}Path"/>
 *         &lt;element name="FieldURIOrConstant" type="{http://schemas.microsoft.com/exchange/services/2006/types}FieldURIOrConstantType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TwoOperandExpressionType", propOrder = {
    "path",
    "fieldURIOrConstant"
})
@XmlSeeAlso({
    IsEqualToType.class,
    IsGreaterThanOrEqualToType.class,
    IsLessThanOrEqualToType.class,
    IsLessThanType.class,
    IsGreaterThanType.class,
    IsNotEqualToType.class
})
public abstract class TwoOperandExpressionType
    extends SearchExpressionType
{

    @XmlElementRef(name = "Path", namespace = "http://schemas.microsoft.com/exchange/services/2006/types", type = JAXBElement.class)
    protected JAXBElement<? extends BasePathToElementType> path;
    @XmlElement(name = "FieldURIOrConstant", required = true)
    protected FieldURIOrConstantType fieldURIOrConstant;

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PathToExceptionFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PathToExtendedFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PathToIndexedFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PathToUnindexedFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasePathToElementType }{@code >}
     *     
     */
    public JAXBElement<? extends BasePathToElementType> getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PathToExceptionFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PathToExtendedFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PathToIndexedFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PathToUnindexedFieldType }{@code >}
     *     {@link JAXBElement }{@code <}{@link BasePathToElementType }{@code >}
     *     
     */
    public void setPath(JAXBElement<? extends BasePathToElementType> value) {
        this.path = ((JAXBElement<? extends BasePathToElementType> ) value);
    }

    /**
     * Gets the value of the fieldURIOrConstant property.
     * 
     * @return
     *     possible object is
     *     {@link FieldURIOrConstantType }
     *     
     */
    public FieldURIOrConstantType getFieldURIOrConstant() {
        return fieldURIOrConstant;
    }

    /**
     * Sets the value of the fieldURIOrConstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldURIOrConstantType }
     *     
     */
    public void setFieldURIOrConstant(FieldURIOrConstantType value) {
        this.fieldURIOrConstant = value;
    }

}
