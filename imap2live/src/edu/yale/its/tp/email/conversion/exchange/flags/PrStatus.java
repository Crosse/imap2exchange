package edu.yale.its.tp.email.conversion.exchange.flags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBElement;
import edu.yale.its.tp.email.conversion.exchange.*;
import com.microsoft.schemas.exchange.services._2006.types.BasePathToElementType;
import com.microsoft.schemas.exchange.services._2006.types.MapiPropertyTypeType;
import com.microsoft.schemas.exchange.services._2006.types.PathToExtendedFieldType;


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
 */
public class PrStatus extends Exchange32BitFlags {

//	FLDSTATUS_HIGHLIGHTED 0x00000001
	public static final Exchange32BitFlag FLDSTATUS_HIGHLIGHTED = new Exchange32BitFlag("FLDSTATUS_HIGHLIGHTED", 1);
//	FLDSTATUS_TAGGED      0x00000002
	public static final Exchange32BitFlag FLDSTATUS_TAGGED = new Exchange32BitFlag("FLDSTATUS_TAGGED", 2);
//	FLDSTATUS_HIDDEN      0x00000004
	public static final Exchange32BitFlag FLDSTATUS_HIDDEN = new Exchange32BitFlag("FLDSTATUS_HIDDEN", 4);
//	FLDSTATUS_DELMARKED   0x00000008
	public static final Exchange32BitFlag FLDSTATUS_DELMARKED = new Exchange32BitFlag("FLDSTATUS_DELMARKED", 8);

	public static final JAXBElement<? extends BasePathToElementType> PR_STATUS_PATH;
	public static final PathToExtendedFieldType PR_STATUS_URI = new PathToExtendedFieldType();
	public static final String PR_STATUS_TAG = "0x360b";
	private static final List<ExchangeBitFlag> flags;

	static{

		PR_STATUS_URI.setPropertyTag(PR_STATUS_TAG);
		PR_STATUS_URI.setPropertyType(MapiPropertyTypeType.INTEGER);
		PR_STATUS_PATH = MessageUtil.typesObjectFactory.createExtendedFieldURI(PR_STATUS_URI);

		List<ExchangeBitFlag> f =  new ArrayList<ExchangeBitFlag>();
		f.add(FLDSTATUS_HIGHLIGHTED); 
		f.add(FLDSTATUS_TAGGED);
		f.add(FLDSTATUS_HIDDEN);
		f.add(FLDSTATUS_DELMARKED);
		flags = Collections.unmodifiableList(f);
	}
	
	public List<ExchangeBitFlag> getFlags(){
		return flags;
	}

}
