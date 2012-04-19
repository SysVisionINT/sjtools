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

import java.util.logging.Level;
import java.util.logging.Logger;

import net.java.sjtools.logging.Log;

public class JDKLog implements Log {
	private static final long serialVersionUID = -3535639544190105076L;
	
	private String loggerName = null;
	private transient Logger logger = null;

	public JDKLog(String loggerName) {
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
		return getLogger().isLoggable(Level.FINEST);
	}
	
	public boolean isDebugEnabled() {
		return getLogger().isLoggable(Level.FINE);
	}

	public boolean isErrorEnabled() {
		return getLogger().isLoggable(Level.SEVERE);
	}

	public boolean isInfoEnabled() {
		return getLogger().isLoggable(Level.INFO);
	}

	public boolean isFatalEnabled() {
		return getLogger().isLoggable(Level.SEVERE);
	}

	public boolean isWarnEnabled() {
		return getLogger().isLoggable(Level.WARNING);
	}
	
	public void trace(Object message) {
		getLogger().log(Level.FINEST, String.valueOf(message));
	}

	public void trace(Object message, Throwable t) {
		getLogger().log(Level.FINEST, String.valueOf(message), t);
	}	

	public void debug(Object message) {
		getLogger().log(Level.FINE, String.valueOf(message));
	}

	public void debug(Object message, Throwable t) {
		getLogger().log(Level.FINE, String.valueOf(message), t);
	}

	public void info(Object message) {
		getLogger().log(Level.INFO, String.valueOf(message));
	}

	public void info(Object message, Throwable t) {
		getLogger().log(Level.INFO, String.valueOf(message), t);
	}

	public void warn(Object message) {
		getLogger().log(Level.WARNING, String.valueOf(message));
	}

	public void warn(Object message, Throwable t) {
		getLogger().log(Level.WARNING, String.valueOf(message), t);
	}

	public void error(Object message) {
		getLogger().log(Level.SEVERE, String.valueOf(message));
	}

	public void error(Object message, Throwable t) {
		getLogger().log(Level.SEVERE, String.valueOf(message), t);
	}

	public void fatal(Object message) {
		getLogger().log(Level.SEVERE, String.valueOf(message));
	}

	public void fatal(Object message, Throwable t) {
		getLogger().log(Level.SEVERE, String.valueOf(message), t);
	}

	public String getLoggerName() {
		return loggerName;
	}

}
