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


public class SearchUtil {

	private String startIgnoreStr = null;
	private String endIgnoreStr = null;

	private SearchUtil(String startIgnoreStr, String endIgnoreStr) {
		this.startIgnoreStr = startIgnoreStr;
		this.endIgnoreStr = endIgnoreStr;
	}

	public static SearchUtil getInstance() {
		return getInstance(null, null);
	}

	public static SearchUtil getInstance(String startIgnoreStr, String endIgnoreStr) {
		return new SearchUtil(startIgnoreStr, endIgnoreStr);
	}

	public int indexOf(String text, String search, int startPosition) {
		if (TextUtil.isEmptyString(text) || TextUtil.isEmptyString(search)) {
			return -1;
		}

		boolean hasIgnoreClause = !TextUtil.isEmptyString(startIgnoreStr) && !TextUtil.isEmptyString(endIgnoreStr);

		char[] textArray = text.toCharArray();
		char[] searchArray = search.toCharArray();

		char[] startIgnoreArray = null;
		char[] endIgnoreArray = null;

		if (hasIgnoreClause) {
			startIgnoreArray = startIgnoreStr.toCharArray();
			endIgnoreArray = endIgnoreStr.toCharArray();
		}

		int ignoreLevel = 0;
		int ignoreStartPos = 0;
		int ignoreEndPos = 0;
		int searchPos = 0;
		int firstLocation = -1;

		for (int i = startPosition; i < textArray.length; i++) {

			if (hasIgnoreClause) {
				if (textArray[i] == startIgnoreArray[ignoreStartPos]) {
					ignoreStartPos++;

					if (ignoreStartPos == startIgnoreArray.length) {
						ignoreLevel++;
						ignoreStartPos = 0;
					}

					continue;
				}

				ignoreStartPos = 0;

				if (ignoreLevel > 0) {
					if (textArray[i] == endIgnoreArray[ignoreEndPos]) {
						ignoreEndPos++;

						if (ignoreEndPos == endIgnoreArray.length) {
							ignoreLevel--;
							ignoreEndPos = 0;
						}
					} else {
						ignoreEndPos = 0;
					}

					continue;
				}
			}

			if (textArray[i] != searchArray[searchPos]) {
				firstLocation = -1;
				searchPos = 0;
			}

			if (textArray[i] == searchArray[searchPos]) {
				if (searchPos == 0) {
					firstLocation = i;
				}

				searchPos++;

				if (searchPos == searchArray.length) {
					return firstLocation;
				}
			}

		}

		return -1;
	}

	public int indexOf(String text, String search) {
		return indexOf(text, search, 0);
	}
}
