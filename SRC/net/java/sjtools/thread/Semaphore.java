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
package net.java.sjtools.thread;

public class Semaphore {
	private String mutex = "muuuutex";

	public boolean waitForGreen(long milliSeconds) {
		long start = 0;
		
		synchronized (mutex) {
			try {
				start = System.currentTimeMillis();
				mutex.wait(milliSeconds);
				return (System.currentTimeMillis() - start) < milliSeconds;
			} catch (InterruptedException e) {
				return false;
			}
		}
	}
	
	public void waitForGreen() {
		synchronized (mutex) {
			try {
				mutex.wait();
			} catch (InterruptedException e) {}
		}
	}	

	public void goGreen() {
		synchronized (mutex) {
			mutex.notifyAll();
		}
	}
}
