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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ObjectFactory {
	private static final char[] HEXCHAR =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final String HEXINDEX = "0123456789abcdef          ABCDEF";

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();

		ObjectOutputStream os = new ObjectOutputStream(bo);
		os.writeObject(obj);

		return bo.toByteArray();
	}

	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bi = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(bi);

		return is.readObject();
	}

	public static String serializeToString(Object obj) throws IOException {
		return byteToHex(serialize(obj));
	}

	public static Object deserializeFromString(String data) throws IOException, ClassNotFoundException {
		return deserialize(hexToByte(data));
	}

	public static byte[] hexToByte(String hexString) {
		byte[] data = new byte[hexString.length() / 2];
		int hexStringIndex = 0;
		
		char character;
		int value;
		int currentByte;
		
		for (int byteIndex = 0; byteIndex < data.length; byteIndex++) {
			character = hexString.charAt(hexStringIndex++);
			
			value = HEXINDEX.indexOf(character);
			currentByte = (value & 0xf) << 4;
			
			character = hexString.charAt(hexStringIndex++);
			value = HEXINDEX.indexOf(character);
			
			currentByte += (value & 0xf);
			
			data[byteIndex] = (byte)currentByte;
		}

		return data;
	}

	public static String byteToHex(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		
		int currentByte;
		
		for (int i = 0; i < bytes.length; i++) {
			currentByte = ((int)bytes[i]) & 0xff;
			
			buffer.append(HEXCHAR[(currentByte >> 4) & 0xf]);
			buffer.append(HEXCHAR[currentByte & 0xf]);
		}

		return buffer.toString();
	}
	
	public static byte[] digest(Object obj) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		return md.digest(serialize(obj));
	}

	public static String digestToString(Object obj) throws NoSuchAlgorithmException, IOException {
		return byteToHex(digest(obj));
	}
}
