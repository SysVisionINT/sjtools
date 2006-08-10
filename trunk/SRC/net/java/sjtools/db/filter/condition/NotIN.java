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
package net.java.sjtools.db.filter.condition;

import java.util.Iterator;
import java.util.List;

import net.java.sjtools.util.BeanUtil;

public class NotIN extends Condition {
	public NotIN(String fieldName, List list) {
		super(fieldName);
		values.addAll(list);
	}

	public void append(StringBuffer buffer) {
		buffer.append(fieldName);
		buffer.append(" NOT IN (");

		boolean first = true;

		for (Iterator i = values.iterator(); i.hasNext();) {
			if (first) {
				first = false;
			} else {
				buffer.append(",");
			}

			buffer.append("?");
		}

		buffer.append(")");
	}
	
	public boolean isTrue(BeanUtil obj) throws Exception {
		Object value = obj.get(fieldName);

		return !values.contains(value);
	}	
}
