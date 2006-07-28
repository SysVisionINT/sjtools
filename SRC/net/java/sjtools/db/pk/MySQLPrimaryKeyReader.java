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
package net.java.sjtools.db.pk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.java.sjtools.db.DBUtil;

public class MySQLPrimaryKeyReader implements NativePrimaryKeyReader {
	
	private static final String SQL_GENERATED_ID = "SELECT LAST_INSERT_ID()";

	public long getKey(Connection con) throws SQLException {
		long ret = 0;
		
        PreparedStatement ps = null;
        ResultSet rs = null;

       try {
            ps = con.prepareStatement(SQL_GENERATED_ID, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            rs = ps.executeQuery();
        
            if (rs.next()) {
                ret = rs.getLong(1);
            }
        } finally {
            DBUtil.close(rs);
            DBUtil.close(ps);
        }
		
		return ret;
	}
}
