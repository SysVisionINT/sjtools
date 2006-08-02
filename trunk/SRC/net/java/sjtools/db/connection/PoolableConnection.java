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
import java.sql.SQLException;

public class PoolableConnection extends ConnectionWrapper {
	private static int count = 0;

	private String connectionID = null;
	private ConnectionListener listener = null;

	public PoolableConnection(Connection con, ConnectionListener listener) {
		super(con);
		this.listener = listener;
		connectionID = getConnectionID();
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
		if (listener != null) {
			listener.connectionClosed(this);
		}
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
