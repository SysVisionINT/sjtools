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
package net.java.sjtools.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.sjtools.pool.error.ObjectCreationException;
import net.java.sjtools.pool.error.PoolUnavailableException;
import net.java.sjtools.pool.error.WaitTimeExpiredException;
import net.java.sjtools.pool.task.Expire;
import net.java.sjtools.pool.task.Timeout;
import net.java.sjtools.pool.task.Validate;
import net.java.sjtools.thread.Lock;
import net.java.sjtools.time.timer.SuperTimer;

public class Pool {
	private PoolConfig config = null;
	private PoolFactory factory = null;
	private Map idlList = new HashMap();
	private Map inUseList = new HashMap();
	private boolean running = true;
	private Lock lock = null;

	public Pool(PoolConfig config, PoolFactory factory) {
		setPoolConfig(config);
		setPoolFactory(factory);

		lock = new Lock(this);

		try {
			lock.getWriteLock();
			fillPool();
		} finally {
			lock.releaseLock();
		}
	}

	public void processValidation() {
		if (!running) {
			return;
		}

		try {
			lock.getWriteLock();
			testIdlList();
			fillPool();
		} finally {
			lock.releaseLock();
		}
	}

	public void processExpires() {
		if (!running) {
			return;
		}

		try {
			lock.getWriteLock();
			pruneList(idlList, config.getExpireTime());
		} finally {
			lock.releaseLock();
		}
	}

	public void processTimeouts() {
		if (!running) {
			return;
		}

		try {
			lock.getWriteLock();
			pruneList(inUseList, config.getTimeOut());
			fillPool();
		} finally {
			lock.releaseLock();
		}
	}

	private void testIdlList() {
		List removeList = new ArrayList();
		Object obj = null;

		for (Iterator i = idlList.keySet().iterator(); i.hasNext();) {
			obj = i.next();

			if (!factory.validateObject(obj)) {
				removeList.add(obj);
			}
		}

		for (Iterator i = removeList.iterator(); i.hasNext();) {
			obj = i.next();

			factory.destroyObject(obj);
			idlList.remove(obj);
		}
	}

	public int getPoolSize() {
		try {
			lock.getReadLock();
			return idlList.size() + inUseList.size();
		} finally {
			lock.releaseLock();
		}
	}

	public int getObjectsInUse() {
		try {
			lock.getReadLock();
			return inUseList.size();
		} finally {
			lock.releaseLock();
		}
	}

	public int getObjectsIdle() {
		try {
			lock.getReadLock();
			return idlList.size();
		} finally {
			lock.releaseLock();
		}
	}

	private void fillPool() {
		int currentSize = idlList.size() + inUseList.size();
		int minSize = config.getMinimalSize();

		for (; currentSize < minSize; currentSize++) {
			try {
				idlList.put(factory.createObject(), new Long(System.currentTimeMillis()));
			} catch (ObjectCreationException e) {}
		}
	}

	private void pruneList(Map list, long time) {
		List removeList = new ArrayList();
		Object obj = null;
		long now = System.currentTimeMillis();

		for (Iterator i = list.keySet().iterator(); i.hasNext();) {
			obj = i.next();

			if ((now - ((Long) list.get(obj)).longValue()) > time) {
				removeList.add(obj);
			}
		}

		if (!removeList.isEmpty()) {
			Iterator i = removeList.iterator();

			while (i.hasNext()) {
				obj = i.next();

				factory.destroyObject(obj);
				list.remove(obj);
			}
		}
	}

	private void schedule() {
		SuperTimer timer = SuperTimer.getInstance();

		if (config.isValidateOnInterval()) {
			timer.schedule(new Validate(this), config.getValidationTime(), config.getValidationTime());
		}

		if (config.getExpireTime() != PoolConfig.NEVER_EXPIRE) {
			timer.schedule(new Expire(this), config.getExpireTime(), config.getExpireTime());
		}

		if (config.getTimeOut() != PoolConfig.NEVER_TIMEOUT) {
			timer.schedule(new Timeout(this), config.getTimeOut(), config.getTimeOut());
		}
	}

	public void close() {
		if (!running) {
			return;
		}

		running = false;

		clear();

		config = null;
		factory = null;
		idlList = null;
		inUseList = null;
	}

	public void clear() {
		try {
			lock.getWriteLock();
			pruneList(idlList, -1);
			pruneList(inUseList, -1);
		} finally {
			lock.releaseLock();
		}
	}

	public Object borrowObject() throws PoolUnavailableException, WaitTimeExpiredException, ObjectCreationException {
		if (!running) {
			throw new PoolUnavailableException();
		}

		Object obj = null;
		long wait = Long.MAX_VALUE;

		if (config.getWaitTime() != PoolConfig.WAIT_FOREVER) {
			wait = System.currentTimeMillis() + config.getWaitTime();
		}

		while (obj == null && System.currentTimeMillis() < wait) {
			obj = getFirstObject();

			if (obj == null) {
				if (inUseList.size() <= config.getMaxSize() || config.getMaxSize() == PoolConfig.NO_MAX_SIZE) {
					obj = factory.createObject();
				}
			}

			if (config.isValidateOnBorrow() && !factory.validateObject(obj)) {
				factory.destroyObject(obj);
				obj = null;
			}
		}

		if (obj == null) {
			throw new WaitTimeExpiredException();
		}

		try {
			lock.getWriteLock();
			inUseList.put(obj, new Long(System.currentTimeMillis()));
		} finally {
			lock.releaseLock();
		}

		return obj;
	}

	private Object getFirstObject() {
		Object obj = null;

		try {
			lock.getWriteLock();

			if (!idlList.isEmpty()) {
				obj = idlList.keySet().iterator().next();

				if (idlList.remove(obj) == null) {
					obj = null;
				}
			}
		} finally {
			lock.releaseLock();
		}

		return obj;
	}

	public void returnObject(Object obj) {
		if (!running) {
			return;
		}

		try {
			lock.getWriteLock();
			inUseList.remove(obj);

			if (config.isValidateOnReturn()) {
				if (!factory.validateObject(obj)) {
					factory.destroyObject(obj);
					lock.releaseLock();

					return;
				}
			}

			idlList.put(obj, new Long(System.currentTimeMillis()));
		} finally {
			lock.releaseLock();
		}
	}

	public void invalidade(Object obj) {
		try {
			lock.getWriteLock();
			factory.destroyObject(obj);
			inUseList.remove(obj);
			idlList.remove(obj);
		} finally {
			lock.releaseLock();
		}
	}

	public PoolConfig getPoolConfig() {
		return config;
	}

	public PoolFactory getPoolFactory() {
		return factory;
	}

	public boolean isRunning() {
		return running;
	}

	private void setPoolConfig(PoolConfig config) {
		this.config = config;

		schedule();
	}

	public void setPoolFactory(PoolFactory factory) {
		this.factory = factory;
		factory.setPool(this);
	}
}
