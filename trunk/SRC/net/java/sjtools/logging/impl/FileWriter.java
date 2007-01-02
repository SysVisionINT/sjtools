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
package net.java.sjtools.logging.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;

import net.java.sjtools.io.SuperFile;
import net.java.sjtools.logging.api.Config;
import net.java.sjtools.logging.api.Writer;
import net.java.sjtools.logging.error.LogRuntimeError;
import net.java.sjtools.time.SuperDate;

public class FileWriter implements Writer, Config, Serializable {
	private static final long serialVersionUID = -6035365141449424419L;

	private static final String FILE_NAME = "sjtools.logging.fileWriter.fileName";
	private static final String DATE_PATTERN = "sjtools.logging.fileWriter.datePattern";

	private String fileName = null;
	private String datePattern = null;
	private SuperDate lastDate = null;

	private PrintWriter printWriter = null;

	public synchronized void print(String text) {
		println(text);
		flush();
	}

	private void init() {
		SuperDate now = new SuperDate();

		if (printWriter == null) {
			createFile();
		}

		if (!lastDate.getFormatedDate(datePattern).equals(now.getFormatedDate(datePattern))) {
			printWriter.flush();
			printWriter.close();

			File file = new File(fileName);

			file.renameTo(new File(file.getParentFile(), file.getName().concat(lastDate.getFormatedDate(datePattern))));

			createFile();
		}

		lastDate = now;
	}

	private void createFile() {
		try {
			SuperFile file = new SuperFile(fileName);

			if (file.exists()) {
				lastDate = new SuperDate(file.lastModified());
			} else {
				lastDate = new SuperDate();
			}

			printWriter = file.getWriterAppender();
		} catch (FileNotFoundException e) {
			throw new LogRuntimeError(e);
		}
	}

	private void println(String text) {
		init();

		printWriter.println(text);
	}

	public synchronized void print(String text, Throwable throwable) {
		println(text);
		throwable.printStackTrace(printWriter);
		flush();
	}

	public String[] getConfigParameters() {
		String[] args = { FILE_NAME, DATE_PATTERN };

		return args;
	}

	public void setConfigParameter(String name, String value) {
		if (name.equals(FILE_NAME)) {
			fileName = value;
		} else if (name.equals(DATE_PATTERN)) {
			datePattern = value;
		}
	}

	private void flush() {
		printWriter.flush();
	}
}
