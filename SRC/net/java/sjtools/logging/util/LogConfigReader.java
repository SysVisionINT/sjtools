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
package net.java.sjtools.logging.util;

import java.util.Iterator;
import java.util.Properties;

import net.java.sjtools.config.AbstractConfigReader;
import net.java.sjtools.config.error.ConfigurationError;
import net.java.sjtools.logging.error.LogConfigurationError;

public class LogConfigReader extends AbstractConfigReader {
	private static final String LOGGER_CONFIG_FILE = "sjtools-logging.properties";

	private static LogConfigReader me = null; 
	
	private LogConfigReader(String resourceName) throws ConfigurationError {
		super(resourceName);
	}

	public static LogConfigReader getInstance() {
		if (me == null) {
			config();
		}
		
		return me;
	}

	private synchronized static void config() {
		if (me != null) {
			return;
		}
		
		try {
			me = new LogConfigReader(LOGGER_CONFIG_FILE);
		} catch (ConfigurationError e) {
			throw new LogConfigurationError("Error reading configuration file " + LOGGER_CONFIG_FILE, e);
		}
	}
	
	public Properties getParameters(String parameter) {
		Properties p = new Properties();

		String key = null;

		for (Iterator i = getParameterList().iterator(); i.hasNext();) {
			key = (String) i.next();

			if (key.startsWith(parameter)) {
				p.setProperty(key, getParameter(key));
			}
		}

		return p;
	}
}
