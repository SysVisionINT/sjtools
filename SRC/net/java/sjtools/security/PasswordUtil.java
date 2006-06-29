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

import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
	private static final char DEFAULT_SALT = '=';

	public static String getRandomPassword(int size) {
		StringBuffer output = new StringBuffer();

		for (int i = 0; i < size; i++) {
			output.append(CypherUtil.getRandomChar());
		}

		return output.toString();
	}

	public static String passwordCypher(String password) throws NoSuchAlgorithmException {
		return passwordCypher(DEFAULT_SALT, password);
	}

	public static String passwordCypher(char salt, String password) throws NoSuchAlgorithmException {
		byte[] masterTable = { (byte) salt };
		byte[] saltedPassword = CypherUtil.xor(password.getBytes(), masterTable);
		byte[] cypher = CypherUtil.digest(saltedPassword);

		StringBuffer output = new StringBuffer();
		output.append(salt);
		output.append(CypherUtil.getReadableText(cypher));

		return output.toString();
	}

	public static boolean isEqual(String nonCipheredPassword, String cipheredPassword) throws NoSuchAlgorithmException {
		if (cipheredPassword == null || nonCipheredPassword == null) {
			return false;
		}

		if (cipheredPassword.length() <= 1) {
			return false;
		}

		String newCipheredPassword = passwordCypher(cipheredPassword.charAt(0), nonCipheredPassword);

		return newCipheredPassword.equals(cipheredPassword);
	}
}
