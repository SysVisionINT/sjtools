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
package net.java.sjtools.frameworks.recordProcessor.model.error;

public class InvalidElementError extends ProcessorError {

	private static final long serialVersionUID = -2036956442665785032L;

	private int record = 0;
	private int position = 0;
	private String validator = null;
	private String value = null;

	public InvalidElementError(int record, int position, String validator, String value) {
		super("Invalid element at record " + record + ", column " + position + ". Validator: " + validator + ". Value:" + value);

		this.record = record;
		this.position = position;
		this.validator = validator;
		this.value = value;
	}

	public int getRecord() {
		return record;
	}

	public int getPosition() {
		return position;
	}

	public String getValidator() {
		return validator;
	}

	public String getValue() {
		return value;
	}

}
