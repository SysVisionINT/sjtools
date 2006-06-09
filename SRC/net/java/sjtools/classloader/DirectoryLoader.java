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
package net.java.sjtools.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.util.StreamUtil;
import net.java.sjtools.util.TextUtil;

public class DirectoryLoader implements Loader {
	private String directoryName = null;

	public DirectoryLoader(String directory) {
		directoryName = directory;

		if (!directoryName.endsWith(System.getProperty("file.separator"))) {
			directoryName += System.getProperty("file.separator");
		}
	}

	public byte[] loadClassBytes(String className) {
		FileInputStream is = null;
		byte[] classBytes = null;

		try {
			String classPath = convertClassName(className);

			is = new FileInputStream(directoryName + classPath + ".class");

			if (is.available() > 0) {
				classBytes = new byte[is.available()];
				is.read(classBytes);
			}
		} catch (Exception e) {
		} finally {
			StreamUtil.close(is);
		}

		return classBytes;
	}

	private String convertClassName(String className) {
		List classPackages = TextUtil.split(className, ".");
		StringBuffer buffer = new StringBuffer();

		String separator = System.getProperty("file.separator");

		for (Iterator i = classPackages.iterator(); i.hasNext();) {
			if (buffer.length() != 0) {
				buffer.append(separator);
			}

			buffer.append(i.next());
		}

		return buffer.toString();
	}

	public URL findResource(String resourceName) {
		File file = new File(directoryName + resourceName);

		if (file.exists()) {
			try {
				return file.toURL();
			} catch (MalformedURLException e) {
			}
		}

		return null;
	}

	public String getName() {
		return directoryName;
	}
}