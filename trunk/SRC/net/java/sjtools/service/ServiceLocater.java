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
import java.util.Properties;

import net.java.sjtools.ioc.BasicObjectLoader;
import net.java.sjtools.ioc.ObjectRegistry;
import net.java.sjtools.ioc.error.ObjectRegestryError;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.service.error.ServiceLocaterError;
import net.java.sjtools.util.PropertyReader;
import net.java.sjtools.util.ResourceUtil;

public class ServiceLocater extends BasicObjectLoader {
	private static final String FILE_NAME = "service-config.properties";

	private static Log log = LogFactory.getLog(ServiceLocater.class);

	private static ServiceLocater myself = new ServiceLocater();
	private Properties config = null;
	private ObjectRegistry registry = null;

	private ServiceLocater() {
		registry = new ObjectRegistry(this);
	}

	public static Object lookup(Class clazz) throws ServiceLocaterError {
		Object object = null;

		try {
			object = myself.registry.getObject(clazz);
		} catch (ObjectRegestryError e) {
			throw new ServiceLocaterError(e);
		}

		return object;
	}

	private synchronized void readServiceConfig() throws ObjectRegestryError {
		if (config != null) {
			return;
		}

		try {
			config = PropertyReader.getProperties(ResourceUtil.getContextResourceInputStream(FILE_NAME));
		} catch (IOException e) {
			log.error("Error while trying to read configuration file " + FILE_NAME, e);
			throw new ObjectRegestryError(e);
		}
	}

	public String getClassName(String objectName) throws ObjectRegestryError {
		if (config == null) {
			readServiceConfig();
		}

		String className = config.getProperty(objectName);

		if (className == null) {
			log.error("Implementing class for service " + objectName + " not found");

			throw new ObjectRegestryError("Implementing class for service " + objectName + " not found");
		}

		return className;
	}

	public boolean isObjectDefined(String objectName) throws ObjectRegestryError {
		String name = getClassName(objectName);

		if (name != null) {
			return true;
		}

		return false;
	}
}
