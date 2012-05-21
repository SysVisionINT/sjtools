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
package net.java.sjtools.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import net.java.sjtools.thread.Lock;
import net.java.sjtools.util.PropertyReader;
import net.java.sjtools.util.ResourceUtil;
import net.java.sjtools.util.URLUtil;

public class ConfigFile {
	private File configFile = null;
	private Lock lock = null;
	private long configLastChangeDate = 0;
	private long configLastValidationDate = 0;
	private long validationInterval = 600000;
	private Properties properties = new Properties();
	
	public ConfigFile(File configFile) throws FileNotFoundException {
		if (configFile == null || !configFile.exists()) {
			throw new FileNotFoundException("ConfigFile '" + configFile.getAbsolutePath() + "' not found");
		}
		
		setFile(configFile);
	}
	
	public ConfigFile(String resourceName) throws FileNotFoundException {
		URL url = ResourceUtil.getContextResourceURL(resourceName);
		
		if (url == null) {
			throw new FileNotFoundException("ConfigFile '" + resourceName + "' not found in classpath");
		}
		
		try {
			setFile(URLUtil.toFile(url));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setFile(File config) {
		configFile = config;
		lock = new Lock(configFile);
		
		validate();
	}
	
	private void validate() {
		if (configLastValidationDate + validationInterval < System.currentTimeMillis()) {
			updateIfModified();
			configLastValidationDate = System.currentTimeMillis();
		}
	}

	private void updateIfModified() {
		if (!configFile.exists()) {
			throw new RuntimeException("File '" + configFile.getAbsolutePath() + "' not longer exists");
		}
		
		long lastModified = configFile.lastModified();
		
		if (lastModified > configLastChangeDate) {
			lock.getWriteLock();
			
			try {
				properties = PropertyReader.getProperties(configFile);
				
				configLastChangeDate = lastModified;
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				lock.releaseLock();
			}
		}
	}
	
	public Collection getParameterList() {
		validate();
		
		List parameters = new ArrayList();
		
		lock.getReadLock();
		
		for (Enumeration i = properties.propertyNames(); i.hasMoreElements();) {
			parameters.add((String) i.nextElement());
		}
		
		lock.releaseLock();
		
		return parameters;
	}
	
	public boolean isParameterDefined(String parameterName) {
		validate();
		
		lock.getReadLock();
		boolean defined = properties.containsKey(parameterName);
		lock.releaseLock();
		
		return defined;
	}
	
	public String getParameter(String parameterName) {
		validate();
		
		lock.getReadLock();
		String value = properties.getProperty(parameterName);
		lock.releaseLock();
		
		return value;
	}
	
	public String getParameter(String parameterName, String defaultValue) {
		if (! isParameterDefined(parameterName)) {
			return defaultValue;
		}
		
		return getParameter(parameterName);
	}

	public long getValidationInterval() {
		return validationInterval;
	}
	
	public void setValidationInterval(long validationInterval) {
		this.validationInterval = validationInterval;
	}
	
	public long getConfigDate() {
		return this.configLastChangeDate;
	}
}
