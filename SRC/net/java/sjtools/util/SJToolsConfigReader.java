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
package net.java.sjtools.util;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import net.java.sjtools.logging.error.LogConfigurationError;
import net.java.sjtools.thread.Lock;
import net.java.sjtools.util.PropertyReader;
import net.java.sjtools.util.ResourceUtil;

public class SJToolsConfigReader {
	private static final String LOGGER_CONFIG_FILE = "sjtools-config.properties";

	private static Properties properties = null;
	private static Lock lock = null;

	public static String getParameter(String parameter) {
		if (properties == null) {
			readProperties();
		}

		lock.getReadLock();
		String value = properties.getProperty(parameter);
		lock.releaseLock();

		return value;
	}

	public static void setParameter(String parameter, String value) {
		if (properties == null) {
			readProperties();
		}

		lock.getWriteLock();
		properties.setProperty(parameter, value);
		lock.releaseLock();
	}

	public static Properties getParameters(String parameter) {
		Properties p = new Properties();

		lock.getReadLock();

		String key = null;

		for (Iterator i = properties.keySet().iterator(); i.hasNext();) {
			key = (String) i.next();

			if (key.startsWith(parameter)) {
				p.setProperty(key, properties.getProperty(key));
			}
		}

		lock.releaseLock();

		return p;
	}

	private static synchronized void readProperties() {
		if (properties != null) {
			return;
		}

		Properties props = null;

		try {
			InputStream is = ResourceUtil.getContextResourceInputStream(LOGGER_CONFIG_FILE);

			if (is != null) {
				props = PropertyReader.getProperties(is);
			} else {
				props = new Properties();
			}

			properties = props;
			lock = new Lock(properties);
		} catch (Exception e) {
			throw new LogConfigurationError("Error reading configuration file " + LOGGER_CONFIG_FILE);
		}
	}
}
