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
package net.java.sjtools.logging;

import net.java.sjtools.logging.api.Factory;
import net.java.sjtools.logging.error.LogConfigurationError;
import net.java.sjtools.logging.impl.DefaultFactory;
import net.java.sjtools.logging.util.LogClassLoader;
import net.java.sjtools.logging.util.LogConfigReader;

public class LogFactory {
	private static final String LOGGER_FACTORY_PROPERTY = "sjtools.logging.factory";

	private static Factory factory = null;

	public static Log getLog(Class clazz) throws LogConfigurationError {
		return getLog(clazz.getName());
	}

	public static Log getLog(String name) throws LogConfigurationError {
		return getLogInstance(name);
	}

	private static Log getLogInstance(String name) throws LogConfigurationError {
		if (factory == null) {
			loadFactory();
		}

		return factory.getLog(name);
	}

	private static synchronized void loadFactory() throws LogConfigurationError {
		if (factory != null) {
			return;
		}

		String factoryName = LogConfigReader.getParameter(LOGGER_FACTORY_PROPERTY);

		if (factoryName == null) {
			factoryName = DefaultFactory.class.getName();
		}

		if (factoryName.equals(DefaultFactory.class.getName())) {
			factory = new DefaultFactory();
		} else {
			factory = (Factory) LogClassLoader.getObject(factoryName);
		}
	}
}
