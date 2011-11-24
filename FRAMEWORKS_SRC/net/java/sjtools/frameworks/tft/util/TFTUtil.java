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
package net.java.sjtools.frameworks.tft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.java.sjtools.frameworks.tft.error.TFTException;
import net.java.sjtools.frameworks.tft.model.TFTClient;
import net.java.sjtools.util.StreamUtil;

public class TFTUtil {

	public static void copyStream(InputStream input, OutputStream output) throws TFTException {
		byte[] buffer = new byte[64000];
		int count = 0;

		try {
			while ((count = input.read(buffer)) != -1) {
				output.write(buffer, 0, count);
			}
		} catch (IOException e) {
			throw new TFTException("Error copying InputStream to OutputStream", e);
		}
	}

	public static void sendFile(TFTClient client, File localFile, String remoteFile) throws TFTException {
		try {
			FileInputStream fileInputStream = new FileInputStream(localFile);
			
			client.put(fileInputStream, remoteFile);
		} catch (FileNotFoundException e) {
			throw new TFTException("Error sending file '" + localFile.getAbsolutePath() + "' to '" + remoteFile + "'", e);
		}
	}
	
	public static void getFile(TFTClient client, String remoteFile, File localFile) throws TFTException {
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		
		try {
			inputStream = client.get(remoteFile);
			fileOutputStream = new FileOutputStream(localFile);
			
			TFTUtil.copyStream(inputStream, fileOutputStream);
		} catch (FileNotFoundException e) {
			throw new TFTException("Error getting file '" + remoteFile + "' to '" + localFile.getAbsolutePath() + "'", e);
		} finally {
			StreamUtil.close(inputStream);
			StreamUtil.close(fileOutputStream);
		}
	}	
}
