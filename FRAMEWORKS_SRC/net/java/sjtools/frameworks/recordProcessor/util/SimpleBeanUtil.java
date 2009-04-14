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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import net.java.sjtools.frameworks.recordProcessor.model.error.ObjectCreationError;
import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;
import net.java.sjtools.time.SuperDate;

public class SimpleBeanUtil {

	private Class beanClass = null;
	private Object bean = null;

	public SimpleBeanUtil(String javaClass) throws ProcessorError {
		try {
			beanClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);
			bean = beanClass.newInstance();
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public void set(String value, String property, String format) throws ProcessorError {
		try {
			Field field = beanClass.getDeclaredField(property);

			Method method = beanClass.getMethod(getMethodName(property), new Class[] { field.getType() });

			Object argument = getNewInstance(field.getType(), value, format);

			method.invoke(bean, new Object[] { argument });
		} catch (ProcessorError e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
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
		// 1) if the class is Timestamp, Date, Calendar, etc, first create a SuperDate then the
		//    proper class
		if (objClass == SuperDate.class || objClass == Calendar.class || objClass == Date.class || objClass.getSuperclass() == Date.class) {
			SuperDate tmpDate = new SuperDate(objValue, objFormat);

			if (objClass == Calendar.class) {
				return tmpDate.getCalendar();
			}

			if (objClass == java.sql.Date.class) {
				return new java.sql.Date(tmpDate.getTime());
			}

			return tmpDate;
		}

		// 2) class has to have a constructor (String value) or (String value, String format) - in this order
		Constructor constructor = null;
		try {
			constructor = objClass.getConstructor(new Class[] { String.class });

			return constructor.newInstance(new String[] { objValue });
		} catch (NoSuchMethodException e) {
			try {
				constructor = objClass.getConstructor(new Class[] { String.class, String.class });

				return constructor.newInstance(new String[] { objValue, objFormat });
			} catch (NoSuchMethodException e1) {
				throw new ObjectCreationError(objClass.getName(), objValue, objFormat);
			}
		}
	}

	public Object getBean() {
		return bean;
	}
}
