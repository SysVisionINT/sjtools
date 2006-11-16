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
package net.java.sjtools.logging.api;

import java.io.Serializable;

public class Level implements Serializable {
	private static final long serialVersionUID = -9115588506016027984L;
	
	private static final int DEBUG_CODE = 1;
	private static final int INFO_CODE = 2;
	private static final int WARN_CODE = 3;
	private static final int ERROR_CODE = 4;
	private static final int FATAL_CODE = 5;

	private static final String DEBUG_MSG = "DEBUG";
	private static final String INFO_MSG = "INFO";
	private static final String WARN_MSG = "WARN";
	private static final String ERROR_MSG = "ERROR";
	private static final String FATAL_MSG = "FATAL";
	
	public static final Level DEBUG = new Level(DEBUG_CODE);
	public static final Level INFO = new Level(INFO_CODE);
	public static final Level WARN = new Level(WARN_CODE);
	public static final Level ERROR = new Level(ERROR_CODE);
	public static final Level FATAL = new Level(FATAL_CODE);
	
	public static final Level DEFAULT_LEVEL = ERROR;

	private int level = 0;

	public static Level getLevel(String levelName) {
		if (levelName == null) {
			return DEFAULT_LEVEL;
		}
		
		if (levelName.equalsIgnoreCase(DEBUG_MSG)) {
			return DEBUG;
		}
		
		if (levelName.equalsIgnoreCase(INFO_MSG)) {
			return INFO;
		}
		
		if (levelName.equalsIgnoreCase(WARN_MSG)) {
			return WARN;
		}
		
		if (levelName.equalsIgnoreCase(ERROR_MSG)) {
			return ERROR;
		}
		
		if (levelName.equalsIgnoreCase(FATAL_MSG)) {
			return FATAL;
		}
		
		return DEFAULT_LEVEL;
	}
	
	private Level(int level) {
		this.level = level;
	}

	public boolean isLoggable(Level other) {
		return level <= other.level; 
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (! (obj instanceof Level)) {
			return false;
		}
		
		Level other = (Level) obj;
		
		return level == other.level;
	}

	public String toString() {
		switch (level) {
		case DEBUG_CODE:
			return DEBUG_MSG;
		case INFO_CODE:
			return INFO_MSG;
		case WARN_CODE:
			return WARN_MSG;
		case ERROR_CODE:
			return ERROR_MSG;
		case FATAL_CODE:
			return FATAL_MSG;
		}

		return null;
	}

}
