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
package net.java.sjtools.method;

import java.lang.reflect.Method;

import net.java.sjtools.util.SafeMap;

public class Methods {

	private SafeMap map = new SafeMap();

	public void add(Class[] parameters, Method method) {
		map.put(getKey(parameters), method);
	}

	public Method get(Class[] parameters) {
		return (Method) map.get(getKey(parameters));
	}

	private String getKey(Class[] parameters) {
		switch (parameters.length) {
			case 0:
				return "[0]";
			case 1:
				return parameters[0].getName();
			default:
				StringBuffer buffer = new StringBuffer();
				
				buffer.append("[");
				
				for (int i = 0; i < parameters.length; i++) {
					if (i == 0) {
						buffer.append(",");
					}
					
					buffer.append(parameters[i].getName());
				}
				
				buffer.append("]");
				
				return buffer.toString();
		}
	}
}
