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

import java.util.LinkedList;

import net.java.sjtools.messaging.Message;
import net.java.sjtools.thread.Lock;

public class MessageQueue {
    private LinkedList queue = null;
    private Lock lock = null;

    public MessageQueue() {
        queue = new LinkedList();
        lock = new Lock(queue);
    }

    public Message getFirst() {
        Message message = null;

        if (!isEmpty()) {
            lock.getWriteLock();
            message = (Message) queue.getFirst();
            lock.releaseLock();
        }

        return message;
    }

    public void deleteFirst() {
        if (!isEmpty()) {
            lock.getWriteLock();
            queue.removeFirst();
            lock.releaseLock();
        }
    }

    public boolean isEmpty() {
        lock.getReadLock();
        boolean empty = queue.isEmpty();
        lock.releaseLock();

        return empty;
    }

    public void add(Message message) {
        lock.getWriteLock();
        queue.addLast(message);
        lock.releaseLock();
    }

    public void clean() {
        lock.getWriteLock();
        queue.clear();
        lock.releaseLock();
    }
}