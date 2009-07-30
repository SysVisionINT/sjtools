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


public class ClassMethods {
	private SafeMap map = new SafeMap();
	
	public void add(String methodName, Class[] parameters, Method method) {
		Methods methods = getMethods(methodName);
		
		if (methods == null)  {
			methods = createMethods(methodName);
		}
		
		methods.add(parameters, method);
	}

	private synchronized Methods createMethods(String methodName) {
		if (map.containsKey(methodName)) {
			return getMethods(methodName);
		}
		
		Methods methods = new Methods();
		
		map.put(methodName, methods);
		
		return methods;
	}

	private Methods getMethods(String methodName) {
		return (Methods) map.get(methodName);
	}

	public Method get(String methodName, Class[] parameters) {
		Methods methods = getMethods(methodName);
		
		if (methods == null)  {
			return null;
		}
		
		return methods.get(parameters);
	}
}
