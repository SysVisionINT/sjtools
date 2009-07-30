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

public class MethodCache {
	private SafeMap map = new SafeMap();
	
	public void add(Class clazz, String methodName, Class[] parameters, Method method) {
		ClassMethods classMethods = getClassMethods(clazz);
		
		classMethods.add(methodName, parameters, method);
	}
	
	public void add(Class clazz, String methodName, Class parameter, Method method) {
		Class [] parameters = new Class[1];
		parameters[0] = parameter;
		
		add(clazz, methodName,parameters, method);
	}
	
	public void add(Class clazz, String methodName, Method method) {
		add(clazz, methodName, new Class[0], method);
	}

	private ClassMethods getClassMethods(Class clazz) {
		if (!map.containsKey(clazz)) {
			createClassMethods(clazz);
		}
		
		return (ClassMethods) map.get(clazz);
	}

	private synchronized void createClassMethods(Class clazz) {
		if (map.containsKey(clazz)) {
			return;
		}
		
		map.put(clazz, new ClassMethods());
	}
	
	public Method get(Class clazz, String methodName, Class[] parameters) {
		ClassMethods classMethods = getClassMethods(clazz);
		
		return classMethods.get(methodName, parameters);
	}
	
	public Method get(Class clazz, String methodName, Class parameter) {
		Class [] parameters = new Class[1];
		parameters[0] = parameter;
		
		return get(clazz, methodName, parameters);
	}
	
	public Method get(Class clazz, String methodName) {
		return get(clazz, methodName, new Class[0]);
	}
}
