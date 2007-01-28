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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
	public static String getRandomPassword(int size) {
		StringBuffer output = new StringBuffer();

		for (int i = 0; i < size; i++) {
			output.append(CypherUtil.getRandomChar());
		}

		return output.toString();
	}

	public static String passwordCypher(String password) throws NoSuchAlgorithmException, IOException {
		return passwordCypher(getRandomSalt(), getRandomSalt(), password);
	}
	
	public static char getRandomSalt() {
		return CypherUtil.getRandomChar();
	}

	public static String passwordCypher(char salt1, char salt2, String password) throws NoSuchAlgorithmException, IOException {
		char[] masterTable = { salt1, salt2 };

		StringBuffer output = new StringBuffer();
		output.append(salt1);
		output.append(salt2);
		output.append(Base64Util.encode(CypherUtil.digest(CypherUtil.xor(password, String.valueOf(masterTable)))));

		return output.toString();
	}

	public static boolean isEqual(String nonCipheredPassword, String cipheredPassword) throws NoSuchAlgorithmException, IOException {
		if (cipheredPassword == null || nonCipheredPassword == null) {
			return false;
		}

		if (cipheredPassword.length() <= 2) {
			return false;
		}

		String newCipheredPassword = passwordCypher(cipheredPassword.charAt(0), cipheredPassword.charAt(1), nonCipheredPassword);

		return newCipheredPassword.equals(cipheredPassword);
	}
}
