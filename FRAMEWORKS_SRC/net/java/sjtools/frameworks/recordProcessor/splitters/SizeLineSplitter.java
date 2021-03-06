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
import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.model.error.InvalidRecordError;
import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;
import net.java.sjtools.util.TextUtil;

public class SizeLineSplitter extends LineSplitter implements Serializable {

	private static final long serialVersionUID = 3764127694687643872L;

	private Integer elementCount = null;
	private int elementLength = 0;

	public SizeLineSplitter(Integer elementCount, int elementLength) {
		this.elementCount = elementCount;
		this.elementLength = elementLength;
	}

	public List nextRecord() throws ProcessorError {
		try {
			List ret = new ArrayList();

			if (!TextUtil.isEmptyString(getLine())) {
				char[] lineArray = getLine().toCharArray();

				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < lineArray.length; i++) {
					buffer.append(lineArray[i]);

					if (buffer.length() == elementLength || i == lineArray.length - 1) {
						ret.add(buffer.toString());
						buffer = new StringBuffer();
					}
				}

				if (elementCount != null && ret.size() != elementCount.intValue()) {
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
		return "SizeLineSplitter(" + elementCount + ", " + elementLength + ")";
	}
}
