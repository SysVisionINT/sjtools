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
package net.java.sjtools.db.sql;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;

import net.java.sjtools.db.filter.Filter;
import net.java.sjtools.util.TextUtil;

public class SQLUtil {
	private static Map keywords = null;
	
	public static String getSelectForTable(String tableName, Filter filter) {
		if (TextUtil.isEmptyString(tableName)) {
			return null;
		}
		
		StringBuffer buffer = new StringBuffer();

		buffer.append("SELECT * FROM ");
		buffer.append(tableName);

		if (filter != null) {
			if (filter.hasWhere()) {
				buffer.append(" WHERE ");
				buffer.append(filter.getWhereSQL());
			}

			if (filter.hasOrder()) {
				buffer.append(" ORDER BY ");
				buffer.append(filter.getOrderSQL());
			}
		}

		return buffer.toString();
	}

	public static String getUpdatedSelect(String sql, Filter filter) {
		if (TextUtil.isEmptyString(sql) || filter == null) {
			return sql;
		}

		String workingSQL = cleanSQL(sql);

		String from = getSelectToFrom(workingSQL);
		String where = getWhereClause(workingSQL);
		String group = getGroupToHaving(workingSQL);
		String order = getOrderClause(workingSQL);

		StringBuffer buffer = new StringBuffer();

		buffer.append(from);

		if (filter.hasWhere()) {
			if (where == null) {
				buffer.append(" WHERE ");
			} else {
				buffer.append(where);

				buffer.append(" AND ");
			}

			buffer.append(filter.getWhereSQL());
		} else if (where != null) {
			buffer.append(where);
		}

		if (group != null) {
			buffer.append(group);
		}

		if (filter.hasOrder()) {
			buffer.append(" ORDER BY ");
			buffer.append(filter.getOrderSQL());
		} else if (order != null) {
			buffer.append(order);
		}

		return buffer.toString();
	}

	public static String cleanSQL(String sql) {
		if (TextUtil.isEmptyString(sql)) {
			return null;
		}
		
		String workingSQL = " ".concat(sql.trim());

		workingSQL = workingSQL.replace('\n', ' ');
		workingSQL = workingSQL.replace('\r', ' ');
		workingSQL = workingSQL.replace('\t', ' ');
		
		workingSQL = addSpaces(workingSQL, '(');
		workingSQL = addSpaces(workingSQL, ')');

		workingSQL = TextUtil.replace(workingSQL, keywords);
		
		while (workingSQL.indexOf("  ") != -1) {
			workingSQL = TextUtil.replace(workingSQL, "  ", " ");
		}

		return workingSQL.trim();
	}

    private static String addSpaces(String text, char searchChar) {
    	CharacterIterator iterator = new StringCharacterIterator(text);
    	
        StringBuffer buffer = new StringBuffer();

        for (char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
			if (c == searchChar) {
				buffer.append(" ");
			}
			
			buffer.append(c);
			
			if (c == searchChar) {
				buffer.append(" ");
			}
		}

        return buffer.toString();
    }
    
	private static String getOrderClause(String workingSQL) {
		int posORDER = workingSQL.indexOf(" ORDER BY ");

		if (posORDER < 0) {
			return null;
		}

		return workingSQL.substring(posORDER);
	}

	private static String getGroupToHaving(String workingSQL) {
		int posGROUP = workingSQL.indexOf(" GROUP BY ");

		if (posGROUP < 0) {
			return null;
		}

		int pos = workingSQL.indexOf(" ORDER BY ");

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(posGROUP, pos);
	}

	private static String getWhereClause(String workingSQL) {
		int posWHERE = workingSQL.indexOf(" WHERE ");

		if (posWHERE < 0) {
			return null;
		}

		int pos = workingSQL.indexOf(" GROUP BY ");

		if (pos < 0) {
			pos = workingSQL.indexOf(" ORDER BY ");
		}

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(posWHERE, pos);
	}

	private static String getSelectToFrom(String workingSQL) {
		int pos = workingSQL.indexOf(" WHERE ");

		if (pos < 0) {
			pos = workingSQL.indexOf(" GROUP BY ");
		}

		if (pos < 0) {
			pos = workingSQL.indexOf(" ORDER BY ");
		}

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(0, pos);
	}
	
	static {
		keywords = new HashMap();
		
		keywords.put(" select ", " SELECT ");
		keywords.put(" from ", " FROM ");
		keywords.put(" where ", " WHERE ");
		keywords.put(" group ", " GROUP ");
		keywords.put(" by ", " BY ");
		keywords.put(" having ", " HAVING ");
		keywords.put(" order ", " ORDER ");
		keywords.put(" and ", " AND ");
		keywords.put(" or ", " OR ");
		keywords.put(" like ", " LIKE ");
		keywords.put(" between ", " BETWEEN ");
		keywords.put(" not ", " NOT ");
		keywords.put(" in ", " IN ");
		keywords.put(" distinct ", " DISTINCT ");
	}
}
