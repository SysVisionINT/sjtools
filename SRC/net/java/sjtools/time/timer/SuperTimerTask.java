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

public abstract class SuperTimerTask implements Runnable {
	private SuperTimerTaskRunner runner = null;

	public boolean cancel() {
		if (runner == null) {
			return false;
		}

		return runner.cancel();
	}

	public long scheduledExecutionTime() {
		if (runner == null) {
			return 0;
		}

		return runner.scheduledExecutionTime();
	}

	protected void setRunner(SuperTimerTaskRunner runner) {
		this.runner = runner;
	}
}
