/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2009 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
package net.java.sjtools.frameworks.tft.impl;

import java.io.Serializable;
import java.util.Properties;

public class URLData implements Serializable {
	private static final long serialVersionUID = -2768146720807293580L;
	
	private String url = null;
	private String protocol = null;
	private String serverName = null;
	private Integer portNumber = null;
	private String path = null;
	private Properties properties = new Properties();
	
	public URLData(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public Integer getPortNumber() {
		return portNumber;
	}
	
	public void setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}
	
	public String getProperty(String name) {
		return properties.getProperty(name);
	}
	
	public Properties getProperties() {
		return properties;
	}
}
