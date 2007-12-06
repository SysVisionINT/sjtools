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

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.java.sjtools.io.error.InvalidFilterException;

public class RegExpNameFilter implements FileFilter {

	private String filter = null;
	private Pattern pattern = null;

	public RegExpNameFilter(String filter) throws InvalidFilterException, PatternSyntaxException {
		if (filter == null) {
			throw new InvalidFilterException("null");
		}

		this.pattern = Pattern.compile(filter);
		this.filter = filter;
	}

	public boolean accept(File pathname) {
		return pattern.matcher(pathname.getName()).matches();
	}

	public String getFilter() {
		return filter;
	}

}
