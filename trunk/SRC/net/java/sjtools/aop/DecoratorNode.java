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
package net.java.sjtools.aop;

public class DecoratorNode {
	private DecoratorNode next = null;
	private MethodDecorator decorator = null;
	
	public DecoratorNode(MethodDecorator decorator) {
		this.decorator = decorator;
	}

	public void setNext(DecoratorNode node) {
		next = node;
	}

	public Object invoke(MethodInvoker invoker, Object target, Object[] args) throws Throwable {
		if (next == null) {
			return decorator.invoke(invoker, target, args);
		} else {
			return decorator.invoke(new MethodInvoker(next, invoker), target, args);
		}
	}
}
