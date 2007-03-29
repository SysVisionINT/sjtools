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

public class NumberUtil {
	private static final String MAX_INTEGER = String.valueOf(Integer.MAX_VALUE);
	private static final String MIN_INTEGER = String.valueOf(Integer.MIN_VALUE);

	public static Double format(String number) {
		Double ret = null;
		String work = number.replace(',', '.');

		ret = new Double(work);

		return ret;
	}

	public static boolean isValid(String number) {
		if (TextUtil.isEmptyString(number)) {
			return false;
		}

		String work = number.trim();
		char ch = '\0';

		for (int i = 0; i < work.length(); i++) {
			ch = work.charAt(i);

			if (!Character.isDigit(ch)) {
				if (ch == '-' && i == 0) {
					continue;
				}

				if ((ch == ',' || ch == '.') && (i > 0) && (i < (work.length() - 1))) {
					continue;
				}

				return false;
			}
		}

		return true;
	}

	public static boolean isValidInteger(String number) {
		if (TextUtil.isEmptyString(number)) {
			return false;
		}

		String work = number.trim();
		char ch = '\0';

		for (int i = 0; i < work.length(); i++) {
			ch = work.charAt(i);

			if (!Character.isDigit(ch)) {
				if (!(ch == '-' && i == 0)) {
					return false;
				}
			}
		}

		if (work.charAt(0) == '-') {
			if (work.length() > MIN_INTEGER.length()) {
				return false;
			}

			if (work.length() == MIN_INTEGER.length() && work.compareTo(MIN_INTEGER) > 0) {
				return false;
			}
		} else {
			if (work.length() > MAX_INTEGER.length()) {
				return false;
			}

			if (work.length() == MAX_INTEGER.length() && work.compareTo(MAX_INTEGER) > 0) {
				return false;
			}
		}

		return true;
	}
}
