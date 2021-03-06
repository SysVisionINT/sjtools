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

import net.java.sjtools.messaging.Endpoint;
import net.java.sjtools.messaging.error.TimeoutException;
import net.java.sjtools.messaging.message.Message;
import net.java.sjtools.thread.Semaphore;

public class CallQueue implements MessageQueue {
	private Message message = null;
	private Semaphore semafore = null;
	
	public CallQueue() {
		semafore = new Semaphore();
	}

	public void push(Endpoint endpoint, Message message) {
		this.message = message;
		semafore.goGreen();
	}

	public void close() {
		semafore.goGreen();
	}
	
	public Message getMessage() {
		semafore.waitForGreen();
		return message;
	}
	
	public Message getMessage(long timeout) throws TimeoutException {
		if (semafore.waitForGreen(timeout)) {
			return message;
		} else {
			throw new TimeoutException(timeout);
		}
	}
}
