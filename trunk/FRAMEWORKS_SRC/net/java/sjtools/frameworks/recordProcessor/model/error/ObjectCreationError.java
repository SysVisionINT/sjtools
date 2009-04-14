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

public class ObjectCreationError extends ProcessorError {

	private static final long serialVersionUID = 4191992987294342695L;

	private String objClass = null;
	private String objValue = null;
	private String objFormat = null;

	public ObjectCreationError(String objClass, String objValue, String objFormat) {
		super("Unable to create an object for class " + objClass + ": value=" + objValue + ", format=" + objFormat + ". Additional info: Maybe a (String) or (String, String) constructor is missing.");

		this.objClass = objClass;
		this.objValue = objValue;
		this.objFormat = objFormat;
	}

	public String getObjClass() {
		return objClass;
	}

	public String getObjValue() {
		return objValue;
	}

	public String getObjFormat() {
		return objFormat;
	}

}
