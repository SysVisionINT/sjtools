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

import java.util.HashMap;
import java.util.Map;

import net.java.sjtools.frameworks.tft.error.ProtocolNotImplementedException;

public class Protocol {
	private static Map protocolMap = new HashMap();
	
	private String protocolName = null;
	private String className = null;
	
	public static Protocol getProtocol(String protocolName) throws ProtocolNotImplementedException {
		Protocol protocol = (Protocol) protocolMap.get(protocolName);
		
		if (protocol == null) {
			throw new ProtocolNotImplementedException(protocolName);
		}
		
		return protocol;
	}
	
	private Protocol(String protocolName, String className) {
		this.protocolName = protocolName;
		this.className = className;
	}
	
	public String getProtocol() {
		return protocolName;
	}
	
	public String getImplClassName() {
		return className;
	}

	static {
		protocolMap.put("sftp", new Protocol("sftp", "net.java.sjtools.frameworks.tft.impl.sftp.SFTPClient"));
		protocolMap.put("file", new Protocol("sftp", "net.java.sjtools.frameworks.tft.impl.file.FileClient"));
		protocolMap.put("http", new Protocol("http", "net.java.sjtools.frameworks.tft.impl.http.HTTPClient"));
		protocolMap.put("https", new Protocol("https", "net.java.sjtools.frameworks.tft.impl.http.HTTPClient"));
	}
}
