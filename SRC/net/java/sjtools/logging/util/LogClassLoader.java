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
package net.java.sjtools.logging.util;

import net.java.sjtools.logging.error.LogConfigurationError;

public class LogClassLoader {
	public static Object getObject(String className) {
		Object object = null;

		try {
			Class factoryClass = Thread.currentThread().getContextClassLoader().loadClass(className);

			object = factoryClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new LogConfigurationError("Class " + className + " not found!", e);
		} catch (Exception e) {
			throw new LogConfigurationError("Error creating instance of class " + className, e);
		}

		return object;
	}
}
