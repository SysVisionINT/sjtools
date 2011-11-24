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
package net.java.sjtools.frameworks.tft.impl;

import java.io.Serializable;
import java.util.Date;

import net.java.sjtools.frameworks.tft.model.TFTFile;

public class BaseFile implements TFTFile, Serializable  {
	private static final long serialVersionUID = 7844247152201767660L;
	
	private String fileName = null;
	private Date lastModify = null;
	private boolean directory = false;
	private long size = 0;

	public String getFileName() {
		return fileName;
	}

	public Date getLastModify() {
		return lastModify;
	}

	public boolean isDirectory() {
		return directory;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setLastModify(Date lastModify) {
		this.lastModify = lastModify;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}
	
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
