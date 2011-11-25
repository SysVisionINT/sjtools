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
package net.java.sjtools.frameworks.tft.impl.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.java.sjtools.frameworks.tft.error.NotConnectedError;
import net.java.sjtools.frameworks.tft.error.OperationNotSupportedException;
import net.java.sjtools.frameworks.tft.error.TFTException;
import net.java.sjtools.frameworks.tft.impl.AbstractProtocolImpl;
import net.java.sjtools.frameworks.tft.impl.BaseFile;
import net.java.sjtools.frameworks.tft.impl.URLData;
import net.java.sjtools.time.SuperDate;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPClientImpl extends AbstractProtocolImpl {

	private FTPClient client = null;

	public FTPClientImpl(URLData data) {
		super(data);

		client = new FTPClient();
	}

	public void connect() throws TFTException {
		throw new OperationNotSupportedException("connect()");
	}
	
	public void connect(String login, String password) throws TFTException {
		int port = 21;

		if (getURLData().getPortNumber() != null) {
			port = getURLData().getPortNumber().intValue();
		}

		try {
			client.connect(getURLData().getServerName(), port);
			client.login(login, password);
			
			if (getURLData().getPath() != null) {
				chdir(getURLData().getPath());
			}
		} catch (SocketException e) {
			disconnect();
			throw new TFTException("Error connecting to '" + getURLData().getUrl() + "' with user '" + login + "'", e);
		} catch (IOException e) {
			disconnect();
			throw new TFTException("Error connecting to '" + getURLData().getUrl() + "' with user '" + login + "'", e);
		}
	}

	public void disconnect() {
		if (client != null) {
			try {
				client.disconnect();
			} catch (IOException e) {
			}
		}
	}

	public void chdir(String path) throws TFTException {
		if (client == null || !client.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			if (!client.changeWorkingDirectory(path)) {
				throw new TFTException("Error executing: chdir('" + path + "')");
			}
		} catch (IOException e) {
			throw new TFTException("Error executing: chdir('" + path + "')", e);
		}
	}

	public Collection list() throws TFTException {
		if (client == null || !client.isConnected()) {
			throw new NotConnectedError();
		}
		
		List fileList = new ArrayList();
		
		try {
			FTPFile [] remoteList = client.listFiles();
			
			for (int i = 0; i < remoteList.length; i++) {
				FTPFile entry = remoteList[i];
				
				BaseFile file = new BaseFile();
				file.setFileName(entry.getName());
				file.setSize(entry.getSize());
				file.setDirectory(entry.isDirectory());
				file.setLastModify(new SuperDate(entry.getTimestamp()));
				
				fileList.add(file);
			}
		} catch (IOException e) {
			throw new TFTException("Error executing: list()", e);
		}
		
		return fileList;
	}

	public void delete(String fileName) throws TFTException {
		if (client == null || !client.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			if (!client.deleteFile(fileName)) {
				throw new TFTException("Error executing: delete('" + fileName + "')");
			}
		} catch (IOException e) {
			throw new TFTException("Error executing: delete('" + fileName + "')", e);
		}
	}

	public InputStream get(String fileName) throws TFTException {
		if (client == null || !client.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			return client.retrieveFileStream(fileName);
		} catch (IOException e) {
			throw new TFTException("Error executing: get('" + fileName + "')", e);
		}
	}

	public void put(InputStream is, String fileName) throws TFTException {
		if (client == null || !client.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			if (!client.storeFile(fileName, is)) {
				throw new TFTException("Error executing: put(InputStream, '" + fileName + "')");
			}
		} catch (IOException e) {
			throw new TFTException("Error executing: put(InputStream, '" + fileName + "')", e);
		}
	}

	public void rename(String oldFileName, String newFileName) throws TFTException {
		if (client == null || !client.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			if (!client.rename(oldFileName, newFileName)) {
				throw new TFTException("Error executing: rename('" + oldFileName + "', '" + newFileName + "')");
			}
		} catch (IOException e) {
			throw new TFTException("Error executing: rename('" + oldFileName + "', '" + newFileName + "')", e);
		}
	}

	public void mkdir(String path) throws TFTException {
		if (client == null || !client.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			if (!client.makeDirectory(path)) {
				throw new TFTException("Error executing: mkdir('" + path + "')");
			}
		} catch (IOException e) {
			throw new TFTException("Error executing: mkdir('" + path + "')", e);
		}
	}
}
