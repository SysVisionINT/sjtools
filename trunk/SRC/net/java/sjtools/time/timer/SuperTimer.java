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
package net.java.sjtools.time.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.java.sjtools.thread.executor.Executor;
import net.java.sjtools.thread.executor.impl.BasicThreadExecutor;

public class SuperTimer {
	private Timer timer = null;
	private boolean defaultTimer = false;
	private Executor executor = null;

	private static SuperTimer me = new SuperTimer(true, new BasicThreadExecutor(false));

	private SuperTimer(boolean defaultTimer, Executor executor) {
		this.timer = new Timer(defaultTimer ? true : false);
		this.defaultTimer = defaultTimer;
		this.executor = executor;
	}

	public static SuperTimer getInstance() {
		return me;
	}
	
	public static SuperTimer getInstance(Executor executor) {
		return new SuperTimer(false, executor);
	}

	public void schedule(SuperTimerTask task, long delay) {
		timer.schedule(getTaskRunner(task), delay);
	}

	public void schedule(SuperTimerTask task, Date time) {
		timer.schedule(getTaskRunner(task), time);
	}

	public void schedule(SuperTimerTask task, long delay, long period) {
		timer.schedule(getTaskRunner(task), delay, period);
	}

	public void schedule(SuperTimerTask task, Date firstTime, long period) {
		timer.schedule(getTaskRunner(task), firstTime, period);
	}

	public void scheduleAtFixedRate(SuperTimerTask task, long delay, long period) {
		timer.scheduleAtFixedRate(getTaskRunner(task), delay, period);
	}

	public void scheduleAtFixedRate(SuperTimerTask task, Date firstTime, long period) {
		timer.scheduleAtFixedRate(getTaskRunner(task), firstTime, period);
	}
	
	public boolean close() {
		if (!defaultTimer) {
			timer.cancel();
			executor.close();
			
			return true;
		}
		
		return false;
	}

	private TimerTask getTaskRunner(SuperTimerTask task) {
		return new SuperTimerTaskRunner(this, task);
	}

	protected Executor getExecutor() {
		return executor;
	}
}
