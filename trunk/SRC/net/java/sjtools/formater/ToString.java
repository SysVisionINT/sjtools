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
package net.java.sjtools.formater;

import java.util.Iterator;

import net.java.sjtools.util.SJToolsConfigReader;
import net.java.sjtools.util.SafeMap;
import net.java.sjtools.util.TextUtil;

public class ToString {
	private static final String START_KEY = "sjtools.formater.class.";
	private static SafeMap formaters = new SafeMap();
	private static long lastConfigDate = 0;
	
	public static String toString(Object obj, boolean includePackage) {
		ObjectFormater formater = findFormater(obj.getClass());
		
		if (formater == null) {
			return TextUtil.toString(obj, includePackage);
		} else {
			return formater.format(obj, includePackage);
		}
	}

	private static ObjectFormater findFormater(Class clazz) {
		if (SJToolsConfigReader.getInstance().getConfigDate() > lastConfigDate) {
			reloadConfig();
		}
		
		if (clazz != null) {
			String className = clazz.getName();
			
			if (formaters.containsKey(className)) {
				return (ObjectFormater) formaters.get(className);
			}
			
			return findFormater(clazz.getSuperclass());
		}
		
		return null;
	}
	
	private static synchronized void reloadConfig() {
		if (SJToolsConfigReader.getInstance().getConfigDate() <= lastConfigDate) {
			return;
		}
		
		formaters.clear();
		
		for (Iterator i = SJToolsConfigReader.getInstance().getParameterList().iterator(); i.hasNext();) {
			String key = (String) i.next();

			if (key.startsWith(START_KEY)) {
				String className = key.substring(START_KEY.length());
				String value = SJToolsConfigReader.getInstance().getParameter(key);
				
				formaters.put(className, getObject(value));
			}
		}
		
		lastConfigDate = SJToolsConfigReader.getInstance().getConfigDate();
	}

	public static boolean existFormater(Class clazz) {
		ObjectFormater formater = findFormater(clazz);
		
		return formater != null;
	}
	
	private static Object getObject(String className) {
		Object object = null;

		try {
			Class factoryClass = Thread.currentThread().getContextClassLoader().loadClass(className);

			object = factoryClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new InstantiationError("Class " + className + " not found!");
		} catch (Exception e) {
			throw new InstantiationError("Error creating instance of class " + className);
		}

		return object;
	}
}
