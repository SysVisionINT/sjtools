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
package net.java.sjtools.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.util.StreamUtil;

public class SuperFile extends File {

	private static final long serialVersionUID = -615070155290162604L;

	public SuperFile(String pathname) {
		super(pathname);
	}

	public SuperFile(String parent, String child) {
		super(parent, child);
	}

	public SuperFile(File parent, String child) {
		super(parent, child);
	}

	public BufferedInputStream getInputStream() throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(this));
	}

	public BufferedOutputStream getOutputStream() throws FileNotFoundException {
		return getOutputStream(false);
	}

	public BufferedOutputStream getOutputStream(boolean appender) throws FileNotFoundException {
		return new BufferedOutputStream(new FileOutputStream(this, appender));
	}

	public PrintWriter getWriter() throws FileNotFoundException {
		return new PrintWriter(getOutputStream());
	}

	public PrintWriter getWriterAppender() throws FileNotFoundException {
		return new PrintWriter(getOutputStream(true));
	}

	public PrintWriter getWriter(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		return new PrintWriter(new OutputStreamWriter(getOutputStream(), charset));
	}

	public PrintWriter getWriterAppender(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		return new PrintWriter(new OutputStreamWriter(getOutputStream(true), charset));
	}

	public BufferedReader getReader() throws FileNotFoundException {
		return new BufferedReader(new FileReader(this));
	}

	public BufferedReader getReader(String charset) throws FileNotFoundException, UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(new FileInputStream(this), charset));
	}

	public boolean copyTo(File pathName) {
		boolean ret = false;

		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = getInputStream();
			out = new BufferedOutputStream(new FileOutputStream(pathName));

			int c;

			while ((c = in.read()) != -1) {
				out.write(c);
			}

			out.flush();

			ret = true;
		} catch (Exception e) {
			ret = false;
		} finally {
			StreamUtil.close(in);
			StreamUtil.close(out);
		}

		return ret;
	}

	public boolean moveTo(File pathName) {
		boolean ret = this.renameTo(pathName);

		if (!ret) {
			ret = this.copyTo(pathName);

			if (ret) {
				pathName.setLastModified(this.lastModified());

				if (!this.canWrite()) {
					pathName.setReadOnly();
				}

				ret = this.delete();
			}
		}

		return ret;
	}

	public List listSuperFiles() {
		List fl = new ArrayList();
		String[] sa = super.list();

		if (sa != null) {
			for (int i = 0; i < sa.length; i++) {
				fl.add(new SuperFile(this.getAbsolutePath(), sa[i]));
			}
		}

		return fl;
	}

	public List listSuperFiles(FileFilter fileFilter) {
		List fl = new ArrayList();
		File[] files = super.listFiles(fileFilter);

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				fl.add(new SuperFile(files[i].getAbsolutePath()));
			}
		}

		return fl;
	}

}
