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
package net.java.sjtools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Classe com utilit&aacute;rios para fechar Streams
 */
public class StreamUtil {

	/**
	 * Fecha um InputStream
	 * @param inputStream O InputStream a ser fechado
	 */
	public static void close(InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * Fecha um BufferedReader
	 * @param reader O BufferedReader a ser fechado
	 */
	public static void close(BufferedReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * Fecha um OutputStream
	 * @param outputStream O OutputStream a ser fechado
	 */
	public static void close(OutputStream outputStream) {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * Fecha um PrintWriter
	 * @param writer O PrintWriter a ser fechado
	 */
	public static void close(PrintWriter writer) {
		if (writer != null) {
			writer.close();
		}
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[65536];
		int count = 0;

		while ((count = input.read(buffer)) != -1) {
			output.write(buffer, 0, count);
		}
	}
}
