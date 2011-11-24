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
package net.java.sjtools.frameworks.tft;

import java.lang.reflect.Constructor;

import net.java.sjtools.frameworks.tft.error.TFTException;
import net.java.sjtools.frameworks.tft.impl.Protocol;
import net.java.sjtools.frameworks.tft.impl.URLData;
import net.java.sjtools.frameworks.tft.impl.URLUtil;
import net.java.sjtools.frameworks.tft.model.TFTClient;

public class TFTClientFactory {
	public static TFTClient getInstance(String url) throws TFTException {
		URLData data = URLUtil.parse(url);
		
		Protocol protocol = Protocol.getProtocol(data.getProtocol());
		
		Object impl = getObject(protocol.getImplClassName(), data);
		
		return (TFTClient) impl;
	}
	
	private static Object getObject(String className, URLData data) throws TFTException {
		Class [] args = {URLData.class};
		Object [] initargs = {data};
		
		Object object = null;

		try {
			Class factoryClass = Thread.currentThread().getContextClassLoader().loadClass(className);

			Constructor constructor = factoryClass.getConstructor(args);
			
			object = constructor.newInstance(initargs);
		} catch (ClassNotFoundException e) {
			throw new TFTException("Class " + className + " not found!", e);
		} catch (Exception e) {
			throw new TFTException("Error creating instance of class " + className, e);
		}

		return object;
	}
}
