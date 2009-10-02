package edu.yale.its.tp.email.conversion;

import java.util.*;
import java.text.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.*;
import edu.yale.its.tp.email.conversion.event.*;
import edu.yale.its.tp.email.conversion.util.*;

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
 * This class manages the runnable ExchangeConversions and supplies notification for defined events.
 */
public class ExchangeConversionManager  implements ExchangeConversionAddedListener 
								               , ExchangeConversionStartListener
								               , ExchangeConversionCompleteListener
								               , NoMoreConversionsListener  {
	
	private static final Log logger = LogFactory.getLog(ExchangeConversionManager.class);
	private static final Log completedLogger = LogFactory.getLog(ExchangeConversionManager.class.getName() + ".completed");
	static public final SimpleDateFormat dtFmt = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
	
	int maxRunningThreads;
   	int maxCachedThreads;
	ThreadPoolExecutor threadPool;
	                       
	// Conversions
	List<ExchangeConversion> queued = Collections.synchronizedList(new ArrayList<ExchangeConversion>());
	List<ExchangeConversion> running = Collections.synchronizedList(new ArrayList<ExchangeConversion>());
	
	// Listeners
	List<ExchangeConversionAddedListener> addedListeners = Collections.synchronizedList(new ArrayList<ExchangeConversionAddedListener>());
	List<ExchangeConversionStartListener> startListeners = Collections.synchronizedList(new ArrayList<ExchangeConversionStartListener>());
	List<ExchangeConversionCompleteListener> completeListeners = Collections.synchronizedList(new ArrayList<ExchangeConversionCompleteListener>());
	List<NoMoreConversionsListener> noMoreListeners = Collections.synchronizedList(new ArrayList<NoMoreConversionsListener>());

	static ExchangeConversionManager singleton;
	
	/**
	 * Constructor... Mostly only want to create one of these per jvm
	 */
	public ExchangeConversionManager() {
		this.addExchangeConversionAddedListener(this);
		this.addExchangeConversionStartListener(this);
		this.addExchangeConversionCompleteListener(this);
		this.addNoMoreConversionsListener(this);
		singleton = this;
	}
	
	public static ExchangeConversionManager getInstance(){
		return singleton;
	}
	
	/**
	 * Add a new Conversion to the manager for processing after notifying listeners
	 * @param conversion
	 */
	public synchronized void addConversion(ExchangeConversion conversion){
		this.notifyAddedListeners(conversion);
		if(threadPool == null) threadPool =  new ExchangeConversionPoolExecutor();
		queued.add(conversion);
		threadPool.execute(conversion);
	}
	
	// Do Notifications....
	/////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Notify Listeners of Conversion Added Event
	 */
	protected synchronized void notifyAddedListeners(ExchangeConversion conversion){
		for(ExchangeConversionAddedListener listener : addedListeners){
			listener.ExchangeConversionAdded(new ExchangeConversionEvent(conversion));
		}
	}

	/*
	 * Notify Listeners of Conversion Started Event
	 */
	protected synchronized void notifyStartListeners(ExchangeConversion conversion){
		for(ExchangeConversionStartListener listener : startListeners){
			listener.ExchangeConversionStarted(new ExchangeConversionEvent(conversion));
		}
	}

	/*
	 * Notify Listeners of Conversion Complete Event
	 */
	protected synchronized void notifyCompleteListeners(ExchangeConversion conversion){
		for(ExchangeConversionCompleteListener listener : completeListeners){
			listener.ExchangeConversionCompleted(new ExchangeConversionEvent(conversion));
		}
	}

	/*
	 * Notify Listeners of No More Conversion to Process Event
	 */
	protected synchronized void notifyNoMoreListeners(){
		for(NoMoreConversionsListener listener : noMoreListeners){
			listener.noMoreConversions();
		}
	}

	// Add/Remove Listeners
	/////////////////////////////////////////////////////////////////////////////////

	public synchronized void addExchangeConversionAddedListener(ExchangeConversionAddedListener listener) {
		addedListeners.add(listener);
	}

	public synchronized void removeExchangeConversionAddedListener(ExchangeConversionAddedListener listener) {
		addedListeners.remove(listener);
	}

	public synchronized void addExchangeConversionStartListener(ExchangeConversionStartListener listener) {
		startListeners.add(listener);
	}

	public synchronized void removeExchangeConversionStartListener(ExchangeConversionStartListener listener) {
		startListeners.remove(listener);
	}
	
	public synchronized void addExchangeConversionCompleteListener(ExchangeConversionCompleteListener listener) {
		completeListeners.add(listener);
	}

	public synchronized void removeExchangeConversionCompleteListener(ExchangeConversionCompleteListener listener) {
		completeListeners.remove(listener);
	}

	public synchronized void addNoMoreConversionsListener(NoMoreConversionsListener listener) {
		noMoreListeners.add(listener);
	}

	public synchronized void removeNoMoreConversionsListener(NoMoreConversionsListener listener) {
		noMoreListeners.remove(listener);
	}

	// Local Listener Implementations
	
	// local listener impl to debug
	public synchronized void ExchangeConversionAdded(ExchangeConversionEvent event) {
		logger.info("Conversion Added: " + event.getExchangeConversion().getId() + " - " + dtFmt.format(new Date()));
	}

	// local listener impl to debug
	public synchronized void ExchangeConversionStarted(ExchangeConversionEvent event) {
		logger.info("Conversion Starting: " + event.getExchangeConversion().getId() + " - " + dtFmt.format(new Date()));
	}

	// local listener impl to debug
	public synchronized void ExchangeConversionCompleted(ExchangeConversionEvent event) {
		ExchangeConversion conv = event.getExchangeConversion();
		logger.info("Conversion Completed: " + conv.getId() + " - " + dtFmt.format(new Date()));
		int dur = ((int)(conv.getEnd() - conv.getStart()))/1000;
		completedLogger.info(dtFmt.format(conv.getStart()) 
				     + "\t" + conv.getId() 
				     + "\t" + conv.getStatus()  
				     + "\t" + (dur/60) + " Mins " + (dur% 60) + " Secs"
				     + "\t" + conv.getMessagesMovedCnt()
		             + "\t" + ByteFormatter.of(conv.getMessagesMovedSize()).format(conv.getMessagesMovedSize()));
	}

	// local listener impl to debug
	public synchronized void noMoreConversions() {
		logger.debug("No More Exchange Conversions to process");
	}

	/**
	 * Thread Pool Executor for this Conversion Manager. 
	 *
	 */
	private class ExchangeConversionPoolExecutor extends ThreadPoolExecutor {
		ExchangeConversionPoolExecutor(){
			super(maxRunningThreads, maxRunningThreads, 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(maxCachedThreads));
		}

		protected void beforeExecute(Thread t, Runnable r) {
			ExchangeConversion conv = ((ExchangeConversion)r); 
			t.setName("i2e: " + conv.getId());
			queued.remove(conv);
			running.add(conv);
			ExchangeConversionManager.this.notifyStartListeners((ExchangeConversion)r);
			super.beforeExecute(t, r);
		}
		
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			ExchangeConversion conv = ((ExchangeConversion)r); 
			ExchangeConversionManager.this.notifyCompleteListeners(conv);
			running.remove(conv);
			conv.clean();
			/* Active Count need to be one for last conversion because 
			 * it is still active as this method is called.	 */
			if(this.getQueue().isEmpty() && this.getActiveCount() == 1){
				ExchangeConversionManager.this.notifyNoMoreListeners();
			}
		}
	}

	public int getMaxCachedThreads() {
		return maxCachedThreads;
	}

	public void setMaxCachedThreads(int maxCachedThreads) {
		this.maxCachedThreads = maxCachedThreads;
	}

	public int getMaxRunningThreads() {
		return maxRunningThreads;
	}

	public void setMaxRunningThreads(int maxRunningThreads) {
		this.maxRunningThreads = maxRunningThreads;
	}

	public List<ExchangeConversion> getQueued() {
		return Collections.unmodifiableList(queued);
	}

	public List<ExchangeConversion> getRunning() {
		return Collections.unmodifiableList(running);
	}

}
