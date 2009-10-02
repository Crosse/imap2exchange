
package com.microsoft.schemas.exchange.services._2006.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.microsoft.schemas.exchange.services._2006.types.FolderQueryTraversalType;
import com.microsoft.schemas.exchange.services._2006.types.FolderResponseShapeType;
import com.microsoft.schemas.exchange.services._2006.types.FractionalPageViewType;
import com.microsoft.schemas.exchange.services._2006.types.IndexedPageViewType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfBaseFolderIdsType;
import com.microsoft.schemas.exchange.services._2006.types.RestrictionType;


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
 * <p>Java class for FindFolderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FindFolderType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/messages}BaseRequestType">
 *       &lt;sequence>
 *         &lt;element name="FolderShape" type="{http://schemas.microsoft.com/exchange/services/2006/types}FolderResponseShapeType"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="IndexedPageFolderView" type="{http://schemas.microsoft.com/exchange/services/2006/types}IndexedPageViewType"/>
 *           &lt;element name="FractionalPageFolderView" type="{http://schemas.microsoft.com/exchange/services/2006/types}FractionalPageViewType"/>
 *         &lt;/choice>
 *         &lt;element name="Restriction" type="{http://schemas.microsoft.com/exchange/services/2006/types}RestrictionType" minOccurs="0"/>
 *         &lt;element name="ParentFolderIds" type="{http://schemas.microsoft.com/exchange/services/2006/types}NonEmptyArrayOfBaseFolderIdsType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Traversal" use="required" type="{http://schemas.microsoft.com/exchange/services/2006/types}FolderQueryTraversalType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindFolderType", propOrder = {
    "folderShape",
    "indexedPageFolderView",
    "fractionalPageFolderView",
    "restriction",
    "parentFolderIds"
})
public class FindFolderType
    extends BaseRequestType
{

    @XmlElement(name = "FolderShape", required = true)
    protected FolderResponseShapeType folderShape;
    @XmlElement(name = "IndexedPageFolderView")
    protected IndexedPageViewType indexedPageFolderView;
    @XmlElement(name = "FractionalPageFolderView")
    protected FractionalPageViewType fractionalPageFolderView;
    @XmlElement(name = "Restriction")
    protected RestrictionType restriction;
    @XmlElement(name = "ParentFolderIds", required = true)
    protected NonEmptyArrayOfBaseFolderIdsType parentFolderIds;
    @XmlAttribute(name = "Traversal", required = true)
    protected FolderQueryTraversalType traversal;

    /**
     * Gets the value of the folderShape property.
     * 
     * @return
     *     possible object is
     *     {@link FolderResponseShapeType }
     *     
     */
    public FolderResponseShapeType getFolderShape() {
        return folderShape;
    }

    /**
     * Sets the value of the folderShape property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderResponseShapeType }
     *     
     */
    public void setFolderShape(FolderResponseShapeType value) {
        this.folderShape = value;
    }

    /**
     * Gets the value of the indexedPageFolderView property.
     * 
     * @return
     *     possible object is
     *     {@link IndexedPageViewType }
     *     
     */
    public IndexedPageViewType getIndexedPageFolderView() {
        return indexedPageFolderView;
    }

    /**
     * Sets the value of the indexedPageFolderView property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndexedPageViewType }
     *     
     */
    public void setIndexedPageFolderView(IndexedPageViewType value) {
        this.indexedPageFolderView = value;
    }

    /**
     * Gets the value of the fractionalPageFolderView property.
     * 
     * @return
     *     possible object is
     *     {@link FractionalPageViewType }
     *     
     */
    public FractionalPageViewType getFractionalPageFolderView() {
        return fractionalPageFolderView;
    }

    /**
     * Sets the value of the fractionalPageFolderView property.
     * 
     * @param value
     *     allowed object is
     *     {@link FractionalPageViewType }
     *     
     */
    public void setFractionalPageFolderView(FractionalPageViewType value) {
        this.fractionalPageFolderView = value;
    }

    /**
     * Gets the value of the restriction property.
     * 
     * @return
     *     possible object is
     *     {@link RestrictionType }
     *     
     */
    public RestrictionType getRestriction() {
        return restriction;
    }

    /**
     * Sets the value of the restriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestrictionType }
     *     
     */
    public void setRestriction(RestrictionType value) {
        this.restriction = value;
    }

    /**
     * Gets the value of the parentFolderIds property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyArrayOfBaseFolderIdsType }
     *     
     */
    public NonEmptyArrayOfBaseFolderIdsType getParentFolderIds() {
        return parentFolderIds;
    }

    /**
     * Sets the value of the parentFolderIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyArrayOfBaseFolderIdsType }
     *     
     */
    public void setParentFolderIds(NonEmptyArrayOfBaseFolderIdsType value) {
        this.parentFolderIds = value;
    }

    /**
     * Gets the value of the traversal property.
     * 
     * @return
     *     possible object is
     *     {@link FolderQueryTraversalType }
     *     
     */
    public FolderQueryTraversalType getTraversal() {
        return traversal;
    }

    /**
     * Sets the value of the traversal property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderQueryTraversalType }
     *     
     */
    public void setTraversal(FolderQueryTraversalType value) {
        this.traversal = value;
    }

}
