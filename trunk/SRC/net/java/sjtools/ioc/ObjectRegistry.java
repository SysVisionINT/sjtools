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
package net.java.sjtools.ioc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.ioc.error.ObjectCreationError;
import net.java.sjtools.ioc.error.ObjectNotFound;
import net.java.sjtools.ioc.error.ObjectRegestryError;
import net.java.sjtools.ioc.model.RegistryRecord;
import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.thread.Lock;
import net.java.sjtools.util.BeanUtil;

public class ObjectRegistry {
	public static final int OBLECT_NAME = 0;
	public static final int CLASS_NAME = 0;

	private static Log log = LogFactory.getLog(ObjectRegistry.class);

	private List objectMap = new ArrayList();
	private Lock lock = new Lock(objectMap);
	private ObjectLoader loader = null;

	public ObjectRegistry(ObjectLoader ol) {
		loader = ol;
	}

	public Object getObject(String objectName) throws ObjectRegestryError {
		if (log.isDebugEnabled()) {
			log.debug("getObject(" + objectName + ")");
		}

		Object object = get(objectName);

		if (object == null) {
			if (loader.isObjectDefined(objectName)) {
				object = loader.loadObject(objectName);
			}

			if (object == null) {
				throw new ObjectNotFound(objectName);
			}

			addObject(objectName, object);
		}

		if (log.isDebugEnabled()) {
			log.debug("getObject(" + objectName + ") = " + object.getClass().getName());
		}

		return object;
	}

	private RegistryRecord getRecord(String objectName) {
		RegistryRecord record = null;

		lock.getReadLock();

		int pos = objectMap.indexOf(new RegistryRecord(objectName, null));

		if (pos != -1) {
			record = (RegistryRecord) objectMap.get(pos);
		}

		lock.releaseLock();

		return record;
	}

	private Object get(String objectName) {
		RegistryRecord record = getRecord(objectName);

		if (record != null) {
			return record.getValue();
		}

		return null;
	}

	public Object getObject(Class clazz) throws ObjectRegestryError {
		return getObject(clazz.getName());
	}

	private void addObject(String objectName, Object object) {
		lock.getWriteLock();
		objectMap.add(new RegistryRecord(objectName, object));
		lock.releaseLock();
	}

	public void addAlias(String objectName, String alias) throws ObjectNotFound {
		RegistryRecord record = getRecord(objectName);

		if (record != null) {
			lock.getWriteLock();

			record.addName(alias);

			lock.releaseLock();
		} else {
			throw new ObjectNotFound(objectName);
		}
	}

	public void fillDependencies(String objectName, int type) throws ObjectRegestryError {
		Class[] parameters = null;
		Object[] value = null;
		String propertyName = null;
		Object parameterValue = null;

		Object object = getObject(objectName);
		Method[] methods = object.getClass().getMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("set")) {
				parameters = methods[i].getParameterTypes();

				if (parameters.length == 1) {
					propertyName = BeanUtil.getPropertyName(methods[i].getName());

					if (!isPropertyFillable(propertyName)) {
						continue;
					}

					if (type == OBLECT_NAME) {
						if (isObjectDefined(propertyName)) {
							parameterValue = getObject(propertyName);

							if (parameterValue != null) {
								try {
									value = new Object[1];
									value[0] = parameterValue;

									methods[i].invoke(object, value);
								} catch (Exception e) {
									throw new ObjectCreationError("Error invoking method " + methods[i].getName(), e);
								}
							}
						}
					}

					if (type == CLASS_NAME) {
						if (isObjectDefined(parameters[0])) {
							parameterValue = getObject(parameters[0]);

							if (parameterValue != null) {
								try {
									value = new Object[1];
									value[0] = parameterValue;

									methods[i].invoke(object, value);
								} catch (Exception e) {
									throw new ObjectCreationError("Error invoking method " + methods[i].getName(), e);
								}
							}
						}
					}
				}
			}
		}
	}

	protected boolean isPropertyFillable(String propertyName) {
		return true;
	}

	public boolean isObjectDefined(Class clazz) throws ObjectRegestryError {
		return isObjectDefined(clazz.getName());
	}

	public boolean isObjectDefined(String objectName) throws ObjectRegestryError {
		lock.getReadLock();

		int pos = objectMap.indexOf(objectName);

		lock.releaseLock();

		if (pos != -1) {
			return true;
		}

		return loader.isObjectDefined(objectName);
	}
}
