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
package net.java.sjtools.frameworks.recordProcessor.formatters;

import java.io.Serializable;
import java.lang.reflect.Method;

import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;

public class CustomFormatter implements Formatter, Serializable {

	private static final long serialVersionUID = 5076022659942650928L;

	private Object customFormatterObject = null;
	private Method customFormatterFormat = null;

	public CustomFormatter(String javaClass, String formatMethod) throws ProcessorError {
		try {
			Class validatorClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);

			customFormatterObject = validatorClass.newInstance();

			customFormatterFormat = validatorClass.getMethod(formatMethod, new Class[] { String.class });

			if (customFormatterFormat.getReturnType() != String.class) {
				throw new ProcessorError("Method " + formatMethod + " of class " + javaClass + " has to return a String");
			}
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public String format(String value) throws ProcessorError {
		try {
			return (String) customFormatterFormat.invoke(customFormatterObject, new String[] { value });
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

}
