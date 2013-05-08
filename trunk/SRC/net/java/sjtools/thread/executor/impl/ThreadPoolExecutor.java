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

public class ThreadPoolExecutor implements Executor {
	private Executor defaultExecutor = new BasicThreadExecutor(false);
	private ThreadPool pool = null;

	public ThreadPoolExecutor() {
		pool = new ThreadPool();
	}
	
	public ThreadPoolExecutor(ThreadPool threadPool) {
		pool = threadPool;
	}

	public void execute(Runnable runnable) {
		try {
			SuperThread st = pool.getThread();
			
			if (!st.start(runnable)) {
				defaultExecutor.execute(runnable);				
			}
		} catch (Exception e) {
			defaultExecutor.execute(runnable);
		}
	}

	public void close() {
		pool.closePool();
	}
}
