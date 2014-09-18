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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.sjtools.method.MethodCache;

public class BeanUtil {

	private Object obj = null;
	private MethodCache cache = null;

	public BeanUtil(Class clazz) throws InstantiationException, IllegalAccessException {
		obj = clazz.newInstance();
	}

	public BeanUtil(Class clazz, MethodCache cache) throws InstantiationException, IllegalAccessException {
		this(clazz);
		this.cache = cache;
	}

	public BeanUtil(Object obj) {
		if (obj == null) {
			throw new NullPointerException("Can't create BeanUtil instance with null object");
		}

		this.obj = obj;
	}

	public BeanUtil(Object obj, MethodCache cache) {
		this(obj);
		this.cache = cache;
	}

	public Class[] getExtendsList() {
		List ret = new ArrayList();

		Class father = obj.getClass().getSuperclass();

		while (father != null && !father.equals(Object.class)) {
			ret.add(father);

			father = father.getSuperclass();
		}

		return (Class[]) ret.toArray(new Class[ret.size()]);
	}

	public boolean extendsClass(Class clazz) {
		Class[] fathers = getExtendsList();

		for (int i = 0; i < fathers.length; i++) {
			if (fathers[i].getName().equals(clazz.getName())) {
				return true;
			}
		}

		return false;
	}

	public boolean extendsClassFromPackageStartingWith(String packageName) {
		Class[] fathers = getExtendsList();

		for (int i = 0; i < fathers.length; i++) {
			if (fathers[i].getPackage() != null && fathers[i].getPackage().getName().startsWith(packageName)) {
				return true;
			}
		}

		return false;
	}

	public String toString() {
		return toString(true);
	}

	public String toString(boolean includePackage) {
		if (implementsMethod("toString", new Class[0])) {
			return obj.toString();
		}

		StringBuffer buffer = new StringBuffer();

		buffer.append(getClassName(includePackage));
		buffer.append("(");

		Class clazz = obj.getClass();

		Method[] methods = clazz.getMethods();
		Method method = null;
		Object value = null;
		int count = 0;
		List listedFields = new ArrayList();
		String fieldName = null;

		for (int i = 0; i < methods.length; i++) {
			method = methods[i];

			if (!(method.getName().startsWith("get") || method.getName().startsWith("is"))) {
				continue;
			}

			if (method.getDeclaringClass().equals(Object.class)) {
				continue;
			}

			int modifiers = method.getModifiers();

			if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)) {
				continue;
			}

			if (method.getParameterTypes().length != 0) {
				continue;
			}

