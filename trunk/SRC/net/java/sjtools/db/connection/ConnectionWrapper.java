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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class ConnectionWrapper implements Connection {
	private Connection realConnection = null;

	public ConnectionWrapper(Connection con) {
		realConnection = con;
	}

	public int getHoldability() throws SQLException {
		return realConnection.getHoldability();
	}

	public int getTransactionIsolation() throws SQLException {
		return realConnection.getTransactionIsolation();
	}

	public void clearWarnings() throws SQLException {
		realConnection.clearWarnings();
	}

	public void close() throws SQLException {
		realConnection.close();
	}

	public void commit() throws SQLException {
		realConnection.commit();
	}

	public void rollback() throws SQLException {
		realConnection.rollback();
	}

	public boolean getAutoCommit() throws SQLException {
		return realConnection.getAutoCommit();
	}

	public boolean isClosed() throws SQLException {
		return realConnection.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return realConnection.isReadOnly();
	}

	public void setHoldability(int holdability) throws SQLException {
		realConnection.setHoldability(holdability);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		realConnection.setTransactionIsolation(level);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		realConnection.setAutoCommit(autoCommit);
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		realConnection.setReadOnly(readOnly);
	}

	public String getCatalog() throws SQLException {
		return realConnection.getCatalog();
	}

	public void setCatalog(String catalog) throws SQLException {
		realConnection.setCatalog(catalog);
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return realConnection.getMetaData();
	}

	public SQLWarning getWarnings() throws SQLException {
		return realConnection.getWarnings();
	}

	public Savepoint setSavepoint() throws SQLException {
		return realConnection.setSavepoint();
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		realConnection.releaseSavepoint(savepoint);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		realConnection.rollback(savepoint);
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return realConnection.setSavepoint(name);
	}

	public Statement createStatement() throws SQLException {
		return realConnection.createStatement();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return realConnection.createStatement(resultSetType, resultSetConcurrency);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return realConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public Map getTypeMap() throws SQLException {
		return realConnection.getTypeMap();
	}

	public void setTypeMap(Map map) throws SQLException {
		realConnection.setTypeMap(map);
	}

	public String nativeSQL(String sql) throws SQLException {
		return realConnection.nativeSQL(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return realConnection.prepareCall(sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return realConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return realConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return realConnection.prepareStatement(sql);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return realConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return realConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return realConnection.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return realConnection.prepareStatement(sql, columnNames);
	}

	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		return realConnection.createArrayOf(arg0, arg1);
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return realConnection.createBlob();
	}

	public Clob createClob() throws SQLException {
		return realConnection.createClob();
	}

	public NClob createNClob() throws SQLException {
		return realConnection.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return realConnection.createSQLXML();
	}

	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		return realConnection.createStruct(arg0, arg1);
	}

	public Properties getClientInfo() throws SQLException {
		return realConnection.getClientInfo();
	}

	public String getClientInfo(String arg0) throws SQLException {
		return realConnection.getClientInfo(arg0);
	}

	public boolean isValid(int arg0) throws SQLException {
		return realConnection.isValid(arg0);
	}

	public void setClientInfo(Properties arg0) throws SQLClientInfoException {
		realConnection.setClientInfo(arg0);
	}

	public void setClientInfo(String arg0, String arg1)
			throws SQLClientInfoException {
		realConnection.setClientInfo(arg0, arg1);
	}

	public boolean isWrapperFor(Class arg0) throws SQLException {
		return realConnection.isWrapperFor(arg0);
	}

	public Object unwrap(Class arg0) throws SQLException {
		return realConnection.unwrap(arg0);
	}

}
