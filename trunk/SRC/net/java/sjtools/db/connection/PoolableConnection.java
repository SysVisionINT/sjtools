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
package net.java.sjtools.db.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

public class PoolableConnection extends ConnectionWrapper {
	private static int count = 0;

	private String connectionID = null;

	private ConnectionListener listener = null;
	private PreparedStatementCache preparedStatementCache = null;

	private boolean inTransaction = false;
	private boolean closed = false;

	public PoolableConnection(Connection con, ConnectionListener listener) {
		super(con);
		this.listener = listener;
		connectionID = getConnectionID();
	}

	public void setPreparedStatementCache(PreparedStatementCache preparedStatementCache) {
		this.preparedStatementCache = preparedStatementCache;
	}

	private static synchronized String getConnectionID() {
		if (count == Integer.MAX_VALUE) {
			count = 0;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(System.currentTimeMillis());
		buffer.append("-");
		buffer.append(count++);

		return buffer.toString();
	}

	public void closeConnection() throws SQLException {
		super.close();
	}

	public void close() throws SQLException {
		if (!inTransaction) {
			if (listener != null) {
				listener.close(this);
			} else {
				closeConnection();
			}
		}

		closed = true;
	}

	public boolean isClosed() throws SQLException {
		if (!closed) {
			return super.isClosed();
		}

		return closed;
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		super.setAutoCommit(autoCommit);

		if (autoCommit == false) {
			inTransaction = true;
		}
	}

	public void commit() throws SQLException {
		super.commit();
		endOffTransaction();
	}

	public void rollback() throws SQLException {
		super.rollback();
		endOffTransaction();
	}

	private void endOffTransaction() throws SQLException {
		inTransaction = false;

		if (closed) {
			close();
		}
	}

	public Savepoint setSavepoint() throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		throw new SQLException("Method not supported!");
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement ret = null;

		if (preparedStatementCache != null) {
			ret = preparedStatementCache.getPreparedStatement(this, sql);
		}

		if (ret == null) {
			ret = super.prepareStatement(sql);
		}

		if (preparedStatementCache != null) {
			preparedStatementCache.registerPreparedStatement(this, sql, ret);
		}

		return ret;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof PoolableConnection)) {
			return false;
		}

		PoolableConnection other = (PoolableConnection) obj;

		return connectionID.equals(other.connectionID);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("PoolableConnection(");
		buffer.append(connectionID);
		buffer.append(")");

		return buffer.toString();
	}
}
