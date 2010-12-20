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

import java.sql.Connection;
import java.sql.SQLException;

import net.java.sjtools.db.connection.FastConnection;
import net.java.sjtools.db.error.SQLError;

public class TransactionalDataSource extends DataSourceImpl {
	private static final long serialVersionUID = 4609941383579876104L;

	private static ThreadLocal connection = new ThreadLocal();
	private static ThreadLocal transaction = new ThreadLocal();

	public TransactionalDataSource(String driver, String url, String user, String password) {
		super(driver, url, user, password);
	}

	public void startTransaction() throws SQLError {
		transaction.set(Boolean.TRUE);

		Connection con = null;

		try {
			con = super.getConnection();
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
		Boolean tran = (Boolean) transaction.get();

		if (tran != null && tran.booleanValue()) {
			FastConnection con = (FastConnection) connection.get();

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
		Boolean tran = (Boolean) transaction.get();

		if (tran != null && tran.booleanValue()) {
			FastConnection con = (FastConnection) connection.get();

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
		Boolean tran = (Boolean) transaction.get();

		if (tran != null && tran.booleanValue()) {
			return true;
		} else {
			return false;
		}
	}

	public Connection getConnection() throws SQLException {
		if (isInTransaction()) {
			return (Connection) connection.get();
		} else {
			return super.getConnection();
		}
	}

	public static TransactionalDataSource getInstance(String driver, String url, String user, String password) {
		return new TransactionalDataSource(driver, url, user, password);
	}
}
