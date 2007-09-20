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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MethodHandler implements MethodInterceptor {
	private Map methodMap = new HashMap();
	private Map classMap = new HashMap();

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object ret = null;

		MethodInvoker invoker = new MethodInvoker(proxy);

		DecoratorNode methodChain = (DecoratorNode) methodMap.get(method);
		DecoratorNode classChain = (DecoratorNode) classMap.get(obj.getClass().getSuperclass().getName());

		if (classChain != null) {
			if (methodChain != null) {
				ret = classChain.invoke(new MethodInvoker(methodChain, invoker), obj, args);
			} else {
				ret = classChain.invoke(invoker, obj, args);
			}
		} else {
			if (methodChain != null) {
				ret = methodChain.invoke(invoker, obj, args);
			} else {
				ret = invoker.invoke(obj, args);
			}
		}

		return ret;
	}

	public void addDecorator(Class clazz, MethodDecorator decorator) {
		DecoratorNode node = new DecoratorNode(decorator);
		DecoratorNode last = (DecoratorNode) classMap.get(clazz.getName());

		if (last == null) {
			classMap.put(clazz.getName(), node);
		} else {
			last.setNext(node);
		}
	}

	public void addDecorator(Method method, MethodDecorator decorator) {
		DecoratorNode node = new DecoratorNode(decorator);
		DecoratorNode last = (DecoratorNode) methodMap.get(method);

		if (last == null) {
			methodMap.put(method, node);
		} else {
			last.setNext(node);
		}
	}
	
	public void addDecorator(Class clazz, String methodName, Class [] types, MethodDecorator decorator) throws SecurityException, NoSuchMethodException {
		addDecorator(clazz.getMethod(methodName, types), decorator);
	}
}
