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

import java.util.regex.Pattern;

public class EmailUtil {
	
	/**
	 * Regras de validação:
	 *    pointFollowedByPoint - dois pontos (..) seguidos
	 *    pointFollowedByAt    - ponto seguido de arroba (.@)
	 *    atFollowedByPoint    - arroba seguida de ponto (@.)
	 *    regularEmailPattern  - abc2.d_x-p@zyx.ul ou abc@asd.info
	 */

	private static final Pattern pointFollowedByPoint = Pattern.compile(".+\\.\\..+");
	private static final Pattern pointFollowedByAt = Pattern.compile(".+\\.@.+");
	private static final Pattern atFollowedByPoint = Pattern.compile(".+@\\..+");
	private static final Pattern regularEmailPattern = Pattern.compile("^[a-zA-Z0-9]+[a-zA-Z0-9_.-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");


	public static boolean isValidEmailAddress(String email) {
		if (email == null) {
			return false;
		}

		email = email.trim();

		return true && 
			!pointFollowedByPoint.matcher(email).matches() && 
			!pointFollowedByAt.matcher(email).matches() && 
			!atFollowedByPoint.matcher(email).matches()	&& 
			regularEmailPattern.matcher(email).matches();
	}

}
