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
package net.java.sjtools.cache;

import java.util.HashMap;
import java.util.Map;

import net.java.sjtools.thread.Lock;

public class StaticCache {
	private static final int INITIAL_SIZE = 250;
	private static Map cache = new HashMap(INITIAL_SIZE);
	private static Map time = new HashMap(INITIAL_SIZE);
	private static Lock lock = new Lock(cache);

	public static Object getObject(String name) {
		lock.getReadLock();

		Object obj = cache.get(name);

		lock.releaseLock();

		return obj;
	}

	public static void storeObject(String name, Object obj) {
		lock.getWriteLock();

		cache.put(name, obj);
		
		if (obj != null) {
			time.put(name, new Long(System.currentTimeMillis()));
		} else {
			time.remove(name);
		}

		lock.releaseLock();
	}

	public static void invalidadeObject(String name) {
		storeObject(name, null);
	}
	
	public static long getLastChange(String name) {
		lock.getReadLock();

		Long ret = (Long)time.get(name);

		lock.releaseLock();
		
		if (ret == null) {
			return 0;
		} else {
			return ret.longValue();
		}
	}	
}
