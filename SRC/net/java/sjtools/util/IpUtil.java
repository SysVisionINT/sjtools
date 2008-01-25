/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2008 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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

import java.util.Iterator;
import java.util.List;

public class IpUtil {

	private static final String IP_SEPARATOR = ".";
	private static final int IP_PARTS_NR = 4;
	private static final int IP_PART_LENGTH_MAX = 3;
	private static final int IP_PART_LENGTH_MIN = 1;
	private static final int IP_PART_VALUE_MAX = 255;
	private static final int IP_PART_VALUE_MIN = 0;

	private static final int PORT_VALUE_MIN = 0;
	private static final int PORT_VALUE_MAX = 65535;

	public static boolean isValidIP(String ip) {
		if (TextUtil.isEmptyString(ip)) {
			return false;
		}

		try {

			List parts = TextUtil.split(ip, IP_SEPARATOR);

			if (parts.size() != IP_PARTS_NR) {
				return false;
			}

			for (Iterator iter = parts.iterator(); iter.hasNext();) {
				String part = (String) iter.next();

				int partLength = part.length();

				if (partLength < IP_PART_LENGTH_MIN || partLength > IP_PART_LENGTH_MAX) {
					return false;
				}

				int partValue = Integer.parseInt(part);

				if (partValue < IP_PART_VALUE_MIN || partValue > IP_PART_VALUE_MAX) {
					return false;
				}

			}

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValidPort(Integer port) {
		if (port == null) {
			return false;
		}

		return isValidPort(port.intValue());
	}

	public static boolean isValidPort(String port) {
		if (!NumberUtil.isValidInteger(port)) {
			return false;
		}

		Integer portInt = null;

		try {
			portInt = new Integer(port);
		} catch (NumberFormatException e) {
			return false;
		}

		return isValidPort(portInt.intValue());
	}

	public static boolean isValidPort(int port) {
		return port >= PORT_VALUE_MIN && port <= PORT_VALUE_MAX;
	}

}
