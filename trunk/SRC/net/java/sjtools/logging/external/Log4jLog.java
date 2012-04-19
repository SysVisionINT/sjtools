/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package net.java.sjtools.logging.external;

import net.java.sjtools.logging.Log;

import org.apache.log4j.Logger;

public class Log4jLog implements Log {
	private static final long serialVersionUID = -5315912616364251912L;
	
	private String loggerName = null;
	private transient Logger logger = null;

	public Log4jLog(String loggerName) {
		this.loggerName = loggerName;
	}

	private Logger getLogger() {
		if (logger == null) {
			obtainLogger();
		}
		
		return logger;
	}

	private synchronized void obtainLogger() {
		if (logger != null) {
			return;
		}
		
		logger = Logger.getLogger(loggerName);
	}
	
	public boolean isTraceEnabled() {
		return getLogger().isTraceEnabled();
	}

	public boolean isDebugEnabled() {
		return getLogger().isDebugEnabled();
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return getLogger().isInfoEnabled();
	}

	public boolean isFatalEnabled() {
		return true;
	}

	public boolean isWarnEnabled() {
		return true;
	}
	
	public void trace(Object message) {
		getLogger().trace(message);
	}

	public void trace(Object message, Throwable t) {
		getLogger().trace(message, t);
	}		

	public void debug(Object message) {
		getLogger().debug(message);
	}

	public void debug(Object message, Throwable t) {
		getLogger().debug(message, t);
	}

	public void info(Object message) {
		getLogger().info(message);
	}

	public void info(Object message, Throwable t) {
		getLogger().info(message, t);
	}

	public void warn(Object message) {
		getLogger().warn(message);
	}

	public void warn(Object message, Throwable t) {
		getLogger().warn(message, t);
	}

	public void error(Object message) {
		getLogger().error(message);
	}

	public void error(Object message, Throwable t) {
		getLogger().error(message, t);
	}

	public void fatal(Object message) {
		getLogger().fatal(message);
	}

	public void fatal(Object message, Throwable t) {
		getLogger().fatal(message, t);
	}
	
	public String getLoggerName() {
		return loggerName;
	}
}
