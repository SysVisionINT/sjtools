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
package net.java.sjtools.messaging.router;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.java.sjtools.messaging.Endpoint;
import net.java.sjtools.messaging.Listener;
import net.java.sjtools.messaging.MessageBroker;
import net.java.sjtools.messaging.Topic;
import net.java.sjtools.messaging.message.Message;
import net.java.sjtools.messaging.queue.ListenerQueue;
import net.java.sjtools.messaging.queue.MessageQueue;
import net.java.sjtools.thread.Lock;

public class LocalRouter implements Router, MessageRouter {

	private Map topicMap = null;
	private Lock topicLock = null;
	private Map listenerMap = null;
	private Lock listenerLock = null;

	public LocalRouter() {
		topicMap = new HashMap();
		topicLock = new Lock(topicMap);

		listenerMap = new HashMap();
		listenerLock = new Lock(listenerMap);
	}

	public Topic getTopic(String topicName) {
		Topic topic = null;

		try {
			topicLock.getReadLock();
			topic = (Topic) topicMap.get(topicName);
		} finally {
			topicLock.releaseLock();
		}

		if (topic == null) {
			topic = new Topic(Endpoint.getLocalEndpointForTopic(topicName));

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

	public Endpoint registerListener(String destination, Listener listener) {
		try {
			listenerLock.getWriteLock();

			ListenerQueue queue = (ListenerQueue) listenerMap.get(destination);

			if (queue == null) {
				listenerMap.put(destination, new ListenerQueue(listener));
			}
		} finally {
			listenerLock.releaseLock();
		}

		return Endpoint.getLocalEndpointForDestination(destination);
	}

	public String[] getListenerNames() {
		try {
			listenerLock.getReadLock();
			Set listenerNames = listenerMap.keySet();

			return (String[]) listenerNames.toArray(new String[listenerNames.size()]);
		} finally {
			listenerLock.releaseLock();
		}
	}

	public boolean isListenerRegistered(String name) {
		try {
			listenerLock.getReadLock();
			return listenerMap.containsKey(name);
		} finally {
			listenerLock.releaseLock();
		}
	}

	private ListenerQueue getListenerQueue(String listenerName) {
		try {
			listenerLock.getReadLock();
			return (ListenerQueue) listenerMap.get(listenerName);
		} finally {
			listenerLock.releaseLock();
		}
	}

	public void stop() {
		try {
			listenerLock.getWriteLock();
			topicLock.getWriteLock();

			for (Iterator i = listenerMap.values().iterator(); i.hasNext();) {
				MessageQueue queue = (MessageQueue) i.next();
				queue.close();
			}

			listenerMap.clear();
			topicMap.clear();
		} finally {
			listenerLock.releaseLock();
			topicLock.releaseLock();
		}
	}

	public void route(Endpoint endpoint, Message message) {
		if (endpoint.isListener()) {
			ListenerQueue queue = getListenerQueue(endpoint.getDestination());

			if (queue != null) {
				queue.push(message);
			}
		} else {
			Topic topic = getTopic(endpoint.getDestination());
			
			if (topic != null) {
				Endpoint [] subscribers = topic.getSubscribers();
				
				for (int i = 0; i < subscribers.length; i++) {
					if (subscribers[i].isLocal()) {
						ListenerQueue queue = getListenerQueue(subscribers[i].getDestination());

						if (queue != null) {
							queue.push(message);
						}
					} else {
						MessageBroker.sendMessage(subscribers[i], message);
					}
				}
			}
		}
	}

	public MessageRouter getMessageRouter() {
		return this;
	}

	public String getRouterName() {
		return Endpoint.LOCAL_ROUTER;
	}
}