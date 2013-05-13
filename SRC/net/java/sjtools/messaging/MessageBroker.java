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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.java.sjtools.messaging.impl.MessageQueue;
import net.java.sjtools.messaging.model.ListenerRecord;
import net.java.sjtools.thread.Lock;

public class MessageBroker {

	private static MessageBroker messageBroker = new MessageBroker();

	private Map topicMap = null;
	private Lock topicLock = null;
	private Map listenerMap = null;
	private Lock listenerLock = null;

	public static MessageBroker getInstance() {
		return messageBroker;
	}

	private MessageBroker() {
		topicMap = new HashMap();
		topicLock = new Lock(topicMap);

		listenerMap = new HashMap();
		listenerLock = new Lock(listenerMap);
	}

	public Topic createTopic(String topicName) {
		Topic topic = getTopic(topicName);

		if (topic == null) {
			topic = new Topic(topicName);
			
			try {
				topicLock.getWriteLock();
				topicMap.put(topicName, topic);
			} finally {
				topicLock.releaseLock();
			}
		}

		return topic;
	}

	public String[] getTopicNames() {
		try {
			topicLock.getReadLock();
			Set topicNames = topicMap.keySet();

			return (String[]) topicNames.toArray(new String[topicNames.size()]);
		} finally {
			topicLock.releaseLock();
		}
	}

	public Topic getTopic(String topicName) {
		try {
			topicLock.getReadLock();
			return (Topic) topicMap.get(topicName);
		} finally {
			topicLock.releaseLock();
		}
	}
	
	public void registerListener(Listener listener) {
		if (listener != null) {
			registerListener(listener.getClass().getName(), listener);
		}
	}

	public void registerListener(String name, Listener listener) {
		try {
			listenerLock.getWriteLock();

			ListenerRecord registed = (ListenerRecord) listenerMap.get(name);

			if (registed == null) {
				registed = new ListenerRecord(listener);
				listenerMap.put(name, registed);
			} else {
				registed.incrementSubscriptionCount();
			}
		} finally {
			listenerLock.releaseLock();
		}
	}
	
	public void unregisterListener(Listener listener) {
		if (listener != null) {
			registerListener(listener.getClass().getName(), listener);
		}
	}

	public void unregisterListener(String name) {
		try {
			listenerLock.getWriteLock();

			ListenerRecord registed = (ListenerRecord) listenerMap.get(name);

			if (registed != null) {
				registed.decrementSubscriptionCount();

				if (registed.getTopicCount() == 0) {
					listenerMap.remove(name);
					registed.getMessageQueue().close();
				}
			}
		} finally {
			listenerLock.releaseLock();
		}
	}

	private MessageQueue getListenerMessageQueue(String listenerName) {
		try {
			listenerLock.getReadLock();
			ListenerRecord registed = (ListenerRecord) listenerMap.get(listenerName);

			if (registed != null) {
				return registed.getMessageQueue();
			} else {
				return null;
			}
		} finally {
			listenerLock.releaseLock();
		}
	}
	
	public boolean sendMessage(String listenerName, Message message) {
		MessageQueue queue = getListenerMessageQueue(listenerName);
		
		if (queue != null) {
			queue.push(message);
			return true;
		}
		
		return false;
	}

	public void stop() {
		try {
			listenerLock.getWriteLock();
			topicLock.getWriteLock();

			for (Iterator i = listenerMap.values().iterator(); i.hasNext();) {
				ListenerRecord registed = (ListenerRecord) i.next();
				registed.getMessageQueue().close();
			}

			listenerMap.clear();
			topicMap.clear();
		} finally {
			listenerLock.releaseLock();
			topicLock.releaseLock();
		}
	}
}