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
import net.java.sjtools.thread.ThreadNotifier;

public class ThreadFactory implements PoolFactory, ThreadNotifier {
	private Pool pool = null;

	public Object createObject() {
		SuperThread st = new SuperThread(this);

		st.setDaemon(false);
		st.setName("ThreadPool(" + st.getName() + ")");
		st.start();

		return st;
	}

	public boolean validateObject(Object obj) {
		return ((SuperThread)obj).getStatus() != SuperThread.STOP;
	}

	public void destroyObject(Object obj) {
		((SuperThread)obj).die();
	}

	public void done(SuperThread thread) {
		if (pool.isRunning()) {
			pool.returnObject(thread);
		} else {
			destroyObject(thread);
		}
	}

	public void setPool(Pool pool) {
		this.pool = pool;
	}

}
