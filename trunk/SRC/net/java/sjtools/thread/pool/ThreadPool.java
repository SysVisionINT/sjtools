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
import net.java.sjtools.pool.PoolConfig;
import net.java.sjtools.pool.error.ObjectCreationException;
import net.java.sjtools.pool.error.PoolUnavailableException;
import net.java.sjtools.pool.error.WaitTimeExpiredException;
import net.java.sjtools.thread.SuperThread;

public class ThreadPool {
	private Pool pool = null;

	public ThreadPool(PoolConfig config, ThreadFactory factory) {
		pool = new Pool(config, factory);
	}

	public int getPoolSize() {
		return pool.getPoolSize();
	}

	public PoolConfig getThreadConfig() {
		return pool.getPoolConfig();
	}

	public SuperThread getThread() throws PoolUnavailableException, WaitTimeExpiredException, ObjectCreationException {
		return (SuperThread) pool.borrowObject();
	}

	public static PoolConfig getDefaultConfig() {
		PoolConfig pc = new PoolConfig();
		pc.setMinimalSize(0);
		pc.setMaxSize(PoolConfig.NO_MAX_SIZE);
		pc.setExpireTime(60000);
		pc.setTimeOut(PoolConfig.NEVER_TIMEOUT);
		pc.setValidateOnInterval(false);
		pc.setWaitTime(PoolConfig.WAIT_FOREVER);

		return pc;
	}

	public void closePool() {
		pool.close();
	}
}
