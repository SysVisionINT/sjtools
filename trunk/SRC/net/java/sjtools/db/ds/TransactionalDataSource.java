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
import java.util.HashMap;
import java.util.Map;

import net.java.sjtools.db.connection.ConnectionListener;
import net.java.sjtools.db.connection.PoolableConnection;
import net.java.sjtools.thread.Lock;
import net.java.sjtools.thread.SuperThread;
import net.java.sjtools.thread.ThreadContext;

public class TransactionalDataSource extends DataSourceImpl implements ConnectionListener {
	private static final long serialVersionUID = 4351891902885449679L;
	
	private Map threadMap = null;
	private Lock lock = null;

	public TransactionalDataSource(String driver, String url, String user, String password) {
		super(driver, url, user, password);
		init();
	}

	private void init() {
		threadMap = new HashMap();
		lock = new Lock(threadMap);
	}

	public TransactionalDataSource(String url, String user, String password) {
		super(url, user, password);
		init();
	}

	public void startTransaction() {
		DSTransactionInfo info = getDSTransactionInfo();
		info.setInTransaction(true);
		setDSTransactionInfo(info);
	}

	private void setDSTransactionInfo(DSTransactionInfo info) {
		Thread thread = Thread.currentThread();

		if (thread instanceof SuperThread) {
			SuperThread superThread = (SuperThread) thread;
			ThreadContext context = superThread.getThreadContext();

			if (context == null) {
				context = new ThreadContext();

				superThread.setThreadContext(context);
			}

			context.put(toString(), info);
		} else {
			lock.getWriteLock();

			if (info == null) {
				threadMap.remove(thread.getName());
			} else {
				threadMap.put(thread.getName(), info);
			}

			lock.releaseLock();
		}
	}

	private DSTransactionInfo getDSTransactionInfo() {
		DSTransactionInfo info = null;

		Thread thread = Thread.currentThread();

		if (thread instanceof SuperThread) {
			ThreadContext context = ((SuperThread) thread).getThreadContext();

			if (context != null) {
				info = (DSTransactionInfo) context.get(toString());
			}
		} else {
			lock.getReadLock();
			info = (DSTransactionInfo) threadMap.get(thread.getName());
			lock.releaseLock();
		}

		if (info == null) {
			info = new DSTransactionInfo();
		}

		return info;
	}

	public void commit() throws SQLException {
		DSTransactionInfo info = getDSTransactionInfo();

		if (info.getConnection() != null) {
			info.getConnection().commit();
		}

		setDSTransactionInfo(null);
	}

	public void rollback() throws SQLException {
		DSTransactionInfo info = getDSTransactionInfo();

		if (info.getConnection() != null) {
			info.getConnection().rollback();
		}

		setDSTransactionInfo(null);
	}

	public boolean isInTransaction() {
		DSTransactionInfo info = getDSTransactionInfo();

		return info.isInTransaction();
	}

	public Connection getConnection() throws SQLException {
		DSTransactionInfo info = getDSTransactionInfo();

		if (info.getConnection() != null) {
			return info.getConnection();
		} else {
			Connection con = super.getConnection();

			if (info.isInTransaction()) {
				PoolableConnection connection = new PoolableConnection(con, this);
				connection.setAutoCommit(false);

				info.setConnection(connection);
				setDSTransactionInfo(info);

				return connection;
			} else {
				return con;
			}
		}
	}

	public void close(PoolableConnection connection) {
		if (!isInTransaction()) {
			try {
				connection.closeConnection();
			} catch (SQLException e) {
			}
		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("TransactionalDataSource(");
		buffer.append("url=");
		buffer.append(getDataBaseURL());
		buffer.append("user=");
		buffer.append(getUserName());
		buffer.append(")");

		return buffer.toString();
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof TransactionalDataSource)) {
			return false;
		}

		TransactionalDataSource other = (TransactionalDataSource) obj;

		return toString().equals(other.toString());
	}

}
