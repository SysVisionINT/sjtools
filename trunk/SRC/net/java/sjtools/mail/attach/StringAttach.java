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
package net.java.sjtools.mail.attach;

import java.io.IOException;

import javax.activation.DataSource;

import net.java.sjtools.mail.util.ByteArrayDataSource;

public class StringAttach implements MailAttach {
	private static final long serialVersionUID = -6482666554866180451L;

	private ByteArrayDataSource dataSource = null;
	private String fileName = null;

	public StringAttach(String fileName, String data) {
		this(fileName, data, "text/plain");
	}

	public StringAttach(String fileName, String data, String mimeType) {
		this.fileName = fileName;
		dataSource = new ByteArrayDataSource(data, mimeType);
	}

	public DataSource getData() {
		return dataSource;
	}

	public String getFileName() {
		return fileName;
	}

	public String getContent() {
		try {
			return dataSource.getContentAsString();
		} catch (IOException e) {
			// Temos sempre dados
		}

		return null;
	}
}
