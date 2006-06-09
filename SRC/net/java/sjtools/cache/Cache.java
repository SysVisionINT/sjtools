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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.sjtools.thread.Lock;
import net.java.sjtools.time.SuperTimer;

public class Cache implements Runnable {
	private CacheConfig myConfig = null;
	private CacheFactory myHelper = null;
	private Map myCache = null;
	private Lock myLock = null;
	private boolean running = true;

	public Cache(CacheFactory helper, CacheConfig config) {
		myConfig = config;
		myHelper = helper;

		myCache = new HashMap(myConfig.getInitialCapacity());
		myLock = new Lock(myCache);

		schedule();
	}

	public int getCacheSize() {
		myLock.getReadLock();
		int ret = myCache.size();
		myLock.releaseLock();

		return ret;
	}

	public void run() {
		pruneExpiredEntries(myConfig.getExpireTime());
		schedule();
	}

	private void pruneExpiredEntries(long expireTime) {
		CacheEntry ce = null;
		Object obj = null;

		long now = System.currentTimeMillis();
		List expiredObjects = new ArrayList();

		if (!running) {
			return;
		}

		myLock.getReadLock();

		for (Iterator i = myCache.keySet().iterator(); i.hasNext();) {
			obj = i.next();
			ce = (CacheEntry) myCache.get(obj);

			if (((now - ce.getTimestamp()) > expireTime) || (ce.getObject() == null)) {
				expiredObjects.add(obj);
			}
		}

		myLock.releaseLock();

		if (!expiredObjects.isEmpty()) {
			for (Iterator i = expiredObjects.iterator(); i.hasNext();) {
				invalidade(i.next());
			}
		}
	}

	private void schedule() {
		if (!running) {
			return;
		}
		
		if (myConfig.getExpireTime() != CacheConfig.NEVER_EXPIRE) {
			SuperTimer.getInstance().schedule(this, System.currentTimeMillis() + myConfig.getExpireTime());
		}
	}

	public void clear() {
		if (!running) {
			return;
		}

		pruneExpiredEntries(0);

		myLock.getWriteLock();
		myCache.clear();
		myLock.releaseLock();
	}

	public void close() {
		clear();

		running = false;
		myConfig = null;
		myHelper = null;
		myCache = null;
		myLock = null;
	}

	public void invalidade(Object name) {
		if (!running) {
			return;
		}

		myLock.getWriteLock();

		CacheEntry ce = (CacheEntry) myCache.get(name);

		if (ce != null) {
			myCache.remove(name);

			Object obj = ce.getObject();

			if (obj != null) {
				myHelper.destroyObject(obj);
			}
		}

		myLock.releaseLock();
	}

	public Object getObject(Object name) throws ObjectNotFound, CacheUnavailable {
		CacheEntry ce = null;
		Object obj = null;

		if (!running) {
			throw new CacheUnavailable();
		}

		myLock.getReadLock();
		ce = (CacheEntry) myCache.get(name);
		myLock.releaseLock();

		if (ce != null) {
			obj = ce.getObject();
		} else {
			obj = myHelper.getObject(name);

			if (myCache.size() < myConfig.getMaxCapacity()) {
				myLock.getWriteLock();
				myCache.put(name, new CacheEntry(obj));
				myLock.releaseLock();
			}
		}

		return obj;
	}

	public CacheConfig getCacheConfig() {
		return myConfig;
	}

	public void setCacheConfig(CacheConfig config) {
		myConfig = config;
		schedule();
	}

	public CacheFactory getCacheFactory() {
		return myHelper;
	}

	public boolean isRunning() {
		return running;
	}

	public void setCacheFactory(CacheFactory factory) {
		myHelper = factory;
	}

}
