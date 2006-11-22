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
package net.java.sjtools.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Random;

public class CypherUtil {
	private static final byte[] MASTER_TABLE = { 0x2f, 0x32, 0x33, 0x75, 0x59, 0x47, 0x34, 0x61, 0x69, 0x61, 0x42,
			0x4c, 0x4b, 0x4e, 0x2e, 0x71, 0x6a, 0x50, 0x44, 0x39, 0x31, 0x4d, 0x73, 0x6e, 0x4f, 0x65, 0x38, 0x32, 0x55,
			0x6c, 0x63, 0x6f, 0x37, 0x34, 0x4e, 0x48, 0x74, 0x58, 0x6c, 0x41, 0x4e, 0x56, 0x44, 0x76, 0x4a, 0x46, 0x59,
			0x53, 0x79, 0x61, 0x69, 0x7a, 0x61, 0x67, 0x4f, 0x58, 0x50, 0x78, 0x54, 0x62, 0x4b, 0x64, 0x51, 0x65, 0x35,
			0x67, 0x51, 0x74, 0x6a, 0x36, 0x5a, 0x41, 0x39, 0x65, 0x36, 0x77, 0x6b, 0x30, 0x43, 0x32, 0x6d, 0x63, 0x4d,
			0x5a, 0x6b, 0x38, 0x30, 0x4a, 0x57, 0x72, 0x31, 0x30, 0x49, 0x66, 0x57, 0x78, 0x7a, 0x53, 0x6e, 0x53, 0x31,
			0x51, 0x78, 0x6c, 0x56, 0x66, 0x37, 0x79, 0x4c, 0x71, 0x47, 0x65, 0x2e, 0x50, 0x30, 0x6b, 0x41, 0x2f, 0x43,
			0x4d, 0x48, 0x47, 0x6c, 0x2e, 0x47, 0x37, 0x63, 0x77, 0x71, 0x57, 0x39, 0x38, 0x56, 0x52, 0x36, 0x49, 0x66,
			0x75, 0x4c, 0x35, 0x59, 0x74, 0x49, 0x31, 0x79, 0x67, 0x6e, 0x2f, 0x74, 0x4e, 0x75, 0x68, 0x5a, 0x42, 0x45,
			0x38, 0x54, 0x34, 0x77, 0x4c, 0x35, 0x49, 0x64, 0x58, 0x46, 0x6f, 0x68, 0x68, 0x55, 0x62, 0x37, 0x6d, 0x2e,
			0x4a, 0x6d, 0x7a, 0x76, 0x52, 0x69, 0x45, 0x59, 0x52, 0x33, 0x70, 0x35, 0x43, 0x54, 0x72, 0x6e, 0x76, 0x72,
			0x5a, 0x6f, 0x70, 0x69, 0x6a, 0x67, 0x2f, 0x64, 0x6b, 0x76, 0x72, 0x71, 0x63, 0x66, 0x41, 0x53, 0x4a, 0x43,
			0x75, 0x70, 0x46, 0x51, 0x4b, 0x6d, 0x4f, 0x42, 0x73, 0x46, 0x58, 0x68, 0x7a, 0x39, 0x52, 0x4d, 0x78, 0x62,
			0x55, 0x44, 0x4f, 0x42, 0x6a, 0x62, 0x48, 0x4b, 0x48, 0x55, 0x79, 0x77, 0x73, 0x33, 0x70, 0x64, 0x50, 0x73,
			0x34, 0x56, 0x45, 0x54, 0x57, 0x36, 0x44, 0x6f, 0x33, 0x45, 0x32 };
	
	private static final String MESSAGE_DIGESTER = "MD5";

	private static Random random = new Random(System.currentTimeMillis());

	public static char getRandomSalt() {
		return getRandomChar();
	}
	
	public static int getRandomInt(int max) {
		return random.nextInt(max);
	}

	public static char getRandomChar() {
		return (char) MASTER_TABLE[getRandomInt(MASTER_TABLE.length)];
	}
	
	public static String getReadableText(byte[] stringBytes) {
		byte[] cypher = new byte[stringBytes.length];
		int chr;

		for (int i = 0; i < cypher.length; i++) {
			chr = stringBytes[i];

			if (chr < 0) {
				chr = (-1) * chr;
			}

			cypher[i] = MASTER_TABLE[chr];
		}

		return new String(cypher);
	}

	public static String xor(String text, String cypherKey) {
		CharacterIterator iterator = new StringCharacterIterator(text);
		char[] master = cypherKey.toCharArray();

		StringBuffer buffer = new StringBuffer();

		char kode = 0;
		int index = 0;

		for (char c = iterator.first(); c != CharacterIterator.DONE; c = iterator.next()) {
			kode = (char) (c ^ master[index]);
			buffer.append(kode);

			index++;

			if (index >= master.length) {
				index = 0;
			}
		}

		return buffer.toString();
	}

	public static byte[] xor(byte[] text, byte[] cypherKey) {
		byte[] output = new byte[text.length];
		int index = 0;

		for (int i = 0; i < text.length; i++) {
			output[i] = (byte) (text[i] ^ cypherKey[index]);

			index++;

			if (index >= cypherKey.length) {
				index = 0;
			}
		}

		return output;
	}
	
	public static String digest(String text) throws NoSuchAlgorithmException {
		return getReadableText(digest(text.getBytes()));
	}
	
	public static byte[] digest(byte[] input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(MESSAGE_DIGESTER);
		return md.digest(input);
	}
}