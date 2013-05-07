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
package net.java.sjtools.util;

public class Counter {
	private long value = 0;
	
	public synchronized void increment() {
		value++;
	}
	
	public synchronized void increment(int value) {
		value += value;
	}
	
	public synchronized void decrement() {
		value--;
	}
	
	public synchronized void decrement(int value) {
		value -= value;
	}	
	
	public synchronized long getValue() {
		return value;
	}

	public String toString() {
		return String.valueOf(getValue());
	}
}
