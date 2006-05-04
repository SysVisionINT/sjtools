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
package net.java.sjtools.time;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import net.java.sjtools.thread.Lock;
import net.java.sjtools.thread.SuperThread;
import net.java.sjtools.thread.pool.ThreadPool;

public class SuperTimer implements Runnable {
	private static SuperTimer realTimer = null;

	private Thread myThread = null;
	private boolean run = true;
	private long sleep = Long.MAX_VALUE;
	private SortedMap myTasks = new TreeMap();
	private Lock lock = new Lock(myTasks);

	public synchronized static SuperTimer getInstance() {
		if (realTimer == null) {
			realTimer = new SuperTimer();
		}

		return realTimer;
	}

	private SuperTimer() {
		start();
	}

	private void start() {
		myThread = new Thread(Thread.currentThread().getThreadGroup(), this);
		myThread.setDaemon(false);
		myThread.setName("SuperTimer(" + myThread.getName() + ")");
		myThread.start();
		run = true;
	}

	public void cancel() {
		lock.getWriteLock();
		myTasks.clear();
		lock.releaseLock();

		updateClock();
	}

	private void stop() {
		run = false;
		myThread.interrupt();
	}

	public void schedule(Runnable task, Timestamp time) {
		Object first = null;

		lock.getReadLock();

		if (!myTasks.isEmpty()) {
			first = myTasks.firstKey();
		}

		List list = (List) myTasks.get(time);

		lock.releaseLock();

		if (list == null) {
			list = new ArrayList();
		}

		list.add(task);

		lock.getWriteLock();
		myTasks.put(time, list);
		lock.releaseLock();

		if (first == null || time.before((Timestamp) first)) {
			updateClock();
		}
	}

	public void schedule(Runnable task, long time) {
		schedule(task, new Timestamp(time));
	}

	public void run() {
		while (run) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
			}

			if (run) {
				runTasks();
			}
		}
	}

	public int getNumberOfTasks() {
		int count = 0;

		lock.getReadLock();

		for (Iterator i = myTasks.values().iterator(); i.hasNext();) {
			count = count + ((List) i.next()).size();
		}

		lock.releaseLock();

		return count;
	}

	public Timestamp getNextExecution() {
		lock.getReadLock();
		Timestamp time = (Timestamp) myTasks.firstKey();
		lock.releaseLock();

		return time;
	}

	private void runTasks() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List scheduleTasks = new ArrayList();
		List removeList = new ArrayList();
		Timestamp date = null;
		SuperThread thread = null;
		int count = 0;

		lock.getReadLock();

		for (Iterator i = myTasks.keySet().iterator(); i.hasNext();) {
			date = (Timestamp) i.next();

			if (now.after(date) || now.equals(date)) {
				scheduleTasks.addAll((List) myTasks.get(date));
				removeList.add(date);
				count++;
			} else {
				break;
			}
		}

		lock.releaseLock();

		if (count > 0) {
			lock.getWriteLock();

			for (Iterator i = removeList.iterator(); i.hasNext();) {
				myTasks.remove(i.next());
			}

			lock.releaseLock();

			for (Iterator i = scheduleTasks.iterator(); i.hasNext();) {
				thread = ThreadPool.getThread();
				thread.start((Runnable) i.next());
			}

			updateClock();
		}
	}

	private void updateClock() {
		lock.getReadLock();

		if (!myTasks.isEmpty()) {
			Timestamp first = (Timestamp) myTasks.firstKey();
			sleep = first.getTime() - System.currentTimeMillis();

			if (sleep < 0) {
				sleep = 0;
			}
		} else {
			sleep = Long.MAX_VALUE;
		}

		lock.releaseLock();

		if (sleep == Long.MAX_VALUE) {
			stop();
		} else {
			if (run) {
				myThread.interrupt();
			} else {
				start();
			}
		}

	}
}
