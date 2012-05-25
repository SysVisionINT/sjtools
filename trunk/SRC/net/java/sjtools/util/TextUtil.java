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
package net.java.sjtools.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.sjtools.config.error.ConfigurationError;

public class TextUtil {
	private static final String DEFAULT_INCLUDE_PACKAGE = "defaultIncludePackage";
	
	public static final int ALLIGN_CENTER = 0;
	public static final int ALLIGN_LEFT = -1;
	public static final int ALLIGN_RIGHT = 1;

	public static boolean isEmptyString(String txt) {
		return (txt == null || txt.length() == 0);
	}

	public static List split(String text, String token) {
		List list = new ArrayList();
		int start = 0;
		int end = -1;

		while ((end = text.indexOf(token, start)) != -1) {
			list.add(text.substring(start, end));

			start = end + token.length();
		}

		list.add(text.substring(start));

		return list;
	}

	public static String replace(String text, String searchString, String replaceString) {
		StringBuffer buffer = new StringBuffer();
		String line = text;

		int pos = 0;

		while ((pos = line.indexOf(searchString, pos)) != -1) {
			buffer.setLength(0);

			if (pos == 0) {
				buffer.append(replaceString);
				buffer.append(line.substring(pos + searchString.length()));
			} else {
				buffer.append(line.substring(0, pos));
				buffer.append(replaceString);
				buffer.append(line.substring(pos + searchString.length()));
			}

			pos = pos + replaceString.length();

			line = buffer.toString();
		}

		return line;
	}

	public static String replace(String msg, Map parameters) {
		if (parameters.isEmpty() || msg == null) {
			return msg;
		}

		String key = null;
		String text = msg;

		for (Iterator i = parameters.keySet().iterator(); i.hasNext();) {
			key = (String) i.next();
			text = replace(text, key, (String) parameters.get(key));
		}

		return text;
	}

	public static String format(String unformatedText, int size, char filler, int allignType) {
		String text = unformatedText;

		if (text == null) {
			text = "";
		}

		if (text.length() > size) {
			return text.substring(0, size);
		}

		StringBuffer buffer = new StringBuffer();
		int rigth = 0;
		int left = 0;

		switch (allignType) {
			case ALLIGN_LEFT:
				rigth = size - text.length();
				break;
			case ALLIGN_CENTER:
				left = (size - text.length()) / 2;
				rigth = size - (text.length() + left);
				break;
			case ALLIGN_RIGHT:
				left = size - text.length();
				break;
		}

		for (int i = 0; i < left; i++) {
			buffer.append(filler);
		}

		buffer.append(text);

		for (int i = 0; i < rigth; i++) {
			buffer.append(filler);
		}

		return buffer.toString();
	}

	public static String toString(Collection list) {
		return toString(list, isDefaultIncludePackage());
	}

	public static String toString(Collection list, boolean includePackage) {
		if (list == null) {
			return "null";
		}

		StringBuffer buffer = new StringBuffer();

		for (Iterator i = list.iterator(); i.hasNext();) {
			if (buffer.length() != 0) {
				buffer.append(", ");
			}

			buffer.append(toString(i.next(), includePackage));
		}

		return buffer.toString();
	}

	public static String toString(Map map) {
		return toString(map, isDefaultIncludePackage());
	}

	public static String toString(Map map, boolean includePackage) {
		if (map == null) {
			return "null";
		}

		StringBuffer buffer = new StringBuffer();

		Object obj = null;

		for (Iterator i = map.keySet().iterator(); i.hasNext();) {
			if (buffer.length() != 0) {
				buffer.append(", ");
			}

			obj = i.next();

			buffer.append(toString(obj, includePackage));
			buffer.append("=");
			buffer.append(toString(map.get(obj), includePackage));
		}

		return buffer.toString();
	}

	public static String toString(Object[] array) {
		return toString(array, isDefaultIncludePackage());
	}

	public static String toString(Object[] array, boolean includePackage) {
		if (array == null) {
			return "null";
		}

		if (array.length == 0) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				buffer.append(", ");
			}

			buffer.append(toString(array[i], includePackage));
		}

		return buffer.toString();
	}

	public static String toString(Object obj) {
		return toString(obj, isDefaultIncludePackage());
	}

	public static String toString(Object obj, boolean includePackage) {
		if (obj == null) {
			return "null";
		}

		if (obj instanceof String) {
			return (String) obj;
		}

		if (obj.getClass().isPrimitive()) {
			return obj.toString();
		}

		if (obj.getClass().isArray()) {
			return toString((Object[]) obj);
		}

		if (obj.getClass().getPackage() != null) {
			String packageName = obj.getClass().getPackage().getName();

			if (packageName.startsWith("java.") || packageName.startsWith("javax.")) {
				return obj.toString();
			}
		}

		BeanUtil beanUtil = new BeanUtil(obj);

		if (beanUtil.extendsClassFromPackageStartingWith("java.") || beanUtil.extendsClassFromPackageStartingWith("javax.")) {
			return obj.toString();
		}

		return beanUtil.toString(includePackage);
	}

	public static String trim(String text) {
		if (text == null) {
			return null;
		} else {
			return text.trim();
		}
	}

	public static String toLowerCase(String text) {
		if (text == null) {
			return null;
		} else {
			return text.toLowerCase();
		}
	}

	public static String toUpperCase(String text) {
		if (text == null) {
			return null;
		} else {
			return text.toUpperCase();
		}
	}
	
	public static boolean isDefaultIncludePackage() {
		try {
			return SJToolsConfigReader.getInstance().getBoolean(DEFAULT_INCLUDE_PACKAGE, true);
		} catch (ConfigurationError e) {
			return true;
		}
	}

	public static void setDefaultIncludePackage(boolean defaultIncludePackage) {
		SJToolsConfigReader.getInstance().setParameter(DEFAULT_INCLUDE_PACKAGE, Boolean.toString(defaultIncludePackage));
	}
}