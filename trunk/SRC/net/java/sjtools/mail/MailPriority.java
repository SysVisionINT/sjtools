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
package net.java.sjtools.mail;

public class MailPriority {
	public static final MailPriority LOW = new MailPriority(5);
	public static final MailPriority NORMAL = new MailPriority(3);
	public static final MailPriority HIGH = new MailPriority(1);
	
	private int priority = 0;
	
	private  MailPriority(int value) {
		priority = value;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false; 
		}
		
		if (!(obj instanceof MailPriority)) {
			return false;
		}
		
		MailPriority other = (MailPriority) obj;
		
		return priority == other.priority;
	}

	public String toString() {
		return String.valueOf(priority);
	}
}
