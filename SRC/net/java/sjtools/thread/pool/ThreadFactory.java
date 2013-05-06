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
package net.java.sjtools.thread.pool;

import net.java.sjtools.pool.Pool;
import net.java.sjtools.pool.PoolFactory;
import net.java.sjtools.thread.SuperThread;

public class ThreadFactory implements PoolFactory, ThreadListener {
	private String threadIdentifier = null;
	private int priority = Thread.NORM_PRIORITY;
	private boolean daemon = true;
	private int threadNumber = 1;
	private ThreadGroup group = null;
	private Pool pool = null;
	
	public ThreadFactory(String threadIdentifier, boolean daemon, int priority) {
		this.threadIdentifier = threadIdentifier;
		this.daemon = daemon;
		this.priority = priority;
		this.group = Thread.currentThread().getThreadGroup();
	}

	public Object createObject() {
		SuperThread st = new SuperThread(group, getThreadName(), this);

		st.setDaemon(daemon);
		st.setPriority(priority);
		
		st.start();

		return st;
	}

	private String getThreadName() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(threadIdentifier);
		buffer.append("-");
		buffer.append(threadNumber++);
		
		return buffer.toString();
	}

	public boolean validateObject(Object obj) {
		if (obj instanceof SuperThread) {
			return ((SuperThread)obj).getStatus() < SuperThread.STOPPING;	
		}
		
		return false;
	}

	public void destroyObject(Object obj) {
		if (obj instanceof SuperThread) {
			((SuperThread)obj).die();
		}
	}

	public void done(SuperThread thread) {
		if (pool != null && pool.isRunning()) {
			pool.returnObject(thread);
		} else {
			destroyObject(thread);
		}
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

}
