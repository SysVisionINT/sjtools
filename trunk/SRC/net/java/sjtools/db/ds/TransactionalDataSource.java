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
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import net.java.sjtools.db.connection.ConnectionListener;
import net.java.sjtools.db.connection.PoolableConnection;
import net.java.sjtools.util.JNDIUtil;

public class TransactionalDataSource implements DataSource, ConnectionListener {
	private boolean transaction = false;
	private boolean connectionClose = true;
	private PoolableConnection connection = null;
	private DataSource dataSource = null;

	private TransactionalDataSource(DataSource ds) {
		dataSource = ds;
	}

	public static TransactionalDataSource getInstance(String driver, String url, String user, String password) {
		return getInstance(new DataSourceImpl(driver, url, user, password));
	}

	public static TransactionalDataSource getInstance(String jndiName) throws NamingException {
		return getInstance((DataSource) JNDIUtil.getJNDIObject(jndiName));
	}

	public static TransactionalDataSource getInstance(DataSource ds) {
		return new TransactionalDataSource(ds);
	}

	public void startTransaction() {
		transaction = true;
	}

	public void commit() throws SQLException {
		if (transaction) {
			if (connection != null) {
				try {
					connection.commit();
				} finally {
					if (connectionClose) {
						closeConnection();
					}
				}
			}

			transaction = false;
		}
	}

	public void rollback() throws SQLException {
		if (transaction) {
			if (connection != null) {
				try {
					connection.rollback();
				} finally {
					if (connectionClose) {
						closeConnection();
					}
				}
			}

			transaction = false;
		}
	}

	public boolean isInTransaction() {
		return transaction;
	}

	public Connection getConnection() throws SQLException {
		if (transaction) {
			if (connection != null) {
				return connection;
			} else {
				connection = getNewConnection();
				connectionClose = false;
				return connection;
			}
		} else {
			return getNewConnection();
		}
	}

	private PoolableConnection getNewConnection() throws SQLException {
		Connection connection = dataSource.getConnection();

		if (transaction) {
			connection.setAutoCommit(false);
		}

		return new PoolableConnection(connection, transaction ? this : null);
	}

	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	public void setLoginTimeout(int time) throws SQLException {
		dataSource.setLoginTimeout(time);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	public void setLogWriter(PrintWriter lw) throws SQLException {
		dataSource.setLogWriter(lw);
	}

	public Connection getConnection(String user, String password) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public void connectionClosed(PoolableConnection con) {
		connectionClose = true;
		
		if (!transaction) {
			try {
				closeConnection();
			} catch (SQLException e) {
			}
		}
	}

	private void closeConnection() throws SQLException {
		connection.closeConnection();
		connection = null;
	}
}
