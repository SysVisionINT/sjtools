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

import java.io.Serializable;

import net.java.sjtools.logging.impl.Level;

public class DBMS implements Serializable {
	private static final long serialVersionUID = -538091222823564956L;

	private static final String DRIVER_UNKNOWN = "UNKNOWN JDBC DRIVER!";
	
	public static final String DRIVER_SAPDB = "SAP DB";
	public static final String DRIVER_MCKOI = "MCKOI";
	public static final String DRIVER_ORACLE = "ORACLE";
	public static final String DRIVER_INFORMIX = "INFORMIX";
	public static final String DRIVER_MYSQL = "MYSQL";
	
	private static final int DBMS_UNKNOWN_VALUE = 0;
	private static final int DBMS_SAPDB_VALUE = 1;
	private static final int DBMS_MCKOI_VALUE = 2;
	private static final int DBMS_ORACLE_VALUE = 3;
	private static final int DBMS_INFORMIX_VALUE = 4;
	private static final int DBMS_MYSQL_VALUE = 5;
	
	public static final DBMS DBMS_UNKNOWN = new DBMS(DBMS_UNKNOWN_VALUE);
	public static final DBMS DBMS_SAPDB = new DBMS(DBMS_SAPDB_VALUE);
	public static final DBMS DBMS_MCKOI = new DBMS(DBMS_MCKOI_VALUE);
	public static final DBMS DBMS_ORACLE = new DBMS(DBMS_ORACLE_VALUE);
	public static final DBMS DBMS_INFORMIX = new DBMS(DBMS_INFORMIX_VALUE);
	public static final DBMS DBMS_MYSQL = new DBMS(DBMS_MYSQL_VALUE);
	
	private int dbms = DBMS_UNKNOWN_VALUE;
	
	private DBMS(int value) {
		dbms = value;
	}
	
	public static DBMS getDBMS(String driverName) {
		if (driverName.indexOf(DRIVER_INFORMIX) >= 0) {
			return DBMS_INFORMIX;
		} else if (driverName.indexOf(DRIVER_ORACLE) >= 0) {
			return DBMS_ORACLE;
		} else if (driverName.indexOf(DRIVER_MCKOI) >= 0) {
			return DBMS_MCKOI;
		} else if (driverName.indexOf(DRIVER_SAPDB) >= 0) {
			return DBMS_SAPDB;
		} else if (driverName.indexOf(DRIVER_MYSQL) >= 0) {
			return DBMS_MYSQL;			
		} else {
			return DBMS_UNKNOWN;
		}
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (! (obj instanceof DBMS)) {
			return false;
		}
		
		DBMS other = (DBMS) obj;
		
		return dbms == other.dbms;
	}

	public String toString() {
		switch (dbms) {
		case DBMS_UNKNOWN_VALUE:
			return DRIVER_UNKNOWN;
		case DBMS_SAPDB_VALUE:
			return DRIVER_SAPDB;
		case DBMS_MCKOI_VALUE:
			return DRIVER_MCKOI;
		case DBMS_ORACLE_VALUE:
			return DRIVER_ORACLE;
		case DBMS_INFORMIX_VALUE:
			return DRIVER_INFORMIX;
		case DBMS_MYSQL_VALUE:
			return DRIVER_MYSQL;			
		}

		return null;
	}
}
