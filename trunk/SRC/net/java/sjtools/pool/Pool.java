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

import net.java.sjtools.thread.Lock;
import net.java.sjtools.time.SuperTimer;

public class Pool implements Runnable {
	private PoolConfig myConfig = null;

	private PoolFactory myFactory = null;

	private Map myIdlList = new HashMap();

	private Map myInUseList = new HashMap();

	private boolean running = true;

	private Lock myLock = null;

	private long myNextIntervalValidation = 0;

	private long myNextExpireTime = 0;

	private long myNextTimeOut = 0;

	public Pool(PoolConfig config, PoolFactory factory) {
		setPoolConfig(config);
		setPoolFactory(factory);

		myLock = new Lock(this);

		myLock.getWriteLock();
		fillPool();
		myLock.releaseLock();
	}

	public void run() {
		if (!running) {
			return;
		}

		long now = System.currentTimeMillis();

		if (now >= myNextIntervalValidation) {
			myLock.getWriteLock();
			testIdlList();
			fillPool();
			myLock.releaseLock();
			myNextIntervalValidation = now + myConfig.getValidationTime();
		}

		if (now >= myNextExpireTime) {
			myLock.getWriteLock();
			pruneList(myIdlList, myConfig.getExpireTime(), false);
			myLock.releaseLock();
			myNextExpireTime = now + myConfig.getExpireTime();
		}

		if (now >= myNextTimeOut) {
			myLock.getWriteLock();
			pruneList(myInUseList, myConfig.getTimeOut(), true);
			fillPool();
			myLock.releaseLock();
			myNextTimeOut = now + myConfig.getTimeOut();
		}

		schedule();
	}

	private void testIdlList() {
		List removeList = new ArrayList();
		Object obj = null;

		for (Iterator i = myIdlList.keySet().iterator(); i.hasNext();) {
			obj = i.next();

			if (!myFactory.validateObject(obj)) {
				removeList.add(obj);
			}
		}

		for (Iterator i = removeList.iterator(); i.hasNext();) {
			obj = i.next();

			myFactory.destroyObject(obj);
			myIdlList.remove(obj);
		}
	}

	public int getPoolSize() {
		myLock.getReadLock();
		int currentSize = myIdlList.size() + myInUseList.size();
		myLock.releaseLock();

		return currentSize;
	}

	public int getObjectsInUse() {
		myLock.getReadLock();
		int currentSize = myInUseList.size();
		myLock.releaseLock();

		return currentSize;
	}

	public int getObjectsIdle() {
		myLock.getReadLock();
		int currentSize = myIdlList.size();
		myLock.releaseLock();

		return currentSize;
	}

	private void fillPool() {
		int currentSize = myIdlList.size() + myInUseList.size();
		int minSize = myConfig.getMinimalSize();

		for (; currentSize < minSize; currentSize++) {
			try {
				myIdlList.put(myFactory.createObject(), new Long(System
						.currentTimeMillis()));
			} catch (ObjectCreationException e) {
			}
		}
	}

	private void pruneList(Map list, long time, boolean ignoreMinimal) {
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
			int currentSize = myIdlList.size() + myInUseList.size();
			Iterator i = removeList.iterator();

			while (i.hasNext()
					&& (ignoreMinimal || (currentSize > myConfig
							.getMinimalSize()))) {
				obj = i.next();

				myFactory.destroyObject(obj);
				list.remove(obj);
				currentSize--;
			}
		}
	}

	private void schedule() {
		long next = 0;
		long now = System.currentTimeMillis();

		if (myNextIntervalValidation == 0) {
			if (myConfig.isValidateOnInterval()) {
				myNextIntervalValidation = now + myConfig.getValidationTime();
			} else {
				myNextIntervalValidation = Long.MAX_VALUE;
			}

			if ((myConfig.getExpireTime() != PoolConfig.NEVER_EXPIRE)) {
				myNextExpireTime = now + myConfig.getExpireTime();
			} else {
				myNextExpireTime = Long.MAX_VALUE;
			}

			if ((myConfig.getTimeOut() != PoolConfig.NEVER_TIMEOUT)) {
				myNextTimeOut = now + myConfig.getTimeOut();
			} else {
				myNextTimeOut = Long.MAX_VALUE;
			}
		}

		next = (myNextIntervalValidation < myNextExpireTime ? myNextIntervalValidation
				: myNextExpireTime);
		next = (next < myNextTimeOut ? next : myNextTimeOut);

		if (next != Long.MAX_VALUE) {
			SuperTimer.getInstance().schedule(this, next);
		}
	}

	public void close() {
		if (!running) {
			return;
		}

		running = false;

		clear();

		myConfig = null;
		myFactory = null;
		myIdlList = null;
		myInUseList = null;
	}

	public void clear() {
		myLock.getWriteLock();
		pruneList(myIdlList, -1, true);
		pruneList(myInUseList, -1, true);
		myLock.releaseLock();
	}

	public Object borrowObject() throws PoolUnavailableException,
			WaitTimeExpiredException, ObjectCreationException {
		if (!running) {
			throw new PoolUnavailableException();
		}

		Object obj = null;
		long wait = Long.MAX_VALUE;

		if (myConfig.getWaitTime() != PoolConfig.WAIT_FOREVER) {
			wait = System.currentTimeMillis() + myConfig.getWaitTime();
		}

		while (obj == null && System.currentTimeMillis() < wait) {
			obj = getFirstObject();

			if (obj == null) {
				if (myInUseList.size() <= myConfig.getMaxSize()
						|| myConfig.getMaxSize() == PoolConfig.NO_MAX_SIZE) {
					obj = myFactory.createObject();
				}
			}

			if (myConfig.isValidateOnBorrow() && !myFactory.validateObject(obj)) {
				myFactory.destroyObject(obj);
				obj = null;
			}

		}

		if (obj == null) {
			throw new WaitTimeExpiredException();
		}

		myLock.getWriteLock();
		myInUseList.put(obj, new Long(System.currentTimeMillis()));
		myLock.releaseLock();

		return obj;
	}

	private Object getFirstObject() {
		Object obj = null;

		myLock.getWriteLock();

		if (!myIdlList.isEmpty()) {
			obj = myIdlList.keySet().iterator().next();

			if (myIdlList.remove(obj) == null) {
				obj = null;
			}
		}

		myLock.releaseLock();

		return obj;
	}

	public void returnObject(Object obj) {
		if (!running) {
			return;
		}

		myLock.getWriteLock();
		myInUseList.remove(obj);

		if (myConfig.isValidateOnReturn()) {
			if (!myFactory.validateObject(obj)) {
				myFactory.destroyObject(obj);
				myLock.releaseLock();

				return;
			}
		}

		myIdlList.put(obj, new Long(System.currentTimeMillis()));
		myLock.releaseLock();
	}

	public void invalidade(Object obj) {
		myLock.getWriteLock();
		myFactory.destroyObject(obj);
		myInUseList.remove(obj);
		myIdlList.remove(obj);
		myLock.releaseLock();
	}

	public PoolConfig getPoolConfig() {
		return myConfig;
	}

	public PoolFactory getPoolFactory() {
		return myFactory;
	}

	public boolean isRunning() {
		return running;
	}

	public void setPoolConfig(PoolConfig config) {
		myConfig = config;

		myNextIntervalValidation = 0;
		myNextExpireTime = 0;
		myNextTimeOut = 0;

		schedule();
	}

	public void setPoolFactory(PoolFactory factory) {
		myFactory = factory;
		myFactory.setPool(this);
	}
}