			try {
				value = method.invoke(obj, new Object[0]);

				fieldName = BeanUtil.getPropertyName(method.getName());
				listedFields.add(fieldName);

				if (value != null && value.equals(obj)) {
					continue;
				}

				if (count != 0) {
					buffer.append(", ");
				}

				buffer.append(fieldName);
				buffer.append("=");
				appendObject(buffer, value, includePackage);

				count++;
			} catch (Exception e) {
				throw new RuntimeException("Unable to invoke method " + method.getName() + ".");
			}
		}

		Field[] fields = clazz.getFields();

		for (int i = 0; i < fields.length; i++) {

			if (listedFields.contains(fields[i].getName())) {
				continue;
			}

			if (count != 0) {
				buffer.append(", ");
			}

			try {
				value = fields[i].get(obj);

				buffer.append(fields[i].getName());
				buffer.append("=");
				appendObject(buffer, value, includePackage);

				count++;
			} catch (Exception e) {
				throw new RuntimeException("Unable to get the value from field " + fields[i].getName() + ".");
			}
		}

		buffer.append(")");

		return buffer.toString();
	}

	private void appendObject(StringBuffer buffer, Object value, boolean includePackage) {
		if (value != null) {
			if (value.getClass().isArray()) {
				buffer.append("[");

				for (int j = 0; j < Array.getLength(value); j++) {
					if (j != 0) {
						buffer.append(", ");
					}

					buffer.append(TextUtil.toString(Array.get(value, j), includePackage));
				}

				buffer.append("]");
			} else if (value instanceof Collection) {
				buffer.append("[");
				buffer.append(TextUtil.toString((Collection) value, includePackage));
				buffer.append("]");
			} else if (value instanceof Map) {
				buffer.append("{");
				buffer.append(TextUtil.toString((Map) value, includePackage));
				buffer.append("}");
			} else {
				buffer.append(TextUtil.toString(value, includePackage));
			}
		} else {
			buffer.append("null");
		}

	}

	public boolean implementsMethod(String methodName, Class[] args) {
		try {
			obj.getClass().getDeclaredMethod(methodName, args);
		} catch (NoSuchMethodException e) {
			return false;
		}

		return true;
	}

	public Object invokeMethod(String methodName, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = null;
		Class[] parameters = getClasses(args);

		if (cache != null) {
			method = cache.get(obj.getClass(), methodName, parameters);
		}

		if (method == null) {
			List methods = getMethods(methodName);

			if (methods.size() == 1) {
				method = (Method) methods.get(0);
			} else {
				try {
					method = obj.getClass().getMethod(methodName, parameters);
				} catch (NoSuchMethodException e) {
					methods = filterMethods(methods, args);

					if (methods.size() == 1) {
						method = (Method) methods.get(0);
					} else {
						throw e;
					}
				}
			}

			if (cache != null) {
				cache.add(obj.getClass(), methodName, parameters, method);
			}
		}

		return method.invoke(obj, args);
	}

	private List filterMethods(List methods, Object[] args) {
		List retList = new ArrayList();

		for (Iterator i = methods.iterator(); i.hasNext();) {
			Method method = (Method) i.next();

			Class[] parameters = method.getParameterTypes();

			if (parameters.length != args.length) {
				continue;
			}

			boolean ok = true;

			for (int j = 0; j < args.length && ok; j++) {
				if (!(parameters[j].isAssignableFrom(args[j].getClass()))) {
					ok = false;
				}
			}

			if (ok) {
				retList.add(method);
			}
		}

		return retList;
	}

	private Class[] getClasses(Object[] args) {
		Class[] clazzs = new Class[args.length];

		for (int i = 0; i < args.length; i++) {
			clazzs[i] = args[i].getClass();
		}
		return clazzs;
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

	public String getClassName(boolean includePackage) {
		String className = getClassName();

		if (includePackage) {
			return className;
		}

		int pos = className.lastIndexOf(".");

		if (pos < 0) {
			return className;
		}

		return className.substring(pos + 1);
	}

	public Object get(String propertyName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(getGetMethodName(propertyName), new Object[0]);
	}

	public static String getPropertyName(String methodName) {
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

	public void set(String propertyName, Object value) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String name = getMethodName("set", propertyName);

		Method method = null;

		if (cache != null && value != null) {
			method = cache.get(obj.getClass(), name, value.getClass());
		}

		if (method == null) {
			List list = getMethods(name);

			if (list.isEmpty()) {
				throw new NoSuchMethodException();
			}

			if (list.size() == 1) {
				method = (Method) list.get(0);
			} else {
				if (value == null) {
					throw new NoSuchMethodException(name);
				}

				method = getSetMethod(name, value.getClass());

				if (method == null) {
					throw new NoSuchMethodException(name);
				}
			}

			if (cache != null && value != null) {
				cache.add(obj.getClass(), name, value.getClass(), method);
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
				Class[] primitive = new Class[1];
				primitive[0] = getPrimitiveType(classes[0]);

				if (primitive[0] != null) {
					try {
						method = obj.getClass().getMethod(methodName, primitive);
					} catch (NoSuchMethodException e2) {}
				}
			}

			if (method == null) {
				interfaces = classes[0].getInterfaces();

				for (int i = 0; i < interfaces.length; i++) {
					classesI[0] = interfaces[i];

					try {
						method = obj.getClass().getMethod(methodName, classesI);
					} catch (NoSuchMethodException e) {
						Class[] primitive = new Class[1];
						primitive[0] = getPrimitiveType(classesI[0]);

						if (primitive[0] != null) {
							try {
								method = obj.getClass().getMethod(methodName, primitive);
							} catch (NoSuchMethodException e2) {}
						}
					}
				}
			}

			classes[0] = classes[0].getSuperclass();
		}

		if (method == null) {
			throw new NoSuchMethodException(methodName);
		}

		return method;
	}

	private Class getPrimitiveType(Class clazz) {
		Class primitive = null;

		if (clazz.equals(Integer.class)) {
			primitive = int.class;
		} else if (clazz.equals(Boolean.class)) {
			primitive = boolean.class;
		} else if (clazz.equals(Character.class)) {
			primitive = char.class;
		} else if (clazz.equals(Byte.class)) {
			primitive = byte.class;
		} else if (clazz.equals(Short.class)) {
			primitive = short.class;
		} else if (clazz.equals(Long.class)) {
			primitive = long.class;
		} else if (clazz.equals(Float.class)) {
			primitive = float.class;
		} else if (clazz.equals(Double.class)) {
			primitive = double.class;
		}

		return primitive;
	}

	public static BeanUtil getPropertyBean(BeanUtil beanUtil, String propertyName) throws Exception {
		BeanUtil bu = beanUtil;
		List list = TextUtil.split(propertyName, ".");

		Object obj = null;

		for (int i = 0; i < list.size() - 1; i++) {
			obj = bu.get((String) list.get(i));
			bu = new BeanUtil(obj, beanUtil.cache);
		}

		return bu;
	}

	public static Object getPropertyValue(BeanUtil beanUtil, String propertyName) throws Exception {
		List list = TextUtil.split(propertyName, ".");
		String pn = (String) list.get(list.size() - 1);

		BeanUtil bu = getPropertyBean(beanUtil, propertyName);

		return bu.get(pn);
	}

	public static Object getPropertyValue(MethodCache cache, Object obj, String propertyName) throws Exception {
		return getPropertyValue(new BeanUtil(obj, cache), propertyName);
	}

	public static Object[] primitiveArrayToObjectArray(Object primitiveArray) {
		Object[] array = null;

		int length = Array.getLength(primitiveArray);

		if (length == 0) {
			array = new Object[0];
		} else {
			Class type = Array.get(primitiveArray, 0).getClass();

			array = (Object[]) Array.newInstance(type, length);

			for (int i = 0; i < length; i++) {
				array[i] = Array.get(primitiveArray, i);
			}
		}

		return array;
	}

}