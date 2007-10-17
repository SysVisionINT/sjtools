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
package net.java.sjtools.mail.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.activation.DataSource;

public class ByteArrayDataSource implements DataSource {
	private byte[] data = null;
	private String type = null;

	public ByteArrayDataSource(InputStream is, String type) {
		this.type = type;
		
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int ch = 0;

			while ((ch = is.read()) != -1) {
				os.write(ch);
			}
			
			data = os.toByteArray();
		} catch (IOException e) {
		}
	}

	public ByteArrayDataSource(byte[] data, String type) {
		this.data = data;
		this.type = type;
	}

	public ByteArrayDataSource(String data, String type) {
		try {
			this.data = data.getBytes("iso-8859-1");
		} catch (UnsupportedEncodingException e) {
		}
		
		this.type = type;
	}

	public InputStream getInputStream() throws IOException {
		if (data == null) {
			throw new IOException("ERROR: No data!");
		}
		
		return new ByteArrayInputStream(data);
	}

	public OutputStream getOutputStream() throws IOException {
		throw new IOException("ERROR: Operation not available");
	}

	public String getContentType() {
		return type;
	}

	public String getName() {
		return this.getClass().getName();
	}
}
