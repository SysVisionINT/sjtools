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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.java.sjtools.logging.api.Level;
import net.java.sjtools.logging.util.LogConfigReader;
import net.java.sjtools.thread.Lock;
import net.java.sjtools.util.TextUtil;

public class LogLevel implements Serializable {
	private static final long serialVersionUID = -5230971362016826432L;

	private static final String DEFAULT_LOGGER_LEVEL_PROPERTY = "sjtools.logging.level";

	private Map levelMap = new HashMap();
	private Lock lock = new Lock(levelMap);

	public Level getLevel(String logger) {
		lock.getReadLock();
		Level level = (Level) levelMap.get(logger);
		lock.releaseLock();

		if (level == null) {
			level = findLevel(logger);

			lock.getWriteLock();
			levelMap.put(logger, level);
			lock.releaseLock();
		}

		return level;
	}

	private Level findLevel(String logger) {
		Properties prop = LogConfigReader.getInstance().getParameters(DEFAULT_LOGGER_LEVEL_PROPERTY);

		if (prop.isEmpty()) {
			prop.setProperty(DEFAULT_LOGGER_LEVEL_PROPERTY, "ERROR");
		}

		String level = prop.getProperty(DEFAULT_LOGGER_LEVEL_PROPERTY);
		List list = TextUtil.split(logger, ".");
		StringBuffer name = new StringBuffer(DEFAULT_LOGGER_LEVEL_PROPERTY);

		String value = null;

		for (Iterator i = list.iterator(); i.hasNext();) {
			name.append(".");
			name.append(i.next());

			value = prop.getProperty(name.toString());

			if (value != null) {
				level = value;
			}
		}

		return Level.getLevel(level);
	}

	public void setLoggerLevel(String name, Level level) {
		String loggerName = DEFAULT_LOGGER_LEVEL_PROPERTY;
		
		if (name != null) {
			loggerName = loggerName.concat(".").concat(name);
		}
		
		LogConfigReader.getInstance().setParameter(loggerName, level.toString());

		lock.getWriteLock();
		levelMap.clear();
		lock.releaseLock();
	}
}
