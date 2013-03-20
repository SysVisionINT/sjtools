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
package net.java.sjtools.cache.rollover;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;

import net.java.sjtools.thread.Lock;

public class CacheManager {
	private int cacheMaxSize = 5000;

	private HashMap cache = null;
	private LinkedList memory = null;
	private Lock lock = null;
	
	public CacheManager(int maxSize) {
		this.cacheMaxSize = maxSize;
		
		cache = new HashMap(cacheMaxSize);
		lock = new Lock(cache);
		
		memory = new LinkedList();
	}

	public CacheEntry getCacheEntry(Object key) {
		lock.getReadLock();

		try {
			SoftReference entry = (SoftReference) cache.get(key);

			if (entry != null) {
				return (CacheEntry) entry.get();
			}
		} finally {
			lock.releaseLock();
		}

		return null;
	}

	public void delCacheEntry(Object key) {
		lock.getWriteLock();

		try {
			SoftReference entry = (SoftReference) cache.remove(key);

			if (entry != null) {
				memory.remove(entry);
			}
		} finally {
			lock.releaseLock();
		}
	}

	public void addCacheEntry(CacheEntry entry) {
		lock.getWriteLock();

		try {
			SoftReference ref = new SoftReference(entry);

			cache.put(entry.getKey(), ref);

			if (memory.size() >= cacheMaxSize) {
				SoftReference older = (SoftReference) memory.remove(0);

				if (older != null) {
					CacheEntry olderEntry = (CacheEntry) older.get();

					if (olderEntry != null) {
						cache.remove(olderEntry.getKey());
					}
				}
			}

			memory.addLast(ref);
		} finally {
			lock.releaseLock();
		}
	}
}
