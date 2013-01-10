/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2013 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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

public class ValueSetError extends ProcessorError {

	private static final long serialVersionUID = 2460955870027230262L;

	private int record = 0;
	private int position = 0;
	private String returnProperty = null;
	private String value = null;
	private String format = null;

	public ValueSetError(int record, int position, String returnProperty, String value, String format, Throwable throwable) {
		super("Unable to set value for record " + record + ", column " + position + ". Return property: " + returnProperty + ". Value: " + value + ". Format: " + format, throwable);

		this.record = record;
		this.position = position;
		this.returnProperty = returnProperty;
		this.value = value;
		this.format = format;
	}

	public int getRecord() {
		return record;
	}

	public int getPosition() {
		return position;
	}

	public String getReturnProperty() {
		return returnProperty;
	}

	public String getValue() {
		return value;
	}

	public String getFormat() {
		return format;
	}

}
