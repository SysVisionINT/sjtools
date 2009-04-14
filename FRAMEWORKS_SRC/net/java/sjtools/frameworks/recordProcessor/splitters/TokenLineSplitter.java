/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2009 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
package net.java.sjtools.frameworks.recordProcessor.splitters;

import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.model.error.InvalidRecordError;
import net.java.sjtools.util.TextUtil;

public class TokenLineSplitter extends LineSplitter {

	private Integer elementCount = null;
	private String token = null;
	private boolean optionalLastToken = false;

	public TokenLineSplitter(Integer elementCount, String token, boolean optionalLastToken) {
		this.elementCount = elementCount;
		this.token = token;
		this.optionalLastToken = optionalLastToken;
	}

	public List split(String line) throws InvalidRecordError {
		List ret = TextUtil.split(line, token);

		if (elementCount != null) {
			switch (ret.size() - elementCount.intValue()) {
				case 0:
					if (!optionalLastToken) {
						throw new InvalidRecordError(line, toString());
					}
					break;
				case 1:
					if (!line.endsWith(token)) {
						throw new InvalidRecordError(line, toString());
					}
					break;
				default:
					throw new InvalidRecordError(line, toString());
			}
		}

		return ret;
	}

	public String toString() {
		return "TokenLineSplitter(" + elementCount + ", " + token + ", " + optionalLastToken + ")";
	}
}
