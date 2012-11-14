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
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import net.java.sjtools.config.error.ConfigurationError;
import net.java.sjtools.util.PropertyReader;
import net.java.sjtools.util.ResourceUtil;
import net.java.sjtools.util.URLUtil;

public class ConfigFile extends AbstractConfiguration {
	private File configFile = null;
	private String resourceName = null;
	
	public ConfigFile(File configFile) throws ConfigurationError {
		super();
	
		if (configFile == null || !configFile.exists()) {
			throw new ConfigurationError("Configuration file '" + configFile.getAbsolutePath() + "' not found");
		}
		
		setFile(configFile);
	}
	
	public ConfigFile(String resourceName) throws ConfigurationError {
		super();
		
		URL url = ResourceUtil.getContextResourceURL(resourceName);
		
		if (url == null) {
			throw new ConfigurationError("Configuration file '" + resourceName + "' not found in classpath");
		}
		
		try {
			if (URLUtil.isFile(url)) {
				setFile(URLUtil.toFile(url));
			} else {
				setResourceName(resourceName);
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setResourceName(String name) {
		resourceName = name;
		
		loadResourceFile();
	}

	private void setFile(File config) {
		configFile = config;
		
		updateIfModified();
	}

	protected void updateIfModified() {
		if (configFile != null) {
			updateIfFileModified();
		}
	}

	private void loadResourceFile() {
		try {
			Properties properties = PropertyReader.getProperties(ResourceUtil.getContextResourceInputStream(resourceName));
			
			setConfiguration(properties, System.currentTimeMillis());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void updateIfFileModified() {
		if (!configFile.exists()) {
			throw new RuntimeException("File '" + configFile.getAbsolutePath() + "' not longer exists");
		}
		
		long lastModified = configFile.lastModified();
		
		if (lastModified > getConfigDate()) {
			try {
				Properties properties = PropertyReader.getProperties(configFile);
				
				setConfiguration(properties, lastModified);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
