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
package net.java.sjtools.messaging.impl;

import net.java.sjtools.messaging.Message;
import net.java.sjtools.messaging.MessageBroker;
import net.java.sjtools.messaging.model.Listener;
import net.java.sjtools.messaging.model.MessageStorage;
import net.java.sjtools.messaging.model.StorageRecord;
import net.java.sjtools.time.Sleep;

public class ListenerFeeder implements Runnable {
	private String storageKey = null;
	private Listener listener = null;
	private Thread thread = null;
	private int topicCount = 1;
	private boolean run = true;

	public int getTopicCount() {
		return topicCount;
	}

	public void incTopicCount() {
		topicCount++;
	}

	public void decTopicCount() {
		topicCount--;
	}

	public ListenerFeeder(String listenerName, Listener listener) {
		this.listener = listener;
		storageKey = listenerName;

		thread = new Thread(this);
		thread.setDaemon(true);
		thread.setName("ListenerFeeder(" + thread.getName() + ")");
		thread.start();
	}

	public void delivery(Message message) {
		MessageBroker.getInstance().getMessageStorage().store(storageKey, message);
	}

	public void run() {
		MessageStorage storage = MessageBroker.getInstance().getMessageStorage();

		StorageRecord message = null;

		while (run) {
			if (!storage.hasMessages(storageKey)) {
				Sleep.seconds(1);
			} else {
				message = storage.getNextMessage(storageKey);

				try {
					listener.process(message.getMessage());
					storage.deleteMessage(storageKey, message.getRecordKey());
				} catch (Exception e) {
					Sleep.seconds(5);
				}
			}
		}
	}

	public void stop() {
		run = false;
		MessageBroker.getInstance().getMessageStorage().clean(storageKey);
	}
}