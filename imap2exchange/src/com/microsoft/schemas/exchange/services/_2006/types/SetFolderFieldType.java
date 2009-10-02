
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
 * <p>Java class for SetFolderFieldType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SetFolderFieldType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}FolderChangeDescriptionType">
 *       &lt;choice>
 *         &lt;element name="Folder" type="{http://schemas.microsoft.com/exchange/services/2006/types}FolderType"/>
 *         &lt;element name="CalendarFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}CalendarFolderType"/>
 *         &lt;element name="ContactsFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}ContactsFolderType"/>
 *         &lt;element name="SearchFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}SearchFolderType"/>
 *         &lt;element name="TasksFolder" type="{http://schemas.microsoft.com/exchange/services/2006/types}TasksFolderType"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SetFolderFieldType", propOrder = {
    "folder",
    "calendarFolder",
    "contactsFolder",
    "searchFolder",
    "tasksFolder"
})
public class SetFolderFieldType
    extends FolderChangeDescriptionType
{

    @XmlElement(name = "Folder")
    protected FolderType folder;
    @XmlElement(name = "CalendarFolder")
    protected CalendarFolderType calendarFolder;
    @XmlElement(name = "ContactsFolder")
    protected ContactsFolderType contactsFolder;
    @XmlElement(name = "SearchFolder")
    protected SearchFolderType searchFolder;
    @XmlElement(name = "TasksFolder")
    protected TasksFolderType tasksFolder;

    /**
     * Gets the value of the folder property.
     * 
     * @return
     *     possible object is
     *     {@link FolderType }
     *     
     */
    public FolderType getFolder() {
        return folder;
    }

    /**
     * Sets the value of the folder property.
     * 
     * @param value
     *     allowed object is
     *     {@link FolderType }
     *     
     */
    public void setFolder(FolderType value) {
        this.folder = value;
    }

    /**
     * Gets the value of the calendarFolder property.
     * 
     * @return
     *     possible object is
     *     {@link CalendarFolderType }
     *     
     */
    public CalendarFolderType getCalendarFolder() {
        return calendarFolder;
    }

    /**
     * Sets the value of the calendarFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link CalendarFolderType }
     *     
     */
    public void setCalendarFolder(CalendarFolderType value) {
        this.calendarFolder = value;
    }

    /**
     * Gets the value of the contactsFolder property.
     * 
     * @return
     *     possible object is
     *     {@link ContactsFolderType }
     *     
     */
    public ContactsFolderType getContactsFolder() {
        return contactsFolder;
    }

    /**
     * Sets the value of the contactsFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactsFolderType }
     *     
     */
    public void setContactsFolder(ContactsFolderType value) {
        this.contactsFolder = value;
    }

    /**
     * Gets the value of the searchFolder property.
     * 
     * @return
     *     possible object is
     *     {@link SearchFolderType }
     *     
     */
    public SearchFolderType getSearchFolder() {
        return searchFolder;
    }

    /**
     * Sets the value of the searchFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchFolderType }
     *     
     */
    public void setSearchFolder(SearchFolderType value) {
        this.searchFolder = value;
    }

    /**
     * Gets the value of the tasksFolder property.
     * 
     * @return
     *     possible object is
     *     {@link TasksFolderType }
     *     
     */
    public TasksFolderType getTasksFolder() {
        return tasksFolder;
    }

    /**
     * Sets the value of the tasksFolder property.
     * 
     * @param value
     *     allowed object is
     *     {@link TasksFolderType }
     *     
     */
    public void setTasksFolder(TasksFolderType value) {
        this.tasksFolder = value;
    }

}
