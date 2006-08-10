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
package net.java.sjtools.db.filter.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import net.java.sjtools.util.TextUtil;

public class FilterUtil {
	public static boolean isSmallerThan(Object obj1, Object obj2) {
		if (obj1 == null) {
			return false;
		}

		if (obj2 == null) {
			return false;
		}

		if (obj1 instanceof Boolean) {
			return isSmaller((Boolean) obj1, (Boolean) obj2);
		} else if (obj1 instanceof Byte) {
			return isSmaller((Byte) obj1, (Byte) obj2);
		} else if (obj1 instanceof byte[]) {
			return isSmaller((byte[]) obj1, (byte[]) obj2);
		} else if (obj1 instanceof Date) {
			return isSmaller((Date) obj1, (Date) obj2);
		} else if (obj1 instanceof Double) {
			return isSmaller((Double) obj1, (Double) obj2);
		} else if (obj1 instanceof Float) {
			return isSmaller((Float) obj1, (Float) obj2);
		} else if (obj1 instanceof Integer) {
			return isSmaller((Integer) obj1, (Integer) obj2);
		} else if (obj1 instanceof Long) {
			return isSmaller((Long) obj1, (Long) obj2);
		} else if (obj1 instanceof Short) {
			return isSmaller((Short) obj1, (Short) obj2);
		} else if (obj1 instanceof String) {
			return isSmaller((String) obj1, (String) obj2);
		} else if (obj1 instanceof Time) {
			return isSmaller((Time) obj1, (Time) obj2);
		} else if (obj1 instanceof Timestamp) {
			return isSmaller((Timestamp) obj1, (Timestamp) obj2);
		} else if (obj1 instanceof BigDecimal) {
			return isSmaller((BigDecimal) obj1, (BigDecimal) obj2);
		}

		return true;
	}

	public static boolean isBiggerThan(Object obj1, Object obj2) {
		if (obj1 == null) {
			return false;
		}

		if (obj2 == null) {
			return false;
		}

		if (obj1 instanceof Boolean) {
			return isBigger((Boolean) obj1, (Boolean) obj2);
		} else if (obj1 instanceof Byte) {
			return isBigger((Byte) obj1, (Byte) obj2);
		} else if (obj1 instanceof byte[]) {
			return isBigger((byte[]) obj1, (byte[]) obj2);
		} else if (obj1 instanceof Date) {
			return isBigger((Date) obj1, (Date) obj2);
		} else if (obj1 instanceof Double) {
			return isBigger((Double) obj1, (Double) obj2);
		} else if (obj1 instanceof Float) {
			return isBigger((Float) obj1, (Float) obj2);
		} else if (obj1 instanceof Integer) {
			return isBigger((Integer) obj1, (Integer) obj2);
		} else if (obj1 instanceof Long) {
			return isBigger((Long) obj1, (Long) obj2);
		} else if (obj1 instanceof Short) {
			return isBigger((Short) obj1, (Short) obj2);
		} else if (obj1 instanceof String) {
			return isBigger((String) obj1, (String) obj2);
		} else if (obj1 instanceof Time) {
			return isBigger((Time) obj1, (Time) obj2);
		} else if (obj1 instanceof Timestamp) {
			return isBigger((Timestamp) obj1, (Timestamp) obj2);
		} else if (obj1 instanceof BigDecimal) {
			return isBigger((BigDecimal) obj1, (BigDecimal) obj2);
		}

		return true;
	}

	public static boolean isEqualTo(Object obj1, Object obj2) {
		if (obj1 == null) {
			return false;
		}

		if (obj2 == null) {
			return false;
		}

		if (obj1 instanceof Boolean) {
			return isEqual((Boolean) obj1, (Boolean) obj2);
		} else if (obj1 instanceof Byte) {
			return isEqual((Byte) obj1, (Byte) obj2);
		} else if (obj1 instanceof byte[]) {
			return isEqual((byte[]) obj1, (byte[]) obj2);
		} else if (obj1 instanceof Date) {
			return isEqual((Date) obj1, (Date) obj2);
		} else if (obj1 instanceof Double) {
			return isEqual((Double) obj1, (Double) obj2);
		} else if (obj1 instanceof Float) {
			return isEqual((Float) obj1, (Float) obj2);
		} else if (obj1 instanceof Integer) {
			return isEqual((Integer) obj1, (Integer) obj2);
		} else if (obj1 instanceof Long) {
			return isEqual((Long) obj1, (Long) obj2);
		} else if (obj1 instanceof Short) {
			return isEqual((Short) obj1, (Short) obj2);
		} else if (obj1 instanceof String) {
			return isEqual((String) obj1, (String) obj2);
		} else if (obj1 instanceof Time) {
			return isEqual((Time) obj1, (Time) obj2);
		} else if (obj1 instanceof Timestamp) {
			return isEqual((Timestamp) obj1, (Timestamp) obj2);
		} else if (obj1 instanceof BigDecimal) {
			return isEqual((BigDecimal) obj1, (BigDecimal) obj2);
		}

		return true;
	}

	public static boolean isSmaller(BigDecimal obj1, BigDecimal obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(BigDecimal obj1, BigDecimal obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(BigDecimal obj1, BigDecimal obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(String obj1, String obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(String obj1, String obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(String obj1, String obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Boolean obj1, Boolean obj2) {
		return false;
	}

	public static boolean isBigger(Boolean obj1, Boolean obj2) {
		return false;
	}

	public static boolean isEqual(Boolean obj1, Boolean obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Byte obj1, Byte obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(Byte obj1, Byte obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(Byte obj1, Byte obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(byte[] obj1, byte[] obj2) {
		return false;
	}

	public static boolean isBigger(byte[] obj1, byte[] obj2) {
		return false;
	}

	public static boolean isEqual(byte[] obj1, byte[] obj2) {
		return false;
	}

	public static boolean isSmaller(Date obj1, Date obj2) {
		return obj1.before(obj2);
	}

	public static boolean isBigger(Date obj1, Date obj2) {
		return obj1.after(obj2);
	}

	public static boolean isEqual(Date obj1, Date obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Double obj1, Double obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(Double obj1, Double obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(Double obj1, Double obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Float obj1, Float obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(Float obj1, Float obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(Float obj1, Float obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Integer obj1, Integer obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(Integer obj1, Integer obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(Integer obj1, Integer obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Long obj1, Long obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(Long obj1, Long obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(Long obj1, Long obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Short obj1, Short obj2) {
		return obj1.compareTo(obj2) < 0;
	}

	public static boolean isBigger(Short obj1, Short obj2) {
		return obj1.compareTo(obj2) > 0;
	}

	public static boolean isEqual(Short obj1, Short obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Time obj1, Time obj2) {
		return obj1.before(obj2);
	}

	public static boolean isBigger(Time obj1, Time obj2) {
		return obj1.after(obj2);
	}

	public static boolean isEqual(Time obj1, Time obj2) {
		return obj1.equals(obj2);
	}

	public static boolean isSmaller(Timestamp obj1, Timestamp obj2) {
		return obj1.before(obj2);
	}

	public static boolean isBigger(Timestamp obj1, Timestamp obj2) {
		return obj1.after(obj2);
	}

	public static boolean isEqual(Timestamp obj1, Timestamp obj2) {
		return obj1.equals(obj2);
	}

	public static boolean matches(String value, String object) {
		Map map = new HashMap();
		map.put("%", ".*");
		map.put("?", ".");
		
		String expression = TextUtil.replace(object, map);
		return value.matches(expression);
	}
}

