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
package net.java.sjtools.frameworks.tft.impl.sftp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.java.sjtools.frameworks.tft.error.NotConnectedError;
import net.java.sjtools.frameworks.tft.error.OperationNotSupportedException;
import net.java.sjtools.frameworks.tft.error.TFTException;
import net.java.sjtools.frameworks.tft.impl.AbstractProtocolImpl;
import net.java.sjtools.frameworks.tft.impl.BaseFile;
import net.java.sjtools.frameworks.tft.impl.URLData;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SFTPClientImpl extends AbstractProtocolImpl {

	private JSch jsch = null;
	private Session session = null;
	private ChannelSftp sftp = null;

	public SFTPClientImpl(URLData data) {
		super(data);

		jsch = new JSch();
		
		JSch.setConfig("StrictHostKeyChecking", "no");
	}

	public void connect() throws TFTException {
		throw new OperationNotSupportedException("connect()");
	}
	
	public void connect(String login, String password) throws TFTException {
		int port = 22;

		if (getURLData().getPortNumber() != null) {
			port = getURLData().getPortNumber().intValue();
		}

		try {
			session = jsch.getSession(login, getURLData().getServerName(), port);
			session.setPassword(password);
			session.connect();

			Channel channel = session.openChannel("sftp");
			
			channel.connect();
			sftp = (ChannelSftp) channel;
			
			if (getURLData().getPath() != null) {
				chdir(getURLData().getPath());
			}
		} catch (JSchException e) {
			sftp = null;
			session = null;

			throw new TFTException("Error connecting to '" + getURLData().getUrl() + "' with user '" + login + "'", e);
		}
	}

	public void disconnect() {
		if (sftp != null) {
			sftp.quit();
		}
		
		if (session != null) {
			session.disconnect();
		}
	}

	public void chdir(String path) throws TFTException {
		if (session == null || !session.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			sftp.cd(path);
		} catch (SftpException e) {
			throw new TFTException("Error executing: chdir('" + path + "')", e);
		}
	}

	public Collection list() throws TFTException {
		if (session == null || !session.isConnected()) {
			throw new NotConnectedError();
		}
		
		List fileList = new ArrayList();
		
		try {
			Vector remoteList = sftp.ls(".");
			
			for (Iterator i = remoteList.iterator(); i.hasNext();) {
				LsEntry entry = (LsEntry) i.next();
				
				BaseFile file = new BaseFile();
				file.setFileName(entry.getFilename());
				
				SftpATTRS attrs = entry.getAttrs();
				file.setSize(attrs.getSize());
				file.setDirectory(attrs.isDir());
				file.setLastModify(new Date(((long)attrs.getMTime()) * 1000));
				
				fileList.add(file);
			}
		} catch (SftpException e) {
			throw new TFTException("Error executing: list()", e);
		}
		
		return fileList;
	}

	public void delete(String fileName) throws TFTException {
		if (session == null || !session.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			sftp.rm(fileName);
		} catch (SftpException e) {
			throw new TFTException("Error executing: delete('" + fileName + "')", e);
		}
	}

	public InputStream get(String fileName) throws TFTException {
		if (session == null || !session.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			return sftp.get(fileName);
		} catch (SftpException e) {
			throw new TFTException("Error executing: get('" + fileName + "')", e);
		}
	}

	public void put(InputStream is, String fileName) throws TFTException {
		if (session == null || !session.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			sftp.put(is, fileName);
		} catch (SftpException e) {
			throw new TFTException("Error executing: put(InputStream, '" + fileName + "')", e);
		}
	}

	public void rename(String oldFileName, String newFileName) throws TFTException {
		if (session == null || !session.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			sftp.rename(oldFileName, newFileName);
		} catch (SftpException e) {
			throw new TFTException("Error executing: rename('" + oldFileName + "', '" + newFileName + "')", e);
		}
	}

	public void mkdir(String path) throws TFTException {
		if (session == null || !session.isConnected()) {
			throw new NotConnectedError();
		}
		
		try {
			sftp.mkdir(path);
		} catch (SftpException e) {
			throw new TFTException("Error executing: mkdir('" + path + "')", e);
		}
	}
}
