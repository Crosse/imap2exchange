package edu.yale.its.tp.email.conversion.exchange.flags;

import java.util.List;
//import org.apache.log4j.*;

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
public abstract class ExchangeBitFlags {
	
//	private static Logger logger = Logger.getLogger(ExchangeBitFlags.class);
	
	private static final char TRUE  = '1';
	private static final char FALSE = '0';
	
	protected char[] flagBits = new char[getBits()];
	
	public ExchangeBitFlags(){
		clear();
	}
	
	public abstract List<ExchangeBitFlag> getFlags();
	public abstract int getBits();

	private void clear(){
		for(int i=0; i< getBits(); i++){
			flagBits[i] = FALSE;
		}
	}
	
	public void add(ExchangeBitFlag flag){
		validate(flag);
		flagBits[flag.index()] = TRUE;
	}
	
	public void remove(ExchangeBitFlag flag){
		validate(flag);
		flagBits[flag.index()] = FALSE;
	}

	public boolean isOn(ExchangeBitFlag flag){
		validate(flag);
		return flagBits[flag.index()] == TRUE;
	}
	
	public void setFlags(long newFlags){
		clear();
		char[] tempFlags = Long.toString(newFlags, 2).toCharArray();
		for(int i=0; i<tempFlags.length && i<getBits(); i++){
			flagBits[getBits() - 1 - i] = tempFlags[tempFlags.length -1 - i];
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(ExchangeBitFlag flag : this.getFlags()){
			if(flagBits[flag.index()] == TRUE)
				sb.append(flag.getName()).append(", ");
		}
		if(sb.length()>1){
			sb.replace(sb.length()-2, sb.length()-1, "]");
		} else {
			sb.append("]");
		}
		return sb.toString();
	}
	
	public String toBitString(){
		return new String(flagBits);
	}
	
	public void validate(ExchangeBitFlag flag){
		if(!getFlags().contains(flag)){
			throw new IllegalArgumentException("Invalid Flag type for " + this.getClass().getName());
		}
	}


}
