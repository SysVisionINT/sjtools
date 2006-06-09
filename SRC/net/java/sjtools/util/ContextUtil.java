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
package net.java.sjtools.util;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextUtil {

	public static Context getInitialContext() throws NamingException {
		return new InitialContext();
	}
	
	public static Context getInitialContext(Hashtable map) throws NamingException {
		return new InitialContext(map);
	}
	
	public static Context getInitialContext(File propertyFile) throws NamingException, IOException {
		return new InitialContext(PropertyReader.getProperties(propertyFile));
	}	
	
	public static Context getInitialContext(String propertyFileName) throws NamingException, IOException {
		return new InitialContext(PropertyReader.getProperties(propertyFileName));
	}	
	
	public static void close(Context ctx) {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
			}
		}
	}
}
