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
package net.java.sjtools.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class BeanUtil {
	private Object obj = null;

	public BeanUtil(Class clazz) throws InstantiationException, IllegalAccessException {
		obj = clazz.newInstance();
	}

	public BeanUtil(Object obj) {
		if (obj == null) {
			throw new NullPointerException("Can't create BeanUtil instance with null object");
		}

		this.obj = obj;
	}

	public String toString() {
		if (implementsMethod("toString", new Class[0])) {
			return obj.toString();
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(getClassName());
		buffer.append("(");

		Class clazz = obj.getClass();

		Method[] methods = clazz.getMethods();
		Method method = null;
		Object value = null;
		int count = 0;

		for (int i = 0; i < methods.length; i++) {
			method = methods[i];

			if (!(method.getName().startsWith("get") || method.getName().startsWith("is"))) {
				continue;
			}

			if (method.getDeclaringClass().equals(Object.class)) {
				continue;
			}

			int modifiers = method.getModifiers();

			if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isAbstract(modifiers)
					|| Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)) {
				continue;
			}

			if (method.getParameterTypes().length != 0) {
				continue;
			}

			try {
				value = method.invoke(obj, new Object[0]);

				if (count != 0) {
					buffer.append(", ");
				}

				buffer.append(getPropertyName(method.getName()));
				buffer.append("=");

				if (value != null) {
					if (value.getClass().isArray()) {
						buffer.append("[");

						for (int j = 0; j < Array.getLength(value); j++) {
							if (j != 0) {
								buffer.append(", ");
							}

							buffer.append(TextUtil.toString(Array.get(value, j)));
						}

						buffer.append("]");
					} else if (value instanceof List) {
						buffer.append("[");
						buffer.append(TextUtil.toString((List) value));
						buffer.append("]");
					} else if (value instanceof String) {
						buffer.append(value);
					} else if (value.getClass().isPrimitive()) {
						buffer.append(value);
					} else {
						buffer.append(TextUtil.toString(value));
					}
				} else {
					buffer.append("null");
				}

				count++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		buffer.append(")");

		return buffer.toString();
	}

	public boolean implementsMethod(String methodName, Class[] args) {
		try {
			obj.getClass().getDeclaredMethod(methodName, args);
		} catch (NoSuchMethodException e) {
			return false;
		}

		return true;
	}

	public Object invokeMethod(String methodName, Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class[] clazzs = new Class[args.length];

		for (int i = 0; i < args.length; i++) {
			clazzs[i] = args[i].getClass();
		}

		Method method = obj.getClass().getMethod(methodName, clazzs);

		return method.invoke(obj, args);
	}

	public int hashCode() {
		if (implementsMethod("hashCode", new Class[0])) {
			return obj.hashCode();
		}

		return toString().hashCode();
	}

	public String getClassName() {
		return obj.getClass().getName();
	}

	public Object get(String propertyName) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		return invokeMethod(getGetMethodName(propertyName), new Object[0]);
	}

	public String getPropertyName(String methodName) {
		String name = null;

		if (methodName.startsWith("get") || methodName.startsWith("set")) {
			name = methodName.substring(3);
		} else if (methodName.startsWith("is")) {
			name = methodName.substring(2);
		} else {
			name = methodName;
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(name.substring(0, 1).toLowerCase());

		if (name.length() > 1) {
			buffer.append(name.substring(1));
		}

		return buffer.toString();
	}

	public String getGetMethodName(String propertyName) {
		String name = getMethodName("get", propertyName);

		if (getMethods(name).isEmpty()) {
			String booleanName = getMethodName("is", propertyName);

			if (!getMethods(booleanName).isEmpty()) {
				name = booleanName;
			}
		}

		return name;
	}

	public List getMethods(String methodName) {
		List ret = new ArrayList();

		Method[] methods = obj.getClass().getMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(methodName)) {
				ret.add(methods[i]);
			}
		}

		return ret;
	}

	public String getMethodName(String prefix, String propertyName) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(prefix);

		buffer.append(propertyName.substring(0, 1).toUpperCase());

		if (propertyName.length() > 1) {
			buffer.append(propertyName.substring(1));
		}

		return buffer.toString();
	}

	public void set(String propertyName, Object value) throws NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		String name = getMethodName("set", propertyName);
		List list = getMethods(name);
		Method method = null;

		if (list.isEmpty()) {
			throw new NoSuchMethodException();
		}

		if (list.size() == 1) {
			method = (Method) list.get(0);
		} else {
			method = getSetMethod(name, value.getClass());

			if (method == null) {
				throw new NoSuchMethodException(name);
			}
		}

		Object[] args = new Object[1];
		args[0] = value;

		method.invoke(obj, args);
	}

	public Method getSetMethod(String methodName, Class clazz) throws NoSuchMethodException {
		Class[] classes = new Class[1];
		Class[] classesI = new Class[1];
		Class[] interfaces = null;
		Method method = null;

		classes[0] = clazz;

		while (method == null && classes[0] != null) {
			try {
				method = obj.getClass().getMethod(methodName, classes);
			} catch (NoSuchMethodException e) {
			}

			if (method == null) {
				interfaces = classes[0].getInterfaces();

				for (int i = 0; i < interfaces.length; i++) {
					classesI[0] = interfaces[i];

					try {
						method = obj.getClass().getMethod(methodName, classesI);
					} catch (NoSuchMethodException e) {
					}
				}
			}

			classes[0] = clazz.getSuperclass();
		}

		if (method == null) {
			throw new NoSuchMethodException(methodName);
		}

		return method;
	}
}