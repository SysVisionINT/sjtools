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
package net.java.sjtools.thread;

import net.java.sjtools.thread.pool.ThreadListener;

public class SuperThread extends Thread {

	public static final int WAITING = 1;
	public static final int RUNNING = 2;
	public static final int STOPPING = 3;
	public static final int STOPED = 4;

	private int status = WAITING;
	private Runnable task = null;
	private ThreadListener listener = null;

	protected SuperThread(ThreadGroup group, String threadName, ThreadListener boss) {
		super(group, threadName);
		listener = boss;
	}

	protected SuperThread(ThreadGroup group, String threadName) {
		this(group, threadName, null);
	}

	public void run() {
		while (status < STOPPING) {
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {}

			if (status < STOPPING && task != null) {
				status = RUNNING;

				task.run();

				task = null;

				if (status == RUNNING) {
					status = WAITING;

					if (listener != null) {
						listener.done(this);
					}
				}
			}
		}

		status = STOPED;
	}

	public synchronized int getStatus() {
		return status;
	}

	public synchronized void die() {
		if (status < STOPED) {
			int oldStatus = status;

			status = STOPPING;

			if (oldStatus == WAITING) {
				this.interrupt();
			}
		}
	}

	public synchronized boolean start(Runnable task) {
		if (status == WAITING) {
			this.task = task;

			this.interrupt();

			return true;
		} else {
			return false;
		}
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof SuperThread)) {
			return false;
		}

		return ((Thread) obj).getName().equals(this.getName());
	}
}