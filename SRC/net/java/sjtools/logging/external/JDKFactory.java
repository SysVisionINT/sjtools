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

import java.util.logging.Logger;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.api.Factory;
import net.java.sjtools.logging.api.Level;
import net.java.sjtools.logging.error.LogRuntimeError;

public class JDKFactory implements Factory {

	public Log getLog(String name) {
		return new JDKLog(name);
	}

	public void setLoggerLevel(String name, Level level) {
		if (name == null) {
			throw new LogRuntimeError("The logger name must be different from NULL");
		}
		
		Logger logger = Logger.getLogger(name);
		
		if (logger != null) {
			if (level.equals(Level.DEBUG)) {
				logger.setLevel(java.util.logging.Level.FINE);
			} else if (level.equals(Level.INFO)) {
				logger.setLevel(java.util.logging.Level.INFO);
			} else if (level.equals(Level.WARN)) {
				logger.setLevel(java.util.logging.Level.WARNING);
			} else if (level.equals(Level.ERROR)) {
				logger.setLevel(java.util.logging.Level.SEVERE);
			} else if (level.equals(Level.FATAL)) {
				logger.setLevel(java.util.logging.Level.SEVERE);
			}
		}
	}
}
