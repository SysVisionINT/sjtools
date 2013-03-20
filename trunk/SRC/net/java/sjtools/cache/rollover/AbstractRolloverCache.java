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

public abstract class AbstractRolloverCache {
	private CacheManager cacheManager = null;
	private long cacheTimeout = 0;
	
	public Object getObject(Object key) {
		if (cacheManager == null) {
			createCache();
		}
		
		Object data = null;

		CacheEntry cacheEntry = cacheManager.getCacheEntry(key);

		if (cacheEntry != null) {
			if (cacheEntry.getTimeout() > System.currentTimeMillis()) {
				return cacheEntry.getContent();
			} else {
				data = getValue(key);
			}
		} else {
			data = getValue(key);
		}

		if (data != null) {
			if (cacheEntry != null) {
				cacheManager.delCacheEntry(key);
			}

			cacheEntry = new CacheEntry();
			cacheEntry.setTimeout(System.currentTimeMillis() + cacheTimeout);
			cacheEntry.setContent(data);
			cacheEntry.setKey(key);

			cacheManager.addCacheEntry(cacheEntry);
		}

		return data;
	}

	private synchronized void createCache() {
		if (cacheManager != null) {
			return;
		}
		
		cacheTimeout = getCacheTimeout();
		cacheManager = new CacheManager(getCacheMaxSize());
	}

	public abstract int getCacheMaxSize();
	public abstract long getCacheTimeout();
	public abstract Object getValue(Object key);
}
