
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
 * <p>Java class for GroupAttendeeConflictData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroupAttendeeConflictData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/exchange/services/2006/types}AttendeeConflictData">
 *       &lt;sequence>
 *         &lt;element name="NumberOfMembers" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NumberOfMembersAvailable" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NumberOfMembersWithConflict" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="NumberOfMembersWithNoData" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupAttendeeConflictData", propOrder = {
    "numberOfMembers",
    "numberOfMembersAvailable",
    "numberOfMembersWithConflict",
    "numberOfMembersWithNoData"
})
public class GroupAttendeeConflictData
    extends AttendeeConflictData
{

    @XmlElement(name = "NumberOfMembers")
    protected int numberOfMembers;
    @XmlElement(name = "NumberOfMembersAvailable")
    protected int numberOfMembersAvailable;
    @XmlElement(name = "NumberOfMembersWithConflict")
    protected int numberOfMembersWithConflict;
    @XmlElement(name = "NumberOfMembersWithNoData")
    protected int numberOfMembersWithNoData;

    /**
     * Gets the value of the numberOfMembers property.
     * 
     */
    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    /**
     * Sets the value of the numberOfMembers property.
     * 
     */
    public void setNumberOfMembers(int value) {
        this.numberOfMembers = value;
    }

    /**
     * Gets the value of the numberOfMembersAvailable property.
     * 
     */
    public int getNumberOfMembersAvailable() {
        return numberOfMembersAvailable;
    }

    /**
     * Sets the value of the numberOfMembersAvailable property.
     * 
     */
    public void setNumberOfMembersAvailable(int value) {
        this.numberOfMembersAvailable = value;
    }

    /**
     * Gets the value of the numberOfMembersWithConflict property.
     * 
     */
    public int getNumberOfMembersWithConflict() {
        return numberOfMembersWithConflict;
    }

    /**
     * Sets the value of the numberOfMembersWithConflict property.
     * 
     */
    public void setNumberOfMembersWithConflict(int value) {
        this.numberOfMembersWithConflict = value;
    }

    /**
     * Gets the value of the numberOfMembersWithNoData property.
     * 
     */
    public int getNumberOfMembersWithNoData() {
        return numberOfMembersWithNoData;
    }

    /**
     * Sets the value of the numberOfMembersWithNoData property.
     * 
     */
    public void setNumberOfMembersWithNoData(int value) {
        this.numberOfMembersWithNoData = value;
    }

}
