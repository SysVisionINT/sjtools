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
package net.java.sjtools.db.filter.operation;

import java.util.Iterator;

import net.java.sjtools.db.filter.Expression;
import net.java.sjtools.util.BeanUtil;

public class Or extends Operation {
	private static final long serialVersionUID = 8828849031336430030L;

	protected String getOperator() {
		return "OR";
	}
	
	public boolean isTrue(BeanUtil obj) throws Exception {
		if (expressions.isEmpty()) {
			return true;
		}
		
		Expression expression = null;

		for (Iterator i = expressions.iterator(); i.hasNext();) {
			expression = (Expression) i.next();

			if (expression.isTrue(obj)) {
				return true;
			}
		}

		return false;
	}	
}
