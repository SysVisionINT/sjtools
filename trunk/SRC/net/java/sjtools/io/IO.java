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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import net.java.sjtools.util.TextUtil;

public class IO {
	public static String currentCharSet = null;
	public static PrintStream out = null;
	public static PrintStream err = null;
	public static BufferedReader in = null;

	public synchronized static void setCharSet(String charSet) throws UnsupportedEncodingException {
		redirectStdOut(System.out, charSet);
		redirectStdErr(System.err, charSet);
		redirectStdIn(System.in, charSet);

		currentCharSet = charSet;
	}

	public synchronized static void redirectStdOut(OutputStream os, String charSet) throws UnsupportedEncodingException {
		out = new PrintStream(os, true, charSet);
	}

	public static void redirectStdOut(OutputStream os) throws UnsupportedEncodingException {
		redirectStdOut(os, getCurrentCharSet());
	}

	public synchronized static void redirectStdErr(OutputStream os, String charSet) throws UnsupportedEncodingException {
		err = new PrintStream(os, true, charSet);
	}

	public static void redirectStdErr(OutputStream os) throws UnsupportedEncodingException {
		redirectStdErr(os, getCurrentCharSet());
	}

	public synchronized static void redirectStdIn(InputStream is, String charSet) throws UnsupportedEncodingException {
		in = new BufferedReader(new InputStreamReader(is, charSet));
	}

	public static void redirectStdIn(InputStream is) throws UnsupportedEncodingException {
		redirectStdIn(is, getCurrentCharSet());
	}

	public static String getCurrentCharSet() {
		return currentCharSet;
	}

	public static String read() throws IOException {
		out.flush();
		err.flush();

		String line = in.readLine();

		if (line == null || line.length() == 0) {
			return null;
		} else {
			return line;
		}
	}
	
	public static void print(Object value) {
		IO.out.print(TextUtil.toString(value));
	}
	
	public static void println(Object value) {
		IO.out.println(TextUtil.toString(value));
	}
	
	public static void println() {
		IO.out.println();
	}

	public static String getDefaultSystemCharset() {
		String charSet = System.getProperty("file.encoding");

		if (charSet == null) {
			charSet = "UTF-8";
		}

		return charSet;
	}

	static {
		try {
			setCharSet(getDefaultSystemCharset());
		} catch (UnsupportedEncodingException e) {
			out = System.out;
			err = System.err;
			in = new BufferedReader(new InputStreamReader(System.in));

			e.printStackTrace(err);
		}
	}
}
