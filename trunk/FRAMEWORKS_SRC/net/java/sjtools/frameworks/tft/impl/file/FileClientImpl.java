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
package net.java.sjtools.frameworks.tft.impl.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.java.sjtools.frameworks.tft.error.NotConnectedError;
import net.java.sjtools.frameworks.tft.error.OperationNotSupportedException;
import net.java.sjtools.frameworks.tft.error.TFTException;
import net.java.sjtools.frameworks.tft.impl.AbstractProtocolImpl;
import net.java.sjtools.frameworks.tft.impl.BaseFile;
import net.java.sjtools.frameworks.tft.impl.URLData;
import net.java.sjtools.frameworks.tft.util.TFTUtil;
import net.java.sjtools.io.SuperFile;
import net.java.sjtools.util.StreamUtil;

public class FileClientImpl extends AbstractProtocolImpl {

	private SuperFile currentDir = null;

	public FileClientImpl(URLData data) {
		super(data);
	}

	public void connect() throws TFTException {
		if (!getURLData().getServerName().toLowerCase().equals("localhost")) {
			throw new TFTException("file protocol only works with 'localhost'");
		}
		
		if (getURLData().getPortNumber() != null) {
			throw new TFTException("No port number allowed for protocol file");
		}
		
		if (getURLData().getPath() != null) {
			currentDir = new SuperFile(getURLData().getPath());
		} else {
			currentDir = new SuperFile(".");
		}
	}

	public void connect(String login, String password) throws TFTException {
		throw new OperationNotSupportedException("connect(login, password)");
	}

	public void disconnect() {
		currentDir = null;
	}

	public void chdir(String path) throws TFTException {
		if (currentDir == null) {
			throw new NotConnectedError();
		}

		if (path.startsWith("/")) {
			currentDir = new SuperFile(path);
		} else {
			currentDir = new SuperFile(currentDir, path);
		}
	}

	public void mkdir(String path) throws TFTException {
		if (currentDir == null) {
			throw new NotConnectedError();
		}

		SuperFile newDir = new SuperFile(currentDir, path);

		if (!newDir.mkdir()) {
			throw new TFTException("Error executing: mkdir('" + path + "')");
		}
	}

	public Collection list() throws TFTException {
		if (currentDir == null) {
			throw new NotConnectedError();
		}

		List fileList = new ArrayList();

		File[] remoteList = currentDir.listFiles();

		for (int i = 0; i < remoteList.length; i++) {
			File entry = remoteList[i];

			BaseFile file = new BaseFile();
			file.setFileName(entry.getName());

			file.setSize(entry.length());
			file.setDirectory(entry.isDirectory());
			file.setLastModify(new Date(entry.lastModified()));

			fileList.add(file);
		}

		return fileList;
	}

	public void delete(String fileName) throws TFTException {
		if (currentDir == null) {
			throw new NotConnectedError();
		}

		SuperFile file = new SuperFile(currentDir, fileName);
		
		if (!file.delete()) {
			throw new TFTException("Error executing: delete('" + fileName + "')");
		}
	}

	public void rename(String oldFileName, String newFileName) throws TFTException {
		if (currentDir == null) {
			throw new NotConnectedError();
		}

		SuperFile file = new SuperFile(currentDir, oldFileName);
		SuperFile newFile = new SuperFile(currentDir, newFileName);
		
		if (!file.renameTo(newFile)) {
			throw new TFTException("Error executing: rename('" + oldFileName + "', '" + newFileName + "')");
		}
	}

	public InputStream get(String fileName) throws TFTException {
		if (currentDir == null) {
			throw new NotConnectedError();
		}
		
		SuperFile file = new SuperFile(currentDir, fileName);
		
		try {
			return file.getInputStream();
		} catch (FileNotFoundException e) {
			throw new TFTException("Error executing: get('" + fileName + "')", e);
		}
	}

	public void put(InputStream is, String fileName) throws TFTException {
		if (currentDir == null) {
			throw new NotConnectedError();
		}

		SuperFile file = new SuperFile(currentDir, fileName);
		OutputStream os = null;
		
		try {
			os = file.getOutputStream();
			
			TFTUtil.copyStream(is, os);
		} catch (FileNotFoundException e) {
			throw new TFTException("Error executing: put(InputStream, '" + fileName + "')", e);
		} finally {
			StreamUtil.close(os);
		}
	}
}
