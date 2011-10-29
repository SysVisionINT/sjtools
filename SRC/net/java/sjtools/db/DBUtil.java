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
package net.java.sjtools.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import net.java.sjtools.db.ds.DataSourceFactory;

public class DBUtil {
	public static Connection getConnection(String jndiName) throws NamingException, SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource(jndiName);

		return getConnection(dataSource);
	}
	
	public static Connection getConnection(String driver, String url, String user, String password) throws SQLException {
		DataSource dataSource = DataSourceFactory.getDataSource(driver, url, user, password);

		return getConnection(dataSource);
	}	
	
	public static Connection getConnection(DataSource dataSource) throws SQLException {
		if (dataSource != null) {
			return dataSource.getConnection();
		} else {
			throw new SQLException("No datasource!");
		}
	}

	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
	}

	public static void close(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
			}
		}
	}
}
