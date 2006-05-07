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
package net.java.sjtools.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NullReader {
	private static Byte getByte(ResultSet rs, byte value) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			return new Byte(value);
		}
	}

	public static Byte getByte(ResultSet rs, int columnNumber) throws SQLException {
		return getByte(rs, rs.getByte(columnNumber));
	}

	public static Byte getByte(ResultSet rs, String columnName) throws SQLException {
		return getByte(rs, rs.getByte(columnName));
	}

	private static Short getShort(ResultSet rs, short value) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			return new Short(value);
		}
	}

	public static Short getShort(ResultSet rs, int columnNumber) throws SQLException {
		return getShort(rs, rs.getShort(columnNumber));
	}

	public static Short getShort(ResultSet rs, String columnName) throws SQLException {
		return getShort(rs, rs.getShort(columnName));
	}

	private static Integer getInteger(ResultSet rs, int value) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			return new Integer(value);
		}
	}

	public static Integer getInt(ResultSet rs, int columnNumber) throws SQLException {
		return getInteger(rs, rs.getInt(columnNumber));
	}

	public static Integer getInt(ResultSet rs, String columnName) throws SQLException {
		return getInteger(rs, rs.getInt(columnName));
	}

	private static Long getLong(ResultSet rs, long value) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			return new Long(value);
		}
	}

	public static Long getLong(ResultSet rs, int columnNumber) throws SQLException {
		return getLong(rs, rs.getLong(columnNumber));
	}

	public static Long getLong(ResultSet rs, String columnName) throws SQLException {
		return getLong(rs, rs.getLong(columnName));
	}

	private static Float getFloat(ResultSet rs, float value) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			return new Float(value);
		}
	}

	public static Float getFloat(ResultSet rs, int columnNumber) throws SQLException {
		return getFloat(rs, rs.getFloat(columnNumber));
	}

	public static Float getFloat(ResultSet rs, String columnName) throws SQLException {
		return getFloat(rs, rs.getFloat(columnName));
	}

	private static Double getDouble(ResultSet rs, double value) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			return new Double(value);
		}
	}

	public static Double getDouble(ResultSet rs, int columnNumber) throws SQLException {
		return getDouble(rs, rs.getDouble(columnNumber));
	}

	public static Double getDouble(ResultSet rs, String columnName) throws SQLException {
		return getDouble(rs, rs.getDouble(columnName));
	}

	private static Boolean getBoolean(ResultSet rs, boolean value) throws SQLException {
		if (rs.wasNull()) {
			return null;
		} else {
			return new Boolean(value);
		}
	}

	public static Boolean getBoolean(ResultSet rs, int columnNumber) throws SQLException {
		return getBoolean(rs, rs.getBoolean(columnNumber));
	}

	public static Boolean getBoolean(ResultSet rs, String columnName) throws SQLException {
		return getBoolean(rs, rs.getBoolean(columnName));
	}
}
