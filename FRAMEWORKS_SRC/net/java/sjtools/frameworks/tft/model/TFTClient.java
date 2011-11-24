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
package net.java.sjtools.frameworks.tft.model;

import java.io.InputStream;
import java.util.Collection;

import net.java.sjtools.frameworks.tft.error.TFTException;

public interface TFTClient {

	public void connect() throws TFTException;

	public void connect(String login, String password) throws TFTException;

	public void disconnect();

	public void chdir(String path) throws TFTException;

	public Collection list() throws TFTException;
	
	public Collection list(TFTFileFilter filter) throws TFTException;

	public void delete(String fileName) throws TFTException;
	
	public void rename(String oldFileName, String newFileName) throws TFTException;

	public InputStream get(String fileName) throws TFTException;

	public void put(InputStream is, String fileName) throws TFTException;
}
