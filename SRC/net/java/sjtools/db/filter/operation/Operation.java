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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.db.filter.Expression;

public abstract class Operation implements Expression {

	protected List expressions = new ArrayList();

	public void addExpression(Expression expression) {
		expressions.add(expression);
	}

	public void append(StringBuffer buffer) {
		if (expressions.isEmpty()) {
			return;
		}

		buffer.append("(");

		Expression expression = null;
		boolean first = true;

		for (Iterator i = expressions.iterator(); i.hasNext();) {
			expression = (Expression) i.next();

			if (first) {
				first = false;
			} else {
				buffer.append(" ");
				buffer.append(getOperator());
				buffer.append(" ");
			}

			expression.append(buffer);
		}

		buffer.append(")");
	}

	public void appendValues(List list) {
		if (expressions.isEmpty()) {
			return;
		}

		Expression expression = null;

		for (Iterator i = expressions.iterator(); i.hasNext();) {
			expression = (Expression) i.next();
			expression.appendValues(list);
		}

	}

	protected abstract String getOperator();
}
