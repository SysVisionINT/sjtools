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

import java.util.HashMap;
import java.util.Map;

import net.java.sjtools.messaging.Message;
import net.java.sjtools.messaging.model.MessageStorage;
import net.java.sjtools.messaging.model.StorageRecord;
import net.java.sjtools.thread.Lock;

public class DefaultMessageStorage  implements MessageStorage {
    private Map queueMap = null;
    private Lock lock = null;

    public DefaultMessageStorage() {
        queueMap = new HashMap();
        lock = new Lock(queueMap);
    }

    private MessageQueue getMessageQueue(String listenerName) {
        lock.getReadLock();
        MessageQueue queue = (MessageQueue) queueMap.get(listenerName);
        lock.releaseLock();

        return queue;
    }

    private void addMessageQueue(String listenerName, MessageQueue messageQueue) {
        lock.getWriteLock();
        queueMap.put(listenerName, messageQueue);
        lock.releaseLock();
    }

    private void deleteMessageQueue(String listenerName) {
        lock.getWriteLock();
        queueMap.remove(listenerName);
        lock.releaseLock();
    }

    public StorageRecord getNextMessage(String listenerName) {
        Message message = null;
        StorageRecord messageRecord = null;

        MessageQueue queue = getMessageQueue(listenerName);

        if (queue != null) {
            message = queue.getFirst();

            messageRecord = new StorageRecord("first", message);
        }

        return messageRecord;
    }

    public boolean hasMessages(String listenerName) {
        boolean empty = false;

        MessageQueue queue = getMessageQueue(listenerName);

        if (queue != null) {
            empty = !queue.isEmpty();
        }

        return empty;
    }

    public void store(String listenerName, Message message) {
        MessageQueue queue = getMessageQueue(listenerName);

        if (queue == null) {
            queue = new MessageQueue();

            addMessageQueue(listenerName, queue);
        }

        queue.add(message);
    }

    public void clean(String listenerName) {
        MessageQueue queue = getMessageQueue(listenerName);

        if (queue != null) {
            queue.clean();
        }

        deleteMessageQueue(listenerName);
    }

    public void open() {
        // Nada a fazer uma vez que fica tudo em memoria
    }

    public void deleteMessage(String listenerName, String recordKey) {
        MessageQueue queue = getMessageQueue(listenerName);

        if (queue != null) {
            queue.deleteFirst();
        }
    }

    public void close() {
        lock.getWriteLock();
        queueMap.clear();
        lock.releaseLock();
    }
}