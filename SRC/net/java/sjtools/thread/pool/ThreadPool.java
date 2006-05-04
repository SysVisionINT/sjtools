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
import net.java.sjtools.thread.SuperThread;

public class ThreadPool {
	private static Pool myPool = new Pool(getDefaultConfig(), new ThreadFactory());

	public static int getPoolSize() {
		return myPool.getPoolSize();
	}

	public static PoolConfig getThreadConfig() {
		return myPool.getPoolConfig();
	}

	public static void setThreadConfig(PoolConfig config) {
		myPool.setPoolConfig(config);
	}

	public static SuperThread getThread() {
		SuperThread st = null;

		try {
			st = (SuperThread) myPool.borrowObject();
		} catch (Exception e) {
		}

		return st;
	}

	private static PoolConfig getDefaultConfig() {
		PoolConfig pc = new PoolConfig();
		pc.setMinimalSize(0);
		pc.setMaxSize(PoolConfig.NO_MAX_SIZE);
		pc.setExpireTime(60000);
		pc.setTimeOut(PoolConfig.NEVER_TIMEOUT);
		pc.setValidateOnInterval(true);
		pc.setValidationTime(10000);
		pc.setWaitTime(10000);

		return pc;
	}

	public static void closePool() {
		myPool.close();
	}
}
