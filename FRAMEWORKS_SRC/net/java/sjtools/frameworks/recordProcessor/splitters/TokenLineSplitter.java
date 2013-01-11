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

import java.io.Serializable;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.model.error.InvalidRecordError;
import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;
import net.java.sjtools.util.TextUtil;

public class TokenLineSplitter extends LineSplitter implements Serializable {

	private static final long serialVersionUID = -7350747531275072173L;

	private Integer elementCount = null;
	private String token = null;
	private boolean optionalLastToken = false;

	public TokenLineSplitter(Integer elementCount, String token, boolean optionalLastToken) {
		this.elementCount = elementCount;
		this.token = token;
		this.optionalLastToken = optionalLastToken;
	}

	public List nextRecord() throws ProcessorError {
		try {
			List ret = TextUtil.split(getLine(), token);

			if (elementCount != null) {
				switch (ret.size() - elementCount.intValue()) {
					case 0:
						if (!optionalLastToken) {
							throw new InvalidRecordError(getLine(), toString());
						}
						break;
					case 1:
						if (!getLine().endsWith(token)) {
							throw new InvalidRecordError(getLine(), toString());
						}
						break;
					default:
						throw new InvalidRecordError(getLine(), toString());
				}
			}

			return ret;
		} catch (ProcessorError e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public String toString() {
		return "TokenLineSplitter(" + elementCount + ", " + token + ", " + optionalLastToken + ")";
	}
}
