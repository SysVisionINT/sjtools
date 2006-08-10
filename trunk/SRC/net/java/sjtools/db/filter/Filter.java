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
package net.java.sjtools.db.filter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.db.filter.order.Order;
import net.java.sjtools.util.BeanUtil;

public class Filter implements Comparator {
	private Expression where = null;

	private List orderBy = new ArrayList();

	public void setWhere(Expression where) {
		this.where = where;
	}

	public void addOrder(Order order) {
		orderBy.add(order);
	}

	public boolean hasWhere() {
		return where != null;
	}

	public String getWhereSQL() {
		if (!hasWhere()) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();

		where.append(buffer);

		return buffer.toString();
	}

	public boolean hasOrder() {
		return !orderBy.isEmpty();
	}

	public String getOrderSQL() {
		if (!hasOrder()) {
			return null;
		}

		StringBuffer buffer = new StringBuffer();

		Order order = null;
		boolean first = true;

		for (Iterator i = orderBy.iterator(); i.hasNext();) {
			order = (Order) i.next();

			if (first) {
				first = false;
			} else {
				buffer.append(", ");
			}

			order.append(buffer);
		}

		return buffer.toString();
	}

	public void setValues(PreparedStatement stm, List values) throws SQLException {
		Object obj = null;
		int pos = 1;

		for (Iterator i = values.iterator(); i.hasNext();) {
			obj = i.next();

			if (obj instanceof Boolean) {
				stm.setBoolean(pos++, ((Boolean) obj).booleanValue());
			} else if (obj instanceof Byte) {
				stm.setByte(pos++, ((Byte) obj).byteValue());
			} else if (obj instanceof byte[]) {
				stm.setBytes(pos++, (byte[]) obj);
			} else if (obj instanceof Date) {
				stm.setDate(pos++, (Date) obj);
			} else if (obj instanceof Double) {
				stm.setDouble(pos++, ((Double) obj).doubleValue());
			} else if (obj instanceof Float) {
				stm.setFloat(pos++, ((Float) obj).floatValue());
			} else if (obj instanceof Integer) {
				stm.setInt(pos++, ((Integer) obj).intValue());
			} else if (obj instanceof Long) {
				stm.setLong(pos++, ((Long) obj).longValue());
			} else if (obj instanceof Short) {
				stm.setShort(pos++, ((Short) obj).shortValue());
			} else if (obj instanceof String) {
				stm.setString(pos++, (String) obj);
			} else if (obj instanceof Time) {
				stm.setTime(pos++, (Time) obj);
			} else if (obj instanceof Timestamp) {
				stm.setTimestamp(pos++, (Timestamp) obj);
			} else if (obj instanceof BigDecimal) {
				stm.setBigDecimal(pos++, (BigDecimal) obj);
			}
		}
	}

	public void setValues(PreparedStatement stm) throws SQLException {
		if (where != null) {
			List values = new ArrayList();

			where.appendValues(values);

			setValues(stm, values);
		}
	}

	public Collection apply(Collection input) throws Exception {
		List output = new ArrayList();

		if (hasWhere()) {
			Object obj = null;

			for (Iterator i = input.iterator(); i.hasNext();) {
				obj = i.next();

				if (where.isTrue(new BeanUtil(obj))) {
					output.add(obj);
				}
			}
		} else {
			output.addAll(input);
		}

		if (hasOrder()) {
			Collections.sort(output, this);
		}

		return output;
	}

	public boolean isValid(Object object) throws Exception {
		if (hasWhere()) {
			return where.isTrue(new BeanUtil(object));
		}

		return true;
	}

	public int compare(Object obj1, Object obj2) {
		BeanUtil bean1 = new BeanUtil(obj1);
		BeanUtil bean2 = new BeanUtil(obj2);

		Order order = null;
		int ret = 0;

		for (Iterator i = orderBy.iterator(); i.hasNext();) {
			order = (Order) i.next();

			try {
				ret = order.compare(bean1, bean2);
			} catch (Exception e) {
				throw new Error(e);
			}

			if (ret != 0) {
				break;
			}
		}

		return ret;
	}
}
