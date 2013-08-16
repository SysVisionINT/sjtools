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
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.java.sjtools.db.connection.FastConnection;
import net.java.sjtools.db.error.SQLError;

public class TransactionManager implements DataSource {
	private static ThreadLocal<FastConnection> connection = new ThreadLocal<FastConnection>();
	private static ThreadLocal<Boolean> transaction = new ThreadLocal<Boolean>();

	private DataSource dataSource = null;
	
	public TransactionManager(DataSource ds) {
		dataSource = ds;
	}

	public void startTransaction() throws SQLError {
		transaction.set(Boolean.TRUE);

		Connection con = null;

		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			throw new SQLError(e);
		}

		setAutoCommit(con, false);

		connection.set(new FastConnection(con));
	}

	private void setAutoCommit(Connection con, boolean auto) {
		try {
			con.setAutoCommit(auto);
		} catch (SQLException e) {}
	}

	public void commit() throws SQLError {
		Boolean tran = transaction.get();

		if (tran != null && tran.booleanValue()) {
			FastConnection con = connection.get();

			if (con != null) {
				try {
					con.commit();
				} catch (SQLException e) {
					throw new SQLError(e);
				} finally {
					setAutoCommit(con, true);
					close(con);
				}
			}

			transaction.set(Boolean.FALSE);
			connection.set(null);
		}
	}

	private void close(FastConnection con) {
		con.closeConnection();
	}

	public void rollback() throws SQLError {
		Boolean tran = transaction.get();

		if (tran != null && tran.booleanValue()) {
			FastConnection con = connection.get();

			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException e) {
					throw new SQLError(e);
				} finally {
					setAutoCommit(con, true);
					close(con);
				}
			}

			transaction.set(Boolean.FALSE);
			connection.set(null);
		}
	}

	public boolean isInTransaction() {
		Boolean tran = transaction.get();

		if (tran != null && tran.booleanValue()) {
			return true;
		} else {
			return false;
		}
	}

	public Connection getConnection() throws SQLException {
		if (isInTransaction()) {
			return connection.get();
		} else {
			return dataSource.getConnection();
		}
	}

	public Connection getConnection(String username, String password) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("Method not supported!");
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new SQLException("Method not supported!");
	}
}
