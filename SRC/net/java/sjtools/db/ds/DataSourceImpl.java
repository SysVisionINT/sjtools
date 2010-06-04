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
package net.java.sjtools.db.ds;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DataSourceImpl implements DataSource, Serializable {
	private static final long serialVersionUID = -1460664318268614437L;

	private PrintWriter out = new PrintWriter(System.out);
	
	private boolean registed = false;
	
	private String driverClass = null;
	private String dataBaseURL = null;
	private String userName = null;
	private String userPassword = null;
	
	public String getDataBaseURL() {
		return dataBaseURL;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getUserName() {
		return userName;
	}

	
	public DataSourceImpl() {
	}
	
	public DataSourceImpl(String driver, String url, String user, String password) {
		driverClass = driver;
		dataBaseURL = url;
		userName = user;
		userPassword = password;
	}
	
	public DataSourceImpl(String url, String user, String password) {
		registed = true;
		
		dataBaseURL = url;
		userName = user;
		userPassword = password;
	}	

	public int getLoginTimeout() throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public PrintWriter getLogWriter() throws SQLException {
		return out;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		this.out = out;
	}

	public Connection getConnection() throws SQLException {
		return getConnection(userName, userPassword);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		if (!registed) {
			driverRegistration();
		}

		return DriverManager.getConnection(dataBaseURL, userName, userPassword);
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setDataBaseURL(String dataBaseURL) {
		this.dataBaseURL = dataBaseURL;
	}

	public void setDriverClass(String driverClass) {
		if (!this.driverClass.equals(driverClass)) {
			registed = false;
		}
		
		this.driverClass = driverClass;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private synchronized void driverRegistration() throws SQLException {
		if (registed) {
			return;
		}

		try {
			Class.forName(driverClass).newInstance();
			registed = true;
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public Object unwrap(Class arg0) throws SQLException {
		throw new SQLException("Method not supported!");
	}
}
