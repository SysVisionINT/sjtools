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
package net.java.sjtools.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CypherUtil {
	public static final String MESSAGE_DIGESTER = "SHA1";

	private static Random random = new Random(System.currentTimeMillis());
	
	public static int getRandomInt(int max) {
		return random.nextInt(max);
	}

	public static char getRandomChar() {
		return (char) Base64Util.MASTER_TABLE[getRandomInt(Base64Util.MASTER_TABLE.length)];
	}	

	public static byte[] xor(String source, String cypherKey) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter os = new OutputStreamWriter(out);
		
		xor(new StringReader(source), cypherKey, os);
		os.flush();
		
		return out.toByteArray();
	}
	
	public static byte[] xor(byte[] source, byte[] cypherKey) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter os = new OutputStreamWriter(out);
		
		xor(new InputStreamReader(new ByteArrayInputStream(source)), new String(cypherKey), os);
		os.flush();
		
		return out.toByteArray();
	}
	
	public static byte[] xor(byte[] source, String cypherKey) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter os = new OutputStreamWriter(out);
		
		xor(new InputStreamReader(new ByteArrayInputStream(source)), cypherKey, os);
		os.flush();

		return out.toByteArray();
	}
	
	public static void xor(Reader in, String cypherKey, Writer out) throws IOException {
		int index = 0;
		int c = 0;

		while ((c = in.read()) != -1) {
			out.write((c ^ cypherKey.charAt(index)));

			index++;

			if (index >= cypherKey.length()) {
				index = 0;
			}
		}
	}	
	
	public static byte[] digest(byte[] input) throws NoSuchAlgorithmException, IOException {
		return digest(new ByteArrayInputStream(input), MESSAGE_DIGESTER);
	}
	
	public static byte[] digest(InputStream in, String digesterName) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance(digesterName);
		
		byte [] buffer = new byte[1024];
		int count = 0;
		
		while ((count = in.read(buffer)) != -1) {
			md.update(buffer, 0, count);
		}
		
		return md.digest();
	}	
}
