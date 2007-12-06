/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2007 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
package net.java.sjtools.io.filter;

import java.util.regex.PatternSyntaxException;

import net.java.sjtools.io.error.InvalidFilterException;
import net.java.sjtools.util.TextUtil;

public class PatternNameFilter extends RegExpNameFilter {

	private String filter = null;

	public PatternNameFilter(String filter) throws InvalidFilterException, PatternSyntaxException {
		super(getRegExp(filter));

		this.filter = filter;
	}

	private static String getRegExp(String filter) throws InvalidFilterException {
		if (filter == null) {
			throw new InvalidFilterException("null");
		}

		String processed = TextUtil.replace(filter, ".", "\\.");
		processed = TextUtil.replace(processed, "?", ".");
		processed = TextUtil.replace(processed, "*", ".*");

		StringBuffer buffer = new StringBuffer();
		buffer.append("^");
		buffer.append(processed);
		buffer.append("$");

		return buffer.toString();
	}

	public String getFilter() {
		return filter;
	}

}
