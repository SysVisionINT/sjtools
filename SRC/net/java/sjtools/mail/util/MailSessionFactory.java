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

import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import net.java.sjtools.util.JNDIUtil;

public class MailSessionFactory {
	private static final String SMTP = "smtp";
	private static final String SMTP_PORT = "25";
	
	public static Session getJNDIMailSession(String jndiName) throws NamingException {
		return (Session) JNDIUtil.getJNDIObject(jndiName);
	}
	
	public static Session getSMTPMailSession(String serverName) {
		return getSMTPMailSession(serverName, SMTP_PORT);
	}
	
	public static Session getSMTPMailSession(String serverName, String port) {
		Properties properties = getProperties(SMTP);
		properties.put("mail.host", serverName);
		properties.put("mail.smtp.port", port);
		
		return Session.getInstance(properties);
	}

	private static Properties getProperties(String protocol) {
		Properties properties = new Properties();
		
		properties.put("mail.transport.protocol", protocol);
		
		return properties;
	}
}
