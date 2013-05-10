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
package net.java.sjtools.messaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.messaging.impl.MessageQueue;
import net.java.sjtools.thread.Lock;

public class Topic {

	private List listenerList = null;
	private Lock listenerLock = null;
	private String name = null;

	protected Topic(String name) {
		listenerList = new ArrayList();
		listenerLock = new Lock(listenerList);

		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void sendMessage(Message msg) {
		MessageBroker broker = MessageBroker.getInstance();

		MessageQueue queue = null;

		try {
			listenerLock.getReadLock();

			for (Iterator i = listenerList.iterator(); i.hasNext();) {
				queue = broker.getListenerMessageQueue((String) i.next());

				if (queue != null) {
					queue.push(msg);
				}
			}
		} finally {
			listenerLock.releaseLock();
		}
	}

	public void subscribe(Listener listener) {
		String listenerName = listener.getClass().getName();

		subscribe(listenerName, listener);
	}

	public void subscribe(String listenerName, Listener listener) {
		if (!isSubscriber(listenerName)) {
			try {
				listenerLock.getWriteLock();
				listenerList.add(listenerName);

				MessageBroker.getInstance().register(listenerName, listener);
			} finally {
				listenerLock.releaseLock();
			}
		}
	}

	public void unsubscribe(Listener listener) {
		String listenerName = listener.getClass().getName();

		unsubscribe(listenerName);
	}

	public void unsubscribe(String listenerName) {
		if (isSubscriber(listenerName)) {
			try {
				listenerLock.getWriteLock();
				listenerList.remove(listenerName);

				MessageBroker.getInstance().unregister(listenerName);
			} finally {
				listenerLock.releaseLock();
			}
		}
	}

	private boolean isSubscriber(String listenerName) {
		try {
			listenerLock.getReadLock();

			return listenerList.contains(listenerName);
		} finally {
			listenerLock.releaseLock();
		}
	}

	public int getNumberOfListeners() {
		try {
			listenerLock.getReadLock();
			return listenerList.size();
		} finally {
			listenerLock.releaseLock();
		}
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof Topic)) {
			return false;
		}

		Topic other = (Topic) obj;

		return other.getName().equals(name);
	}
}