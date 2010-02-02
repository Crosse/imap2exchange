package edu.yale.its.tp.email.conversion.exchange.flags;

import java.util.*;
import java.util.List;
import javax.xml.bind.JAXBElement;
import edu.yale.its.tp.email.conversion.exchange.*;
import com.microsoft.schemas.exchange.services._2006.types.*;

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
public class PrMessageFlags extends Exchange16BitFlags{


	//		Read:           0x00000001(1)
	public static final Exchange16BitFlag MSGFLAG_READ            = new Exchange16BitFlag("MSGFLAG_READ", 1);
	//		Unmodified:     0x00000002(2)
	public static final Exchange16BitFlag MSGFLAG_UNMODIFIED      = new Exchange16BitFlag("MSGFLAG_UNMODIFIED", 2);
	//		Submit:         0x00000004(4)
	public static final Exchange16BitFlag MSGFLAG_SUBMIT          = new Exchange16BitFlag("MSGFLAG_SUBMIT", 4);
	//		Unsent:         0x00000008(8)
	public static final Exchange16BitFlag MSGFLAG_UNSENT          = new Exchange16BitFlag("MSGFLAG_UNSENT", 8);
	//		HasAttach:      0x00000010(16)
	public static final Exchange16BitFlag MSGFLAG_HASATTACH       = new Exchange16BitFlag("MSGFLAG_HASATTACH", 16);
	//		FromMe:         0x00000020(32)
	public static final Exchange16BitFlag MSGFLAG_FROMME          = new Exchange16BitFlag("MSGFLAG_FROMME", 32);
	//		Associated:     0x00000040(64)
	public static final Exchange16BitFlag MSGFLAG_ASSOCIATED      = new Exchange16BitFlag("MSGFLAG_ASSOCIATED", 64);
	//		ReSend:         0x00000080(128)
	public static final Exchange16BitFlag MSGFLAG_RESEND          = new Exchange16BitFlag("MSGFLAG_RESEND", 128);
	//		RNPending:      0x00000100(256)
	public static final Exchange16BitFlag MSGFLAG_RN_PENDING      = new Exchange16BitFlag("MSGFLAG_RN_PENDING", 256);
	//		NRNPending:     0x00000200(512)
	public static final Exchange16BitFlag MSGFLAG_NRN_PENDING     = new Exchange16BitFlag("MSGFLAG_NRN_PENDING", 512);
	/* The only Docs I found on this were missing three flags...
	 * 1024, 2048, and 16384 */
	//		OriginX400:     0x00001000(4096)
	public static final Exchange16BitFlag MSGFLAG_ORIGIN_X400     = new Exchange16BitFlag("MSGFLAG_ORIGIN_X400", 4096);
	//		OriginInternet: 0x00002000(8192)
	public static final Exchange16BitFlag MSGFLAG_ORIGIN_INTERNET = new Exchange16BitFlag("MSGFLAG_ORIGIN_INTERNET", 8192);
	//		OriginMiscExt:  0x00008000(32768)		
	public static final Exchange16BitFlag MSGFLAG_ORIGIN_MISC_EXT = new Exchange16BitFlag("MSGFLAG_ORIGIN_MISC_EXT", 32768);

	public static final JAXBElement<? extends BasePathToElementType> PR_MESSAGE_FLAGS_PATH;
	public static final PathToExtendedFieldType PR_MESSAGE_FLAGS_URI = new PathToExtendedFieldType();
	public static final String PR_MESSAGE_FLAGS_TAG = "0xe07";
	private static final List<ExchangeBitFlag> flags;
	
	static{

		PR_MESSAGE_FLAGS_URI.setPropertyTag(PR_MESSAGE_FLAGS_TAG);
		PR_MESSAGE_FLAGS_URI.setPropertyType(MapiPropertyTypeType.INTEGER);
		PR_MESSAGE_FLAGS_PATH = MessageUtil.typesObjectFactory.createExtendedFieldURI(PR_MESSAGE_FLAGS_URI);
		
		List<ExchangeBitFlag> f =  new ArrayList<ExchangeBitFlag>();
		f.add(MSGFLAG_READ); 
		f.add(MSGFLAG_UNMODIFIED);
		f.add(MSGFLAG_SUBMIT);
		f.add(MSGFLAG_UNSENT);
		f.add(MSGFLAG_HASATTACH);
		f.add(MSGFLAG_FROMME);
		f.add(MSGFLAG_ASSOCIATED);
		f.add(MSGFLAG_RESEND);
		f.add(MSGFLAG_RN_PENDING);
		f.add(MSGFLAG_NRN_PENDING);
		f.add(MSGFLAG_ORIGIN_X400);
		f.add(MSGFLAG_ORIGIN_INTERNET);
		f.add(MSGFLAG_ORIGIN_MISC_EXT);
		flags = Collections.unmodifiableList(f);
	}
	
	public List<ExchangeBitFlag> getFlags(){
		return flags;
	}
	

}
