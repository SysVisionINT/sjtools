package net.java.sjtools.frameworks.file.processor.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;
import net.java.sjtools.frameworks.file.processor.model.error.ObjectCreationError;
import net.java.sjtools.time.SuperDate;

public class SimpleBeanUtil {

	private Class beanClass = null;
	private Object bean = null;

	public SimpleBeanUtil(String javaClass) throws FileUploadError {
		try {
			beanClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);
			bean = beanClass.newInstance();
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public void set(String value, String property, String format) throws FileUploadError {
		try {
			Field field = beanClass.getDeclaredField(property);

			Method method = beanClass.getMethod(getMethodName(property), new Class[] { field.getType() });

			Object argument = getNewInstance(field.getType(), value, format);

			method.invoke(bean, new Object[] { argument });
		} catch (FileUploadError e) {
			throw e;
		} catch (Exception e) {
			throw new FileUploadError(e);
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
