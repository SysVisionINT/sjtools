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
package net.java.sjtools.logging.impl;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.api.Config;
import net.java.sjtools.logging.api.Factory;
import net.java.sjtools.logging.api.Formater;
import net.java.sjtools.logging.api.Writer;
import net.java.sjtools.logging.util.LogClassLoader;
import net.java.sjtools.logging.util.LogConfigReader;

public class DefaultFactory implements Factory {
	private static final String DEFAULT_LOGGER_LEVEL_PROPERTY = "sjtools.logging.level";
	private static final String DEFAULT_LOGGER_FORMATER_PROPERTY = "sjtools.logging.formater";
	private static final String DEFAULT_LOGGER_WRITER_PROPERTY = "sjtools.logging.writer";

	private Level currentLevel = Level.DEFAULT_LEVEL;
	private Formater formater = null;

	public DefaultFactory() {
		// Level
		String value = LogConfigReader.getParameter(DEFAULT_LOGGER_LEVEL_PROPERTY);

		if (value != null) {
			setLevel(Level.getLevel(value));
		}

		// Writer
		value = LogConfigReader.getParameter(DEFAULT_LOGGER_WRITER_PROPERTY);
		Writer writer = null;

		if (value == null) {
			writer = new DefaultWriter();
		} else {
			writer = (Writer) LogClassLoader.getObject(value);
		}

		if (writer instanceof Config) {
			configure((Config) writer);
		}

		// Formater
		value = LogConfigReader.getParameter(DEFAULT_LOGGER_FORMATER_PROPERTY);

		if (value == null) {
			formater = new DefaultFormater();
		} else {
			formater = (Formater) LogClassLoader.getObject(value);
		}

		formater.setWriter(writer);
		
		if (formater instanceof Config) {
			configure((Config) formater);
		}
	}

	private void configure(Config config) {
		if (config.getConfigParameters() == null) {
			return;
		}
		
		String name = null;
		
		for (int i = 0; i < config.getConfigParameters().length; i++) {
			name = config.getConfigParameters()[i];
			config.setConfigParameter(name, LogConfigReader.getParameter(name));
		}
	}

	public Log getLog(String name) {
		DefaultLog log = new DefaultLog(name, currentLevel);
		log.setFormater(formater);

		return log;
	}

	public void setLevel(Level level) {
		currentLevel = level;
	}
}
