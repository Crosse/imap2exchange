
package com.microsoft.schemas.exchange.services._2006.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         Represents an extended property.  Note that there are only a couple of valid attribute
 *         combinations.  Note that all occurances require the PropertyType attribute.
 * 
 *         1.  (DistinguishedPropertySetId || PropertySetId) + (PropertyName || Property Id)
 *         2.  PropertyTag
 * 
 *       
 * 
 * <p>Java class for PathToExtendedFieldType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PathToExtendedFieldType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}BasePathToElementType">
 *       &lt;attribute name="DistinguishedPropertySetId" type="{http://schemas.microsoft.com/exchange/services/2006/types}DistinguishedPropertySetType" />
 *       &lt;attribute name="PropertySetId" type="{http://schemas.microsoft.com/exchange/services/2006/types}GuidType" />
 *       &lt;attribute name="PropertyTag" type="{http://schemas.microsoft.com/exchange/services/2006/types}PropertyTagType" />
 *       &lt;attribute name="PropertyName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="PropertyId" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="PropertyType" use="required" type="{http://schemas.microsoft.com/exchange/services/2006/types}MapiPropertyTypeType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PathToExtendedFieldType")
public class PathToExtendedFieldType
    extends BasePathToElementType
{

    @XmlAttribute(name = "DistinguishedPropertySetId")
    protected DistinguishedPropertySetType distinguishedPropertySetId;
    @XmlAttribute(name = "PropertySetId")
    protected String propertySetId;
    @XmlAttribute(name = "PropertyTag")
    protected String propertyTag;
    @XmlAttribute(name = "PropertyName")
    protected String propertyName;
    @XmlAttribute(name = "PropertyId")
    protected Integer propertyId;
    @XmlAttribute(name = "PropertyType", required = true)
    protected MapiPropertyTypeType propertyType;

    /**
     * Gets the value of the distinguishedPropertySetId property.
     * 
     * @return
     *     possible object is
     *     {@link DistinguishedPropertySetType }
     *     
     */
    public DistinguishedPropertySetType getDistinguishedPropertySetId() {
        return distinguishedPropertySetId;
    }

    /**
     * Sets the value of the distinguishedPropertySetId property.
     * 
     * @param value
     *     allowed object is
     *     {@link DistinguishedPropertySetType }
     *     
     */
    public void setDistinguishedPropertySetId(DistinguishedPropertySetType value) {
        this.distinguishedPropertySetId = value;
    }

    /**
     * Gets the value of the propertySetId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertySetId() {
        return propertySetId;
    }

    /**
     * Sets the value of the propertySetId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertySetId(String value) {
        this.propertySetId = value;
    }

    /**
     * Gets the value of the propertyTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyTag() {
        return propertyTag;
    }

    /**
     * Sets the value of the propertyTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyTag(String value) {
        this.propertyTag = value;
    }

    /**
     * Gets the value of the propertyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Sets the value of the propertyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropertyName(String value) {
        this.propertyName = value;
    }

    /**
     * Gets the value of the propertyId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPropertyId() {
        return propertyId;
    }

    /**
     * Sets the value of the propertyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPropertyId(Integer value) {
        this.propertyId = value;
    }

    /**
     * Gets the value of the propertyType property.
     * 
     * @return
     *     possible object is
     *     {@link MapiPropertyTypeType }
     *     
     */
    public MapiPropertyTypeType getPropertyType() {
        return propertyType;
    }

    /**
     * Sets the value of the propertyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MapiPropertyTypeType }
     *     
     */
    public void setPropertyType(MapiPropertyTypeType value) {
        this.propertyType = value;
    }

}
