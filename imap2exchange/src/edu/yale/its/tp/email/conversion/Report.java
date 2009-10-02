/**
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
 *</pre>
 *
 */
package edu.yale.its.tp.email.conversion;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author jjv6
 *
 */
public class Report {
	
	private static final Log logger = LogFactory.getLog(Report.class);
	
	Map<String, Timer> timers = new TreeMap<String, Timer>();
	
	private static final String LF = "\n";
	private static final String PAD= "    ";
	
	public static final String IMAP_META = "IMAP MetaData Calls";
	public static final String IMAP_MIME = "IMAP Full Mime Message Calls";
	
	public static final String EXCHANGE_CONNECT = "Exchange Connect";
	public static final String EXCHANGE_META = "Exchange MetaData Calls";
	public static final String EXCHANGE_MIME = "Exchange Message Delivery";
	
	public class Timer {
		
		String name;
		
		long ts;
		
		long cnt = 0;
		long max = 0;
		long min = Long.MAX_VALUE;
		long total = 0;
		
		boolean started = false;
		
		public void start(){
			if(started){
				logger.warn("Timer [" + name + "] can not start, already started.");
				return;
			}
			started = true;
			ts = System.currentTimeMillis();
			cnt ++;
		}
		
		public void stop(){
			if(!started){
				logger.warn("Timer [" + name + "]is already Stopped.");
				return;
			}				
			long dur = System.currentTimeMillis() - ts;
			if(dur > max) max = dur;
			if(dur < min) min = dur;
			total += dur;
			started = false;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
	}
	
	public boolean isStarted(String timer){
		Timer t = timers.get(timer);
		if(t == null){
			logger.warn("isStarted? Timer [" + timer + "was not found.");
			return false;
		}
		return t.started;		
	}
	
	public void start(String timer){
		Timer t = timers.get(timer);
		if(t == null){
			t = new Timer();
			t.setName(timer);
			timers.put(timer, t);
		} 
		t.start();		
	}
	
	public void stop(String timer){
		Timer t = timers.get(timer);
		if(t == null){
			logger.warn("Timer [" + timer + "was not found.");
			return;
		}
		t.stop();		
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(String tName : timers.keySet()){
			Timer t = timers.get(tName);
			sb.append(tName).append(LF);
			sb.append(PAD).append("cnt:   ").append(t.cnt).append(LF);
			sb.append(PAD).append("max:   ").append(t.max).append(LF);
			sb.append(PAD).append("min:   ").append(t.min).append(LF);
			sb.append(PAD).append("total: ").append(t.total).append(LF);
		}
		return sb.toString();
	}
	

}
