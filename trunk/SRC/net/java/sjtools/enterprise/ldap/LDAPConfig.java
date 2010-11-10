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
package net.java.sjtools.enterprise.ldap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LDAPConfig implements Serializable {
	private static final long serialVersionUID = -709769950872395071L;
	
	private List domainContextList = new ArrayList();
	private String url = null;
	private String login = null;
	private String password = null;
	private long timeout = 30000;
	private List attributeList = new ArrayList();
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSearchBase(String searchBase) {
		this.domainContextList.add(searchBase);
	}
	
	public void setSearchBase(List contextList) {
		this.domainContextList.addAll(contextList);
	}
	
	public List getDomainContextList() {
		return domainContextList;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void addRequestAttribute(String name) {
		attributeList.add(name);
	}
	
	public List getRequestAttributeList() {
		return attributeList;
	}
}
