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

import net.java.sjtools.messaging.error.NoListenerError;
import net.java.sjtools.messaging.impl.CallQueue;
import net.java.sjtools.messaging.impl.MessageQueue;
import net.java.sjtools.messaging.message.Request;
import net.java.sjtools.messaging.model.ListenerRecord;
import net.java.sjtools.messaging.util.ReferenceUtil;
import net.java.sjtools.thread.Lock;

public class MessageBroker {

	private static MessageBroker messageBroker = new MessageBroker();

	private Map topicMap = null;
	private Lock topicLock = null;
	private Map listenerMap = null;
	private Lock listenerLock = null;
	private Map callMap = null;
	private Lock callLock = null;

	public static MessageBroker getInstance() {
		return messageBroker;
	}

	private MessageBroker() {
		topicMap = new HashMap();
		topicLock = new Lock(topicMap);

		listenerMap = new HashMap();
		listenerLock = new Lock(listenerMap);
		
		callMap = new HashMap();
		callLock = new Lock(callMap);
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

	private Topic getTopic(String topicName) {
		try {
			topicLock.getReadLock();
			return (Topic) topicMap.get(topicName);
		} finally {
			topicLock.releaseLock();
		}
	}
	
	public void registerListener(Listener listener) {
		if (listener != null) {
			registerListener(getListenerName(listener), listener);
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
			registerListener(getListenerName(listener), listener);
		}
	}

	public String getListenerName(Listener listener) {
		return listener.getClass().getName();
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
	
	public String[] getListenerNames() {
		try {
			listenerLock.getReadLock();
			Set listenerNames = listenerMap.keySet();

			return (String[]) listenerNames.toArray(new String[listenerNames.size()]);
		} finally {
			listenerLock.releaseLock();
		}
	}	
	
	public boolean isListenerRegistered(Listener listener) {
		if (listener != null) {
			return isListenerRegistered(getListenerName(listener));
		}
		
		return false;
	}
	
	public boolean isListenerRegistered(String name) {
		try {
			listenerLock.getReadLock();
			return listenerMap.containsKey(name);
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
	
	private MessageQueue getCallMessageQueue(String callRef) {
		try {
			callLock.getReadLock();
			return (MessageQueue) callMap.get(callRef);
		} finally {
			callLock.releaseLock();
		}
	}
	
	public boolean sendMessage(String listenerName, Message message) {
		MessageQueue queue = getListenerMessageQueue(listenerName);
		
		if (queue == null) {
			queue = getCallMessageQueue(listenerName);
		}
		
		if (queue != null) {
			queue.push(message);
			return true;
		}
		
		return false;
	}
	
	public Object call(String listenerName, Object data) throws NoListenerError {
		MessageQueue queue = getListenerMessageQueue(listenerName);
		
		if (queue != null) {
			String msgRef = ReferenceUtil.getMessageReference();
			String callRef = ReferenceUtil.getCallReference(msgRef);
			CallQueue callQueue = registerCall(callRef);
			
			Request request = new Request(callRef, msgRef, data);
			queue.push(request);
			
			Message response = callQueue.getMessage();
			unregisterCall(callRef);
			
			return response.getMessageObject();
		} else {
			throw new NoListenerError(listenerName);
		}
	}
	
	private CallQueue registerCall(String callRef) {
		try {
			callLock.getWriteLock();
			CallQueue queue = new CallQueue();
			
			callMap.put(callRef, queue);
			
			return queue;
		} finally {
			callLock.releaseLock();
		}
	}
	
	private void unregisterCall(String callRef) {
		try {
			callLock.getWriteLock();
			callMap.remove(callRef);
		} finally {
			callLock.releaseLock();
		}
	}	

	public void stop() {
		try {
			listenerLock.getWriteLock();
			topicLock.getWriteLock();
			callLock.getWriteLock();

			for (Iterator i = listenerMap.values().iterator(); i.hasNext();) {
				ListenerRecord registed = (ListenerRecord) i.next();
				registed.getMessageQueue().close();
			}
			
			for (Iterator i = callMap.values().iterator(); i.hasNext();) {
				MessageQueue queue = (MessageQueue) i.next();
				queue.close();
			}

			listenerMap.clear();
			topicMap.clear();
		} finally {
			callLock.releaseLock();
			listenerLock.releaseLock();
			topicLock.releaseLock();
		}
	}
}