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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractConfigReader {
	private List configFileList = new ArrayList();
	
	public AbstractConfigReader(String resourceName) throws FileNotFoundException {
		configFileList.add(new ConfigFile(resourceName));
	}
	
	public AbstractConfigReader(File configFile) throws FileNotFoundException {
		configFileList.add(new ConfigFile(configFile));
	}
	
	public AbstractConfigReader(List configurations) throws FileNotFoundException {
		for (Iterator i = configurations.iterator(); i.hasNext();) {
			Object obj = i.next();
			
			if (obj instanceof String) {
				String resourceName = (String) obj;
				
				configFileList.add(new ConfigFile(resourceName));
			} else if (obj instanceof File) {
				File configFile = (File) obj;
				
				configFileList.add(new ConfigFile(configFile));
			} else {
				throw new FileNotFoundException("ConfigFile " + obj.toString() + " not a resource name or a File");
			}
		}
	}	

	public String getParameter(String parameterName) {
		String value = null;
		
		for (Iterator i = configFileList.iterator(); i.hasNext() && value == null;) {
			ConfigFile configFile = (ConfigFile) i.next();
			
			value = configFile.getParameter(parameterName);
		}
		
		return value;
	}
	
	public String getParameter(String parameterName, String defaultValue) {
		String value = getParameter(parameterName);
		
		if (value == null) {
			value = defaultValue;
		}
		
		return value;
	}
	
	public void setValidationInterval(long validationInterval) {
		for (Iterator i = configFileList.iterator(); i.hasNext();) {
			ConfigFile configFile = (ConfigFile) i.next();
			
			configFile.setValidationInterval(validationInterval);
		}
	}	
}
