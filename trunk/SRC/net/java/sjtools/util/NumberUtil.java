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
	public static Double format(String number) {
		Double ret = null;
		String work = number.replace(',', '.');

		ret = new Double(work);

		return ret;
	}

	public static boolean isValid(String number) {
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

		return true;
	}
}
