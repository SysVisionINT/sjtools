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

import net.java.sjtools.db.filter.util.FilterUtil;
import net.java.sjtools.util.BeanUtil;

public class Between extends Condition {
	private static final long serialVersionUID = -3508858224152814905L;

	public Between(String fieldName, Object object1, Object object2) {
		super(fieldName);
		addValue(object1);
		addValue(object2);
	}

	public void append(StringBuffer buffer) {
		buffer.append("(");
		buffer.append(fieldName);
		buffer.append(" BETWEEN ? AND ?");
		buffer.append(")");
	}

	public boolean isTrue(BeanUtil obj) throws Exception {
		Object value = BeanUtil.getPropertyValue(obj, fieldName);

		Object object1 = values.get(0);
		Object object2 = values.get(1);

		if (FilterUtil.isEqualTo(value, object1) || FilterUtil.isBiggerThan(value, object1)) {
			if (FilterUtil.isEqualTo(value, object2) || FilterUtil.isSmallerThan(value, object2)) {
				return true;
			}
		}

		return false;
	}
}
