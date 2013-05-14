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
package net.java.sjtools.messaging.util;

import java.util.Random;

public class ReferenceUtil {
	private static final int MAX_RANDOM = 1000000;
	private static final String CALL_PREFIX = "CALL-";
	private static final String ACTOR_PREFIX = "ACTOR-";
	
	private static Random random = new Random(System.currentTimeMillis());
	private static long seq = 0;
	
	public static String getMessageReference() {
		return getReference();
	}
	
	private static String getReference() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(Thread.currentThread().getName());
		buffer.append("-");
		buffer.append(String.valueOf(System.currentTimeMillis()));
		buffer.append("-");
		buffer.append(getSeq());
		buffer.append("-");
		buffer.append(random.nextInt(MAX_RANDOM));
		
		return String.valueOf(buffer.toString().hashCode());
	}
	
	private synchronized static String getSeq() {
		if (seq == Long.MAX_VALUE) {
			seq = 0;
		} else {
			seq++;
		}
		
		return String.valueOf(seq);
	}

	public static String getCallReference(String messageReference) {
		return CALL_PREFIX.concat(messageReference);
	}
	
	public static String getActorReference() {
		return ACTOR_PREFIX.concat(getReference());
	}
}
