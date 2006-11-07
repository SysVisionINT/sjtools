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
package net.java.sjtools.logging.impl;

import java.io.Serializable;
import java.sql.Timestamp;

import net.java.sjtools.logging.api.Formater;
import net.java.sjtools.logging.api.Writer;

public class DefaultFormater implements Formater, Serializable {
	private static final long serialVersionUID = -4038375058337133817L;
	
	private StringBuffer buffer = new StringBuffer();
	private Writer writer = null;

	public void write(String loggerName, Level level, Object message, Throwable throwable) {
		buffer.setLength(0);

		buffer.append(new Timestamp(System.currentTimeMillis()));
		buffer.append(" ");
		buffer.append(level);
		buffer.append(" ");
		buffer.append(loggerName);

		if (message != null) {
			buffer.append(" - ");
			buffer.append(String.valueOf(message));
		}

		writer.println(buffer.toString());

		if (throwable != null) {
			writer.print(throwable);
		}
		
		writer.flush();
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}	
}
