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
package net.java.sjtools.ioc.error;

import net.java.sjtools.error.ApplicationError;

public class ObjectRegestryError extends ApplicationError {
	private static final long serialVersionUID = -7703248918605220214L;

	public ObjectRegestryError(Throwable error) {
		super(error);
	}

	public ObjectRegestryError(String message, Throwable error) {
		super(message, error);
	}

	public ObjectRegestryError(String message) {
		super(message);
	}
}
