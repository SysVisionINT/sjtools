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

import java.io.InputStream;
import java.util.Properties;

import net.java.sjtools.logging.api.Factory;
import net.java.sjtools.logging.error.LogConfigurationError;
import net.java.sjtools.logging.impl.DefaultFactory;
import net.java.sjtools.logging.impl.Level;

public class LogFactory {
	private static final String LOGGER_FACTORY_PROPERTY = "sjtools.logging.factory";
	private static final String DEFAULT_LOGGER_LEVEL_PROPERTY = "sjtools.logging.level";
	private static final String LOGGER_CONFIG_FILE = "sjtools-logging.properties";

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

		String factoryName = System.getProperty(LOGGER_FACTORY_PROPERTY);
		String defaultLogLevel = System.getProperty(DEFAULT_LOGGER_LEVEL_PROPERTY);;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if (factoryName == null) {
			Properties props = null;

			try {
				InputStream is = classLoader.getResourceAsStream(LOGGER_CONFIG_FILE);

				if (is != null) {
					props = new Properties();

					props.load(is);

					is.close();
				}
			} catch (Exception e) {
			}

			if (props != null) {
				factoryName = props.getProperty(LOGGER_FACTORY_PROPERTY, DefaultFactory.class.getName());

				if (factoryName.equals(DefaultFactory.class.getName())) {
					defaultLogLevel = props.getProperty(DEFAULT_LOGGER_LEVEL_PROPERTY);
				}
			}
		}

		if (factoryName == null) {
			factoryName = DefaultFactory.class.getName();
		}

		if (factoryName.equals(DefaultFactory.class.getName())) {
			DefaultFactory defaultFactory = new DefaultFactory();
			defaultFactory.setLevel(Level.getLevel(defaultLogLevel));

			factory = defaultFactory;
		} else {
			try {
				Class factoryClass = classLoader.loadClass(factoryName);
				
				factory = (Factory) factoryClass.newInstance();
			} catch (ClassNotFoundException e) {
				throw new LogConfigurationError("Factory class " + factoryName + " not found!", e);
			} catch (Exception e) {
				throw new LogConfigurationError("Error creating instance of factory class " + factoryName, e);
			}
		}
	}
}
