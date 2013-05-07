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
package net.java.sjtools.thread.executor.impl;

import net.java.sjtools.thread.SuperThread;
import net.java.sjtools.thread.executor.Executor;
import net.java.sjtools.thread.pool.ThreadPool;

public class ThreadPoolProvider implements Executor {
	private Executor executor = new SimpleThreadProvider();
	private ThreadPool pool = null;

	public ThreadPoolProvider(ThreadPool threadPool) {
		pool = threadPool;
	}

	public Thread execute(Runnable runnable) {
		Thread thread = null;
		
		try {
			SuperThread st = pool.getThread();
			st.start(runnable);
			
			thread = st;
		} catch (Exception e) {
			thread = executor.execute(runnable);
		}
		
		return thread;
	}

	public void close() {
		pool.closePool();
	}
}
