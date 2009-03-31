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

import net.java.sjtools.messaging.impl.DefaultMessageStorage;
import net.java.sjtools.messaging.impl.ListenerFeeder;
import net.java.sjtools.messaging.model.Listener;
import net.java.sjtools.messaging.model.MessageStorage;
import net.java.sjtools.thread.Lock;

public class MessageBroker {
	private static MessageBroker messageBroker = new MessageBroker();

	private Map topicMap = null;
	private Lock topicLock = null;
	private Map listenerMap = null;
	private Lock listenerLock = null;
	private MessageStorage messageStorage = null;

	public static MessageBroker getInstance() {
		return messageBroker;
	}

	private MessageBroker() {
		topicMap = new HashMap();
		topicLock = new Lock(topicMap);

		setMessageStorage(new DefaultMessageStorage());

		listenerMap = new HashMap();
		listenerLock = new Lock(listenerMap);
	}

	public void setMessageStorage(MessageStorage storage) {
		if (messageStorage != null) {
			messageStorage.close();
		}

		messageStorage = storage;
		messageStorage.open();
	}

	public Topic createTopic(String topicName) {
		Topic topic = getTopic(topicName);

		if (topic == null) {
			topic = new Topic(topicName);
			register(topicName, topic);
		}

		return topic;
	}

	private void register(String topicName, Topic topic) {
		topicLock.getWriteLock();
		topicMap.put(topicName, topic);
		topicLock.releaseLock();
	}

	public String[] getTopicNames() {
		topicLock.getReadLock();
		Set topicNames = topicMap.keySet();
		topicLock.releaseLock();

		return (String[]) topicNames.toArray(new String[topicNames.size()]);
	}

	public Topic getTopic(String topicName) {
		topicLock.getReadLock();
		Topic topic = (Topic) topicMap.get(topicName);
		topicLock.releaseLock();

		return topic;
	}

	protected void register(String name, Listener listener) {
		listenerLock.getWriteLock();

		ListenerFeeder registed = (ListenerFeeder) listenerMap.get(name);

		if (registed == null) {
			registed = new ListenerFeeder(name, listener);
			listenerMap.put(name, registed);
		} else {
			registed.incTopicCount();
		}

		listenerLock.releaseLock();
	}

	protected void unregister(String name) {
		listenerLock.getWriteLock();

		ListenerFeeder registed = (ListenerFeeder) listenerMap.get(name);

		if (registed != null) {
			registed.decTopicCount();

			if (registed.getTopicCount() == 0) {
				listenerMap.remove(name);
				registed.stop();
			}
		}

		listenerLock.releaseLock();
	}

	public MessageStorage getMessageStorage() {
		return messageStorage;
	}

	public ListenerFeeder getListenerFeeder(String listenerName) {
		listenerLock.getReadLock();
		ListenerFeeder feeder = (ListenerFeeder) listenerMap.get(listenerName);
		listenerLock.releaseLock();

		return feeder;
	}
	
	public void stop() {
		listenerLock.getWriteLock();
		
		ListenerFeeder feeder = null;
		
		for (Iterator i =  listenerMap.values().iterator(); i.hasNext();) {
			feeder = (ListenerFeeder) i.next();
			feeder.stop();
		}
		
		listenerMap.clear();
		listenerLock.releaseLock();
		
		topicLock.getWriteLock();
		topicMap.clear();
		topicLock.releaseLock();
	}
}