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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.java.sjtools.config.error.ConfigurationError;
import net.java.sjtools.time.SuperDate;
import net.java.sjtools.util.DateUtil;
import net.java.sjtools.util.NumberUtil;

public abstract class AbstractConfigReader {
	private List configList = new ArrayList();
	
	public AbstractConfigReader(Configuration configuration) {
		configList.add(configuration);
	}
	
	public AbstractConfigReader(String resourceName) throws ConfigurationError {
		configList.add(new ConfigFile(resourceName));
	}
	
	public AbstractConfigReader(File configFile) throws ConfigurationError {
		configList.add(new ConfigFile(configFile));
	}
	
	public AbstractConfigReader(List configurations) throws ConfigurationError {
		for (Iterator i = configurations.iterator(); i.hasNext();) {
			Object obj = i.next();
			
			if (obj instanceof Configuration) {
				configList.add(obj);
			} else if (obj instanceof String) {
				String resourceName = (String) obj;
				
				configList.add(new ConfigFile(resourceName));
			} else if (obj instanceof File) {
				File configFile = (File) obj;
				
				configList.add(new ConfigFile(configFile));
			} else {
				throw new ConfigurationError("Unknown configuration type '" + obj.toString() + "'");
			}
		}
	}	

	public Collection getParameterList() {
		Set parameters = new TreeSet();

		for (Iterator i = configList.iterator(); i.hasNext();) {
			Configuration config = (Configuration) i.next();
			
			parameters.addAll(config.getParameterList());
		}
		
		return parameters;
	}
	
	public boolean isParameterDefined(String parameterName) {
		return getConfigurationWithParameter(parameterName) != null;
	}	
	
	public String getParameter(String parameterName) {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config != null) {
			return config.getParameter(parameterName);
		}
		
		return null;
	}
	
	private Configuration getConfigurationWithParameter(String parameterName) {
		for (Iterator i = configList.iterator(); i.hasNext();) {
			Configuration config = (Configuration) i.next();
			
			if (config.isParameterDefined(parameterName)) {
				return config;
			}
		}
		
		return null;
	}
	
	public String getParameter(String parameterName, String defaultValue) {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config == null) {
			return defaultValue;
		}
		
		return config.getParameter(parameterName);
	}
	
	public void setValidationInterval(long validationInterval) {
		for (Iterator i = configList.iterator(); i.hasNext();) {
			Configuration config = (Configuration) i.next();
			
			config.setValidationInterval(validationInterval);
		}
	}	
	
	public long getConfigDate() {
		long lastDate = 0;
		
		for (Iterator i = configList.iterator(); i.hasNext();) {
			Configuration config = (Configuration) i.next();
			
			if (lastDate < config.getConfigDate()) {
				lastDate = config.getConfigDate();
			}
		}
		
		return lastDate;
	}
	
	public Long getLong(String parameterName) throws ConfigurationError {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config == null) {
			return null;
		} else {
			String value = config.getParameter(parameterName);
			
			if (!NumberUtil.isValidLong(value)) {
				throw new ConfigurationError("Parameter '" + parameterName + "' is not a valid Long");
			}
			
			return new Long(value);
		}
	}
	
	public long getLong(String parameterName, long defaultValue) throws ConfigurationError {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config == null) {
			return defaultValue;
		} else {
			String value = config.getParameter(parameterName);
			
			if (!NumberUtil.isValidLong(value)) {
				throw new ConfigurationError("Parameter '" + parameterName + "' is not a valid Long");
			}
			
			return Long.parseLong(value);
		}
	}	
	
	public Double getDouble(String parameterName) throws ConfigurationError {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config == null) {
			return null;
		} else {
			String value = config.getParameter(parameterName);
			
			if (!NumberUtil.isValidDouble(value)) {
				throw new ConfigurationError("Parameter '" + parameterName + "' is not a valid Double");
			}
			
			return new Double(value);
		}
	}
	
	public double getDouble(String parameterName, double defaultValue) throws ConfigurationError {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config == null) {
			return defaultValue;
		} else {
			String value = config.getParameter(parameterName);
			
			if (!NumberUtil.isValidDouble(value)) {
				throw new ConfigurationError("Parameter '" + parameterName + "' is not a valid Double");
			}
			
			return Double.parseDouble(value);
		}
	}	
	
	public Date getDate(String parameterName, String format) throws ConfigurationError {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config == null) {
			return null;
		} else {
			String value = config.getParameter(parameterName);
			
			if (!DateUtil.isValidDate(value, format)) {
				throw new ConfigurationError("Parameter '" + parameterName + "' is not a valid Date in format '" + format + "'");
			}
			
			try {
				return new SuperDate(config.getParameter(parameterName), format);
			} catch (ParseException e) {
				throw new ConfigurationError("Error parsing parameter '" + parameterName + "'", e);
			}
		}
	}	
	
	public Date getDate(String parameterName, String format, Date defaultValue) throws ConfigurationError {
		Configuration config = getConfigurationWithParameter(parameterName);
		
		if (config == null) {
			return defaultValue;
		} else {
			String value = config.getParameter(parameterName);
			
			if (!DateUtil.isValidDate(value, format)) {
				throw new ConfigurationError("Parameter '" + parameterName + "' is not a valid Date in format '" + format + "'");
			}
			
			try {
				return new SuperDate(config.getParameter(parameterName), format);
			} catch (ParseException e) {
				throw new ConfigurationError("Error parsing parameter '" + parameterName + "'", e);
			}
		}
	}	
}
