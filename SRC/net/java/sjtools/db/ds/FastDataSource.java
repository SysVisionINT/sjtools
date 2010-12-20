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

public class FastDataSource extends DataSourceImpl {
	private static final long serialVersionUID = -961465900213544680L;

	private static ThreadLocal localCon = new ThreadLocal();

	public FastDataSource(String driver, String url, String user, String password) {
		super(driver, url, user, password);
	}

	public Connection getConnection() throws SQLException {
		Connection con = (Connection) localCon.get();

		if (con == null) {
			con = super.getConnection();

			localCon.set(new FastConnection(con));
		}

		return con;
	}

	public void release() {
		FastConnection con = (FastConnection) localCon.get();

		if (con != null) {
			con.closeConnection();
		}
	}
}
