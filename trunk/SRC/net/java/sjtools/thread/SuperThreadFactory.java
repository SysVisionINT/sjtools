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

import net.java.sjtools.thread.pool.ThreadListener;

public class SuperThreadFactory {
	private String threadIdentifier = null;
	private int priority = Thread.NORM_PRIORITY;
	private boolean daemon = true;
	private int threadNumber = 1;
	private ThreadGroup group = null;
	
	public SuperThreadFactory(String threadIdentifier, boolean daemon, int priority) {
		this.threadIdentifier = threadIdentifier;
		this.daemon = daemon;
		this.priority = priority;
		
		SecurityManager currentManager = System.getSecurityManager();
		
		if (currentManager != null) {
			this.group = currentManager.getThreadGroup();
		} else {
			this.group = Thread.currentThread().getThreadGroup();
		}
	}

	public SuperThread createSuperThread(ThreadListener listener) {
		SuperThread st = createThread(listener);

		st.setDaemon(daemon);
		st.setPriority(priority);
		
		st.start();

		return st;
	}
	
	public SuperThread createSuperThread() {
		return createSuperThread(null);
	}
	
	private SuperThread createThread(ThreadListener listener) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(threadIdentifier);
		buffer.append("-");
		buffer.append(threadNumber++);

		return new SuperThread(group, buffer.toString(), listener);	
	}
}
