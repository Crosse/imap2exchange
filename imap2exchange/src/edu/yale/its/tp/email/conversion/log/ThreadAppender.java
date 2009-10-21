package edu.yale.its.tp.email.conversion.log;

import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import java.util.*;
import java.util.concurrent.*;

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
 * </pre>
 *
 */
public class ThreadAppender extends AppenderSkeleton {

	
	Map<String, FileAppender> appenders = new ConcurrentHashMap<String, FileAppender>();
	String outputFolder;
	String managerLogFilename = "conversion.manager.log";
	boolean append = true;
	
	public ThreadAppender(){
	}
	
	public ThreadAppender(String outputFolder){
		this.setOutputFolder(outputFolder);
	}
	
	public ThreadAppender(Layout layout, String outputFolder){
		this.layout = layout;
		this.setOutputFolder(outputFolder);
	}
	
	public String getOutputFolder(){
		return outputFolder;
	}
	
	public void setOutputFolder(String folder){
		if(folder.endsWith("/")) outputFolder = folder;
		else outputFolder = folder + "/";
	}

	@Override
	public void append(LoggingEvent event) {
		getFileAppender(getThreadName(event)).append(event);
	}
	
	public String getThreadName(LoggingEvent event){
		String name  = event.getThreadName(); 
		if(name.startsWith("i2e: ")
		&& name.contains("@")){
			return event.getThreadName().substring(5);
		} else {
			return this.managerLogFilename;
		}
	}
	
	public FileAppender getFileAppender(String threadName){
		FileAppender appender = appenders.get(threadName);
		if(appender == null){
			appender = makeNewAppender(threadName);
			appenders.put(threadName, appender);
		}
		return appender;
	}
	
	public FileAppender makeNewAppender(String threadName){
		FileAppender appender = new FileAppender();
		appender.setLayout(this.layout);
		appender.setFile(outputFolder + threadName);
		appender.setName(threadName);
		appender.setAppend(append);
		appender.activateOptions();
		return appender;
	}

	@Override
	public void close() {
		for(FileAppender appender : appenders.values()){
			appender.close();
		}
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public String getManagerLogFilename() {
		return managerLogFilename;
	}

	public void setManagerLogFilename(String managerLogFilename) {
		this.managerLogFilename = managerLogFilename;
	}

	
	
	
	
}
