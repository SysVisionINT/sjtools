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

import net.java.sjtools.frameworks.tft.error.InvalidURLException;
import net.java.sjtools.util.NumberUtil;
import net.java.sjtools.util.TextUtil;

public class URLUtil {
	private static final String SERVER = "://";
	private static final String PORT = ":";
	private static final String PATH = "/";
	private static final String PROPERTY = ";";
	private static final String LIST = ",";
	private static final String VALUE = "=";
	
	// <protocolo>://<servidor>[:<porto>][/<caminho>][;propriedade=valor[,...]]
	public static URLData parse(String url) throws InvalidURLException {
		String urlPart = url;
		String propPart = null;
		
		if (url.indexOf(PROPERTY) > 0) {
			int propIndex = url.indexOf(PROPERTY);
			urlPart = url.substring(0, propIndex);
			propPart = url.substring(propIndex + 1);
		}
		
		URLData data = new URLData(url);
		
		int end = urlPart.indexOf(SERVER); 
		
		if (end <= 0) {
			// Erro - sem protocolo
			throw new InvalidURLException(url);
		}
		
		data.setProtocol(urlPart.substring(0, end));
		
		int start = end + SERVER.length();
		
		int port = urlPart.indexOf(PORT, start);
		int path = urlPart.indexOf(PATH, start);
		
		if (port < 0) {
			// Sem port
			if (path < 0) {
				// Só tem informação de servidor
				
				data.setServerName(urlPart.substring(start));
			} else if (path == 0) {
				// Erro - não tem servidor
				throw new InvalidURLException(url);
			} else {
				data.setServerName(urlPart.substring(start, path));
				data.setPath(urlPart.substring(path + 1));
			}
		} else if (port == 0) {
			// Erro - não tem servidor
			throw new InvalidURLException(url);
		} else {
			// Com port
			if (path < 0) {
				// Sem path
				data.setServerName(urlPart.substring(start, port));
				
				String serverPort = urlPart.substring(port + 1);
				
				if (NumberUtil.isValidInteger(serverPort)) {
					// 
					data.setPortNumber(Integer.valueOf(serverPort));
				} else {
					// O porto não é valido
					throw new InvalidURLException(url);
				}
			} else if (path == 0) {
				// Não tem serverName
				throw new InvalidURLException(url);
			} else {
				if (path < port) {
					// A path nao pode vir antes do porto
					throw new InvalidURLException(urlPart);
				} else {
					data.setServerName(urlPart.substring(start, port));
					
					String serverPort = urlPart.substring(port + 1, path);
					
					if (NumberUtil.isValidInteger(serverPort)) {
						// 
						data.setPortNumber(Integer.valueOf(serverPort));
						
						data.setPath(urlPart.substring(path + 1));
					} else {
						// O porto não é valido
						throw new InvalidURLException(url);
					}
				}
			}
		}
		
		if (TextUtil.isEmptyString(data.getServerName())) {
			// O serverName é obrigatorio
			throw new InvalidURLException(url);
		}
		
		if (propPart != null) {
			int index = 0;
			
			while (index < propPart.length()) {
				int posList = propPart.indexOf(LIST, index);
				
				String parameter = null;
				
				if (posList > 0) {
					parameter = propPart.substring(index, posList);
					index = posList + 1;
				} else  if (posList == 0) {
					// Parametro invalido
					throw new InvalidURLException(url);
				} else {
					parameter = propPart.substring(index);
					index = propPart.length();
				}
				
				int posValue = parameter.indexOf(VALUE);
				
				if (posValue <= 0 || posValue == parameter.length() -1) {
					// Parametro invalido - sem nome ou sem value
					throw new InvalidURLException(url);
				} else {
					String name = parameter.substring(0, posValue);
					String value = parameter.substring(posValue + 1);
					
					data.setProperty(name, value);
				}
			}
		}
		
		return data;
	}
}
