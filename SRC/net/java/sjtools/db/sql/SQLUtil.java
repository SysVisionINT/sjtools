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
import java.util.Iterator;
import java.util.Map;

import net.java.sjtools.db.filter.Filter;
import net.java.sjtools.util.SearchUtil;
import net.java.sjtools.util.TextUtil;

public class SQLUtil {
	private static final String ORDER_BY = " ORDER BY ";
	private static final String HAVING = " HAVING ";
	private static final String GROUP_BY = " GROUP BY ";
	private static final String WHERE = " WHERE ";
	private static final String FROM = " FROM ";
	private static final String SELECT = " SELECT ";
	
	private static Map keywords = null;
	private static SearchUtil search = SearchUtil.getInstance("(", ")");
	
	public static String getSelectForTable(String tableName, Filter filter) {
		if (TextUtil.isEmptyString(tableName)) {
			return null;
		}
		
		StringBuffer buffer = new StringBuffer();

		buffer.append("SELECT * FROM ");
		buffer.append(tableName);

		if (filter != null) {
			if (filter.hasWhere()) {
				buffer.append(WHERE);
				buffer.append(filter.getWhereSQL());
			}

			if (filter.hasOrder()) {
				buffer.append(ORDER_BY);
				buffer.append(filter.getOrderSQL());
			}
		}

		return buffer.toString();
	}
	
	public static String getSelectCount(String sql, Filter filter) {
		String workingSQL = " ".concat(cleanSQL(sql));

		String from = getFromClause(workingSQL);
		String where = getWhereClause(workingSQL);

		StringBuffer buffer = new StringBuffer();

		buffer.append(SELECT);
		buffer.append(" count(*) ");
		buffer.append(FROM);
		buffer.append(from);
		buffer.append(WHERE);

		if (filter != null && filter.hasWhere()) {
			if (where != null) {
				buffer.append("(");
				buffer.append(where);
				buffer.append(") AND ");
			}

			buffer.append("(");
			buffer.append(filter.getWhereSQL());
			buffer.append(")");
		} else if (where != null) {
			buffer.append(where);
		}

		return buffer.toString();
	}	

	public static String getUpdatedSelect(String sql, Filter filter) {
		if (TextUtil.isEmptyString(sql) || filter == null) {
			return sql;
		}

		String workingSQL = " ".concat(cleanSQL(sql));

		String select = getSelectClause(workingSQL);
		String from = getFromClause(workingSQL);
		String where = getWhereClause(workingSQL);
		String group = getGroupClause(workingSQL);
		String having = getHavingClause(workingSQL);
		String order = getOrderClause(workingSQL);

		StringBuffer buffer = new StringBuffer();

		buffer.append(SELECT);
		buffer.append(select);
		buffer.append(FROM);
		buffer.append(from);
		buffer.append(WHERE);

		if (filter.hasWhere()) {
			if (where != null) {
				buffer.append("(");
				buffer.append(where);
				buffer.append(") AND ");
			}

			buffer.append("(");
			buffer.append(filter.getWhereSQL());
			buffer.append(")");
		} else if (where != null) {
			buffer.append(where);
		}

		if (group != null) {
			buffer.append(GROUP_BY);
			buffer.append(group);
		}
		
		if (having != null) {
			buffer.append(HAVING);
			buffer.append(having);
		}

		if (filter.hasOrder()) {
			buffer.append(ORDER_BY);
			buffer.append(filter.getOrderSQL());
		} else if (order != null) {
			buffer.append(ORDER_BY);
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

		workingSQL = replaceKeywords(workingSQL);
		
		while (workingSQL.indexOf("  ") != -1) {
			workingSQL = TextUtil.replace(workingSQL, "  ", " ");
		}

		return workingSQL.trim();
	}

    private static String replaceKeywords(String sql) {
    	String workingSQL = sql.toLowerCase();
    	String retSQL = sql;
    	
    	StringBuffer buffer = new StringBuffer();
		String key = null;
		String value = null;

		for (Iterator i = keywords.keySet().iterator(); i.hasNext();) {
			key = (String) i.next();
			value = (String) keywords.get(key);
			
			buffer.setLength(0);

			int pos = 0;

			while ((pos = workingSQL.indexOf(key, pos)) != -1) {
				buffer.setLength(0);

				if (pos == 0) {
					buffer.append(value);
					buffer.append(retSQL.substring(pos + key.length()));
				} else {
					buffer.append(retSQL.substring(0, pos));
					buffer.append(value);
					buffer.append(retSQL.substring(pos + key.length()));
				}

				pos = pos + value.length();

				retSQL = buffer.toString();
			}
		}
		
		return retSQL;
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
    
	public static String getOrderClause(String workingSQL) {
		int posORDER = search.indexOf(workingSQL, ORDER_BY);

		if (posORDER < 0) {
			return null;
		}

		return workingSQL.substring(posORDER + 10);
	}

	public static String getGroupClause(String workingSQL) {
		int posGROUP = search.indexOf(workingSQL, GROUP_BY);

		if (posGROUP < 0) {
			return null;
		}

		int pos = search.indexOf(workingSQL, HAVING);
		
		if (pos < 0) {
			pos = search.indexOf(workingSQL, ORDER_BY);
		}

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(posGROUP + 10, pos);
	}
	
	public static String getHavingClause(String workingSQL) {
		int posHAVING = search.indexOf(workingSQL, HAVING);

		if (posHAVING < 0) {
			return null;
		}

		int pos = search.indexOf(workingSQL, ORDER_BY);

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(posHAVING + 8, pos);
	}	

	public static String getWhereClause(String workingSQL) {
		int posWHERE = search.indexOf(workingSQL, WHERE);

		if (posWHERE < 0) {
			return null;
		}

		int pos = search.indexOf(workingSQL, GROUP_BY);

		if (pos < 0) {
			pos = search.indexOf(workingSQL, ORDER_BY);
		}

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(posWHERE + 7, pos);
	}

	public static String getSelectClause(String workingSQL) {
		int posSELECT = search.indexOf(workingSQL, SELECT);

		if (posSELECT < 0) {
			return null;
		}
		
		int pos = search.indexOf(workingSQL, FROM);

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(posSELECT + 8, pos);
	}
	
	public static String getFromClause(String workingSQL) {
		int posFROM = search.indexOf(workingSQL, FROM);
		
		if (posFROM < 0) {
			return null;
		}

		int	pos = search.indexOf(workingSQL, WHERE);
		
		if (pos < 0) {
			pos = search.indexOf(workingSQL, GROUP_BY);
		}

		if (pos < 0) {
			pos = search.indexOf(workingSQL, ORDER_BY);
		}

		if (pos < 0) {
			pos = workingSQL.length();
		}

		return workingSQL.substring(posFROM + 6, pos);
	}	
	
	static {
		keywords = new HashMap();
		
		keywords.put(" select ", SELECT);
		keywords.put(" from ", FROM);
		keywords.put(" where ", WHERE);
		keywords.put(" group by ", GROUP_BY);
		keywords.put(" having ", HAVING);
		keywords.put(" order by ", ORDER_BY);
		keywords.put(" and ", " AND ");
		keywords.put(" or ", " OR ");
		keywords.put(" like ", " LIKE ");
		keywords.put(" between ", " BETWEEN ");
		keywords.put(" not ", " NOT ");
		keywords.put(" in ", " IN ");
		keywords.put(" distinct ", " DISTINCT ");
		keywords.put(" exists ", " EXISTS ");
		keywords.put(" null ", " NULL ");
	}
}
