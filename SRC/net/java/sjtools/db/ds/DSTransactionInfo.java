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

import java.io.Serializable;

import net.java.sjtools.db.connection.PoolableConnection;

public class DSTransactionInfo implements Serializable {
	private static final long serialVersionUID = -2360908238066227384L;
	
	private boolean inTransaction = false;
	private PoolableConnection connection = null;
	
	public PoolableConnection getConnection() {
		return connection;
	}
	
	public void setConnection(PoolableConnection connection) {
		this.connection = connection;
	}
	
	public boolean isInTransaction() {
		return inTransaction;
	}
	
	public void setInTransaction(boolean inTransaction) {
		this.inTransaction = inTransaction;
	}
}
