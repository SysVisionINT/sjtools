/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2009 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
package net.java.sjtools.frameworks.recordProcessor.util;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.java.sjtools.frameworks.recordProcessor.model.FieldAndMethod;
import net.java.sjtools.frameworks.recordProcessor.model.error.ObjectCreationError;
import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;
import net.java.sjtools.time.SuperDate;

public class SimpleBeanUtil {

	private Class beanClass = null;
	private Object bean = null;

	private Map fieldAndMethodMap = new HashMap();
	private Map dateFormatMap = new HashMap();
	private Map stringConstructorMap = new HashMap();
	private Map stringStringConstructorMap = new HashMap();

	public SimpleBeanUtil(String javaClass) throws ProcessorError {
		try {
			beanClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public void initialize() throws ProcessorError {
		try {
			bean = beanClass.newInstance();
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public void set(String value, String property, String format) throws ProcessorError {
		try {
			FieldAndMethod fieldAndMethod = getFieldAndMethod(property);

			Object argument = getNewInstance(fieldAndMethod.getField().getType(), value, format);

			fieldAndMethod.getMethod().invoke(bean, new Object[] { argument });
		} catch (ProcessorError e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public Object getBean() {
		return bean;
	}

	private String getMethodName(String property) {
		return "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
	}

	private Object getNewInstance(Class objClass, String objValue, String objFormat) throws Exception {
		// Check if it is a String
		if (objClass == String.class) {
			return objValue;
		}

		// Check if it is a primitive or a primitive wrapper.
		// Primitive types: boolean, byte, char, short, int, long, float, and double (Doc java 1.4.2)
		if (objClass == Boolean.class || objClass == Boolean.TYPE) {
			return new Boolean(objValue);
		}

		if (objClass == Byte.class || objClass == Byte.TYPE) {
			return new Byte(objValue);
		}

		if (objClass == Character.class || objClass == Character.TYPE) {
			return new Character(objValue.charAt(0));
		}

		if (objClass == Short.class || objClass == Short.TYPE) {
			return new Short(objValue);
		}

		if (objClass == Integer.class || objClass == Integer.TYPE) {
			return new Integer(objValue);
		}

		if (objClass == Long.class || objClass == Long.TYPE) {
			return new Long(objValue);
		}

		if (objClass == Float.class || objClass == Float.TYPE) {
			return new Float(objValue);
		}

		if (objClass == Double.class || objClass == Double.TYPE) {
			return new Double(objValue);
		}

		// Special cases:
		// 1) Dates
		if (objClass == SuperDate.class || objClass == Calendar.class || objClass == Date.class || objClass.getSuperclass() == Date.class) {
			SimpleDateFormat simpleDateFormat = getSimpleDateFormat(objFormat);

			Date date = simpleDateFormat.parse(objValue);

			if (simpleDateFormat.format(date).equals(objValue)) {
				if (objClass == Date.class) {
					return date;
				}

				if (objClass == Calendar.class) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					return calendar;
				}

				if (objClass == java.sql.Date.class) {
					return new java.sql.Date(date.getTime());
				}

				SuperDate superDate = new SuperDate(date.getTime());

				return superDate;
			} else {
				throw new ObjectCreationError(objClass.getName(), objValue, objFormat);
			}
		}

		// 2) class has to have a constructor (String value) or (String value, String format) - in this order
		Constructor constructor = null;
		try {
			constructor = getStringConstructor(objClass);

			return constructor.newInstance(new String[] { objValue });
		} catch (NoSuchMethodException e) {
			try {
				constructor = getStringStringConstructor(objClass);

				return constructor.newInstance(new String[] { objValue, objFormat });
			} catch (NoSuchMethodException e1) {
				throw new ObjectCreationError(objClass.getName(), objValue, objFormat);
			}
		}
	}

	private FieldAndMethod getFieldAndMethod(String property) throws SecurityException, NoSuchFieldException, NoSuchMethodException {
		FieldAndMethod fieldAndMethod = (FieldAndMethod) fieldAndMethodMap.get(property);

		if (fieldAndMethod == null) {
			fieldAndMethod = new FieldAndMethod();

			fieldAndMethod.setField(beanClass.getDeclaredField(property));

			fieldAndMethod.setMethod(beanClass.getMethod(getMethodName(property), new Class[] { fieldAndMethod.getField().getType() }));

			fieldAndMethodMap.put(property, fieldAndMethod);
		}

		return fieldAndMethod;
	}

	private SimpleDateFormat getSimpleDateFormat(String objFormat) throws Exception {
		SimpleDateFormat simpleDateFormat = (SimpleDateFormat) dateFormatMap.get(objFormat);

		if (simpleDateFormat == null) {
			simpleDateFormat = new SimpleDateFormat(objFormat);
			dateFormatMap.put(objFormat, simpleDateFormat);
		}

		return simpleDateFormat;
	}

	private Constructor getStringConstructor(Class objClass) throws SecurityException, NoSuchMethodException {
		String objClassName = objClass.getName();

		Constructor constructor = (Constructor) stringConstructorMap.get(objClassName);

		if (constructor == null) {
			constructor = objClass.getConstructor(new Class[] { String.class });
			stringConstructorMap.put(objClassName, constructor);
		}

		return constructor;
	}

	private Constructor getStringStringConstructor(Class objClass) throws SecurityException, NoSuchMethodException {
		String objClassName = objClass.getName();

		Constructor constructor = (Constructor) stringStringConstructorMap.get(objClassName);

		if (constructor == null) {
			constructor = objClass.getConstructor(new Class[] { String.class, String.class });
			stringStringConstructorMap.put(objClassName, constructor);
		}

		return constructor;
	}

}
