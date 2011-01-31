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
package net.java.sjtools.frameworks.recordProcessor.validators;

import java.io.Serializable;
import java.lang.reflect.Method;

import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;

public class CustomValidator implements Validator, Serializable {

	private static final long serialVersionUID = 8001151871676624067L;

	private Object customValidatorObject = null;
	private Method customValidatorMethod = null;

	public CustomValidator(String javaClass, String method) throws ProcessorError {
		try {
			Class validatorClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);
			customValidatorObject = validatorClass.newInstance();
			customValidatorMethod = validatorClass.getMethod(method, new Class[] { String.class });

			if (customValidatorMethod.getReturnType() != Boolean.TYPE && customValidatorMethod.getReturnType() != Boolean.class) {
				throw new ProcessorError("Method " + method + " of class " + javaClass + " has to return a (Bb)oolean");
			}
		} catch (ProcessorError e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public boolean isValid(String value) {
		try {
			return ((Boolean) customValidatorMethod.invoke(customValidatorObject, new Object[] { value })).booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "CustomValidator(" + customValidatorObject.getClass().getName() + ", " + customValidatorMethod.getName() + ")";
	}

}
