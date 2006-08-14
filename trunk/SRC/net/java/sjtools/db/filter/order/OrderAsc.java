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
package net.java.sjtools.db.filter.order;

import net.java.sjtools.db.filter.util.FilterUtil;
import net.java.sjtools.util.BeanUtil;

public class OrderAsc extends Order {
	private static final long serialVersionUID = -8940279653043174137L;

	public OrderAsc(String fieldName) {
		super(fieldName);
	}

	protected String getType() {
		return "ASC";
	}

	public int compare(BeanUtil obj1, BeanUtil obj2) throws Exception {
		Object value1 = obj1.get(fieldName);
		Object value2 = obj2.get(fieldName);
		
		if (value1 == null && value2 == null) {
			return 0;
		}
		
		if (value1 == null) {
			return -1;
		}
		
		if (value2 == null) {
			return 1;
		}		
		
		if (FilterUtil.isEqualTo(value1, value2)) {
			return 0;	
		}
		
		if (FilterUtil.isSmallerThan(value1, value2)) {
			return -1;	
		} else {
			return 1;
		}
	}
}
