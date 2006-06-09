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
package net.java.sjtools.db.error;

import net.java.sjtools.error.ApplicationError;

public class RecordUpdateException extends ApplicationError {
	private static final long serialVersionUID = -5631267147870707117L;
	
	public static final String UPDATE = "UPDATE"; 
	public static final String DELETE = "DELETE";
	
	private int changedRecords = 0;
	private int expectedChangedRecords = 0;
	private String operation = null;
	
	public RecordUpdateException(String operation, int expectedChangedRecords, int changedRecords) {
		super("Operation " + operation + " was expected to change " + expectedChangedRecords + " but changed " + changedRecords);
		
		this.changedRecords = changedRecords;
		this.expectedChangedRecords = expectedChangedRecords;
		this.operation = operation;
	}

	public int getChangedRecords() {
		return changedRecords;
	}

	public int getExpectedChangedRecords() {
		return expectedChangedRecords;
	}

	public String getOperation() {
		return operation;
	}	
}
