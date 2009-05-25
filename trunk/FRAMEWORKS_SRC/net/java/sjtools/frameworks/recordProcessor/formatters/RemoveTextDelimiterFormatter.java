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
package net.java.sjtools.frameworks.recordProcessor.formatters;

import java.io.Serializable;

import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;

public class RemoveTextDelimiterFormatter implements Formatter, Serializable {

	private static final long serialVersionUID = -6156017261644405812L;

	private String delimiter = null;

	public RemoveTextDelimiterFormatter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String format(String value) throws ProcessorError {
		try {
			if (value.startsWith(delimiter) && value.endsWith(delimiter)) {
				value = value.substring(delimiter.length(), value.length() - delimiter.length());
			}

			return value;
		} catch (Exception e) {
			throw new ProcessorError("RemoveTextDelimiterFormatter: Error while removing delimiter (" + delimiter + ") from value (" + value + ")");
		}
	}

}
