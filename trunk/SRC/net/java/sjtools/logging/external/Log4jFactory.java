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
import net.java.sjtools.logging.api.Factory;
import net.java.sjtools.logging.api.Level;

import org.apache.log4j.Logger;

public class Log4jFactory implements Factory {

	public Log getLog(String name) {
		return new Log4jLog(name);
	}

	public void setLoggerLevel(String name, Level level) {
		Logger logger = Logger.getRootLogger();
		
		if (name != null) {
			logger = Logger.getLogger(name);
		}

		if (logger != null) {
			logger.setLevel(org.apache.log4j.Level.toLevel(level.toString()));
		}
	}
}
