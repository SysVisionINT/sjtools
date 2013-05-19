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

public class Lock {

	private int givenLocks = 0;
	private int waitingWriters = 0;
	private Object mutex = null;

	public Lock(Object obj) {
		mutex = obj;
	}

	public void getReadLock() {
		synchronized (mutex) {
			while ((givenLocks == -1) || (waitingWriters != 0)) {
				try {
					mutex.wait();
				} catch (Exception e) {}
			}

			givenLocks++;
		}
	}

	public void getWriteLock() {
		synchronized (mutex) {
			waitingWriters++;

			while (givenLocks != 0) {
				try {
					mutex.wait();
				} catch (Exception e) {}
			}

			waitingWriters--;
			givenLocks = -1;
		}
	}

	public void releaseLock() {
		synchronized (mutex) {
			if (givenLocks == 0) {
				return;
			}

			if (givenLocks == -1) {
				givenLocks = 0;
			} else {
				givenLocks--;
			}

			mutex.notifyAll();
		}
	}
}
