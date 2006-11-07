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

import java.util.Properties;

import net.java.sjtools.util.PropertyReader;
import net.java.sjtools.util.ResourceUtil;

public class LogConfigReader {
	private static final String LOGGER_CONFIG_FILE = "sjtools-logging.properties";

	private static Properties properties = null;

	public static String getParameter(String parameter) {
		String value = System.getProperty(parameter);

		if (value == null) {
			if (properties == null) {
				readProperties();
			}
			
			value = properties.getProperty(parameter);
		}
		
		return value;
	}

	private static synchronized void readProperties() {
		if (properties != null) {
			return;
		}
		
		try {
			Properties props = PropertyReader.getProperties(ResourceUtil.getContextResourceInputStream(LOGGER_CONFIG_FILE));
			
			properties = props;
		} catch (Exception e) {
		}
	}
}
