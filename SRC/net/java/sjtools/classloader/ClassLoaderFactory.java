/*
 * SJTools - SysVision Java Tools
 *
 * Copyright (C) 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Inform�tica, Lda.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderFactory {

	public static SuperClassLoader getClassLoader(File[] jarList, ClassLoader parent) throws MalformedURLException {
		List loaderList = new ArrayList();

		File file = null;

		for (int i = 0; i < jarList.length; i++) {
			file = jarList[i];

			if (file.isDirectory()) {
				loaderList.add(file.toURI().toURL());
			} else {
				loaderList.add(file.toURI().toURL());
			}
		}

		return new SuperClassLoader((URL[]) loaderList.toArray(new URL[loaderList.size()]), parent);
	}
}
