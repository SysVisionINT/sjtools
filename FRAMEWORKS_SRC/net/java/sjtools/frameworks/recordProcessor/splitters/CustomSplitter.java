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
package net.java.sjtools.frameworks.recordProcessor.splitters;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;

public class CustomSplitter implements Splitter, Serializable {

	private static final long serialVersionUID = 2799506306737427494L;

	private Object customSplitterObject = null;
	private Method customSplitterInit = null;
	private Method customSplitterNextRecord = null;

	public CustomSplitter(String javaClass, String initMethod, String nextRecordMethod) throws ProcessorError {
		try {
			Class validatorClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);

			customSplitterObject = validatorClass.newInstance();

			customSplitterInit = validatorClass.getMethod(initMethod, new Class[] { InputStream.class });

			customSplitterNextRecord = validatorClass.getMethod(nextRecordMethod, null);

			if (customSplitterNextRecord.getReturnType() != List.class) {
				throw new ProcessorError("Method " + nextRecordMethod + " of class " + javaClass + " has to return a List");
			}
		} catch (ProcessorError e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public void init(InputStream inputStream) throws ProcessorError {
		try {
			customSplitterInit.invoke(customSplitterObject, new Object[] { inputStream });
		} catch (Exception e) {
			throw new ProcessorError(e);
		}

	}

	public List nextRecord() throws ProcessorError {
		try {
			return (List) customSplitterNextRecord.invoke(customSplitterObject, null);
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public String toString() {
		return "CustomSplitter(" + customSplitterObject.getClass().getName() + ", " + customSplitterInit.getName() + ", " + customSplitterNextRecord.getName() + ")";
	}

}
