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
package net.java.sjtools.logging.impl;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.api.Formater;

public class DefaultLog implements Log {
	private static final long serialVersionUID = -8705829598191185310L;
	
	private String loggerName = null;
	private Level currentLevel = Level.DEFAULT_LEVEL;
	private Formater formater = null;

	public DefaultLog(String loggerName, Level level) {
		this.loggerName = loggerName;
		currentLevel = level;
	}

	public boolean isDebugEnabled() {
		return currentLevel.isLoggable(Level.DEBUG);
	}

	public boolean isErrorEnabled() {
		return currentLevel.isLoggable(Level.ERROR);
	}

	public boolean isInfoEnabled() {
		return currentLevel.isLoggable(Level.INFO);
	}

	public boolean isFatalEnabled() {
		return currentLevel.isLoggable(Level.FATAL);
	}

	public boolean isWarnEnabled() {
		return currentLevel.isLoggable(Level.WARN);
	}

	public void debug(Object message) {
		debug(message, null);
	}

	public void debug(Object message, Throwable throwable) {
		if (isDebugEnabled()) {
			formater.write(getLoggerName(), Level.DEBUG, message, throwable);
		}
	}

	public void info(Object message) {
		info(message, null);
	}

	public void info(Object message, Throwable throwable) {
		if (isInfoEnabled()) {
			formater.write(getLoggerName(), Level.INFO, message, throwable);
		}
	}

	public void warn(Object message) {
		warn(message, null);
	}

	public void warn(Object message, Throwable throwable) {
		if (isWarnEnabled()) {
			formater.write(getLoggerName(), Level.WARN, message, throwable);
		}
	}

	public void error(Object message) {
		error(message, null);
	}

	public void error(Object message, Throwable throwable) {
		if (isErrorEnabled()) {
			formater.write(getLoggerName(), Level.ERROR, message, throwable);
		}
	}

	public void fatal(Object message) {
		fatal(message, null);
	}

	public void fatal(Object message, Throwable throwable) {
		if (isFatalEnabled()) {
			formater.write(getLoggerName(), Level.FATAL, message, throwable);
		}
	}

	public Level getLevel() {
		return currentLevel;
	}

	public String getLoggerName() {
		return loggerName;
	}	
	
	public void setFormater(Formater formater) {
		this.formater = formater;
	}
}
