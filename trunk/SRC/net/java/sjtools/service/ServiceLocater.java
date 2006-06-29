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
package net.java.sjtools.service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.service.error.ServiceConfigError;
import net.java.sjtools.service.error.ServiceCreationError;
import net.java.sjtools.service.error.ServiceLocaterError;
import net.java.sjtools.service.error.ServiceNotFound;
import net.java.sjtools.thread.Lock;
import net.java.sjtools.util.PropertyReader;
import net.java.sjtools.util.ResourceUtil;

public class ServiceLocater {
	private static final String FILE_NAME = "service-config.properties";

	private static Log log = LogFactory.getLog(ServiceLocater.class);

	private static Map serviceMap = new HashMap();
	private static Lock lock = new Lock(serviceMap);
	private static Properties config = null;

	public static Object lookup(Class clazz) throws ServiceLocaterError {
		String serviceName = clazz.getName();

		if (log.isDebugEnabled()) {
			log.debug("lookup(" + serviceName + ")");
		}

		lock.getReadLock();
		Object object = serviceMap.get(serviceName);
		lock.releaseLock();

		if (object == null) {
			object = loadService(serviceName);
		}

		if (log.isDebugEnabled()) {
			log.debug("lookup(" + serviceName + ") = " + object.getClass().getName());
		}

		return object;
	}

	private static Object loadService(String serviceName) throws ServiceLocaterError {
		if (log.isDebugEnabled()) {
			log.debug("loadService(" + serviceName + ")");
		}

		if (config == null) {
			readServiceConfig();
		}

		String className = config.getProperty(serviceName);

		if (className == null) {
			throw new ServiceNotFound("No implementation for service " + serviceName);
		}

		Object object = createService(className);

		lock.getWriteLock();
		serviceMap.put(serviceName, object);
		lock.releaseLock();

		return object;
	}

	private static Object createService(String className) throws ServiceCreationError {
		if (log.isDebugEnabled()) {
			log.debug("createService(" + className + ")");
		}

		ClassLoader classLoader = getClassLoader();

		Object object = null;

		try {
			Class clazz = classLoader.loadClass(className);

			object = clazz.newInstance();

			fillDependencies(object);
		} catch (Exception e) {
			throw new ServiceCreationError("Error while creating instance of class " + className, e);
		}

		return object;
	}

	private static void fillDependencies(Object object) throws ServiceLocaterError {
		Class[] parameters = null;
		Object objParam = null;
		Object[] value = null;

		Method[] methods = object.getClass().getMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("set")) {
				parameters = methods[i].getParameterTypes();

				if (parameters.length == 1) {
					if (isServiceDefined(parameters[0])) {
						try {
							objParam = lookup(parameters[0]);

							value = new Object[1];
							value[0] = objParam;

							methods[i].invoke(object, value);
						} catch (ServiceLocaterError e) {
							throw e;
						} catch (Exception e) {
							throw new ServiceCreationError("Error invoking method " + methods[i].getName(), e);
						}
					}
				}
			}
		}
	}

	public static boolean isServiceDefined(Class clazz) throws ServiceConfigError {
		if (config == null) {
			readServiceConfig();
		}
		
		String className = config.getProperty(clazz.getName());
		
		return className != null;
	}

	private static ClassLoader getClassLoader() {
		return ServiceLocater.class.getClassLoader();
	}

	private static synchronized void readServiceConfig() throws ServiceConfigError {
		if (config != null) {
			return;
		}

		try {
			config = PropertyReader.getProperties(ResourceUtil.getContextResourceInputStream(FILE_NAME));
		} catch (IOException e) {
			log.error("Error while trying to read configuration file " + FILE_NAME, e);
			throw new ServiceConfigError(e);
		}
	}
}
