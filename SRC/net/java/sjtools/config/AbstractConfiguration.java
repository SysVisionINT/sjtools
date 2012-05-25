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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import net.java.sjtools.thread.Lock;

public abstract class AbstractConfiguration implements Configuration {
	private long configLastValidationDate = 0;
	private long validationInterval = 600000;
	private long configLastChangeDate = 0;
	private Properties properties = new Properties();
	private Lock lock = null;
	
	protected AbstractConfiguration() {
		lock = new Lock(this);
	}
	
	protected void validate() {
		if (configLastValidationDate + validationInterval < System.currentTimeMillis()) {
			updateIfModified();
			configLastValidationDate = System.currentTimeMillis();
		}
	}

	public long getValidationInterval() {
		return validationInterval;
	}
	
	public void setValidationInterval(long validationInterval) {
		this.validationInterval = validationInterval;
	}
	
	protected abstract void updateIfModified();
	
	protected void setConfiguration(Properties newConfig, long lastModificationDate) {
		lock.getWriteLock();
		
		properties = newConfig;
		configLastChangeDate = lastModificationDate;
		configLastValidationDate = System.currentTimeMillis();
		
		lock.releaseLock();
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
	
	public long getConfigDate() {
		return configLastChangeDate;
	}
}
