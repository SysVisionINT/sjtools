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
package net.java.sjtools.messaging.queue;

import net.java.sjtools.io.IO;
import net.java.sjtools.messaging.Endpoint;
import net.java.sjtools.messaging.message.Message;
import net.java.sjtools.messaging.router.MessageRouter;
import net.java.sjtools.messaging.router.RoutingMessage;
import net.java.sjtools.util.Queue;

public class RouterQueue implements MessageQueue, Runnable {

	private Queue queue = null;
	private boolean running = false;
	private boolean sleeping = false;
	private MessageRouter messageRouter = null;
	private Thread thread = null;

	public RouterQueue(MessageRouter messageRouter) {
		this.queue = new Queue();
		this.messageRouter = messageRouter;
		this.running = true;

		thread = new Thread(this);
		thread.setDaemon(true);

		thread.start();
	}

	public void run() {
		while (isRunning()) {
			RoutingMessage routingMessage = (RoutingMessage) queue.poll();

			if (routingMessage != null) {
				try {
					messageRouter.route(routingMessage.getEndpoint(), routingMessage.getMessage());
				} catch (Exception e) {
					e.printStackTrace(IO.err);
				}
			} else {
				try {
					setSleeping(true);
					Thread.sleep(Long.MAX_VALUE);
				} catch (InterruptedException e) {
					setSleeping(false);
				}
				
			}
		}
	}

	public void push(Endpoint endpoint, Message message) {
		if (isRunning()) {
			queue.push(new RoutingMessage(endpoint, message));

			if (isSleeping()) {
				thread.interrupt();
			}
		}
	}

	public synchronized void close() {
		if (isRunning()) {
			running = false;
			queue.clean();

			if (isSleeping()) {
				thread.interrupt();
			}
		}
	}

	private synchronized boolean isRunning() {
		return running;
	}

	private synchronized boolean isSleeping() {
		return sleeping;
	}

	private synchronized void setSleeping(boolean mode) {
		this.sleeping = mode;
	}
}
