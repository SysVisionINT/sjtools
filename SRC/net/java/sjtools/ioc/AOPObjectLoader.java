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
package net.java.sjtools.ioc;

import net.java.sjtools.aop.AOP;
import net.java.sjtools.aop.MethodHandler;
import net.java.sjtools.ioc.error.ObjectCreationError;
import net.java.sjtools.ioc.error.ObjectNotFound;
import net.java.sjtools.ioc.error.ObjectRegestryError;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

public abstract class AOPObjectLoader implements ObjectLoader {
	private static Log log = LogFactory.getLog(AOPObjectLoader.class);
	
	private MethodHandler methodHandler = null;
	private ClassLoader classLoader = null;
	
	public AOPObjectLoader(MethodHandler handler) {
		this(Thread.currentThread().getContextClassLoader(), handler);
	}
	
	public AOPObjectLoader(ClassLoader loader, MethodHandler handler) {
		classLoader = loader;
		methodHandler = handler;
	}
	
	public Object loadObject(String objectName) throws ObjectRegestryError {
		if (log.isDebugEnabled()) {
			log.debug("loadObject(" + objectName + ")");
		}

		String className = null;
		
		try {
			className = getClassName(objectName);
		} catch (ObjectRegestryError e) {
			throw e;
		} catch (Exception e) {
			log.error("Error while looking " + objectName, e);
			throw new ObjectCreationError("Error while looking " + objectName, e);
		}
		
		if (className == null) {
			throw new ObjectNotFound(objectName);
		}
		
		Object object = null;
		
		try {
			Class clazz = classLoader.loadClass(className);

			object = AOP.getInstance(classLoader, methodHandler).create(clazz);
		} catch (Exception e) {
			log.error("Error while creating instance of class " + className, e);
			throw new ObjectCreationError("Error while creating instance of class " + className, e);
		}

		return object;
	}

	public abstract  String getClassName(String objectName) throws Exception;
}
