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

import net.java.sjtools.messaging.error.TopicClosedException;
import net.java.sjtools.messaging.model.Listener;
import net.java.sjtools.thread.Lock;

public class Topic {
    private List listenerList = null;
    private Lock listenerLock = null;
    private String name = null;
    private MessageBroker broker = null;
    private boolean closed = false;

    protected Topic(String name) {
        listenerList = new ArrayList();
        listenerLock = new Lock(listenerList);

        this.name = name;
    }

    protected void setBroker(MessageBroker broker) {
        this.broker = broker;
    }

    public String getName() {
        return name;
    }

    public boolean isClosed() {
        return closed;
    }
    
    public void sendMessage(Message msg) throws TopicClosedException {
        if (closed) {
            throw new TopicClosedException();
        }

        Listener listener = null;

        listenerLock.getReadLock();

        for (Iterator i = listenerList.iterator(); i.hasNext();) {
            listener = (Listener) i.next();

            listener.process(msg);
        }

        listenerLock.releaseLock();
    }

    public void subscribe(Listener listener) throws TopicClosedException {
        if (closed) {
            throw new TopicClosedException();
        }

        listenerLock.getWriteLock();
        listenerList.add(broker.register(listener));
        listenerLock.releaseLock();
    }

    protected void close() {
        listenerLock.getWriteLock();
        
        for (Iterator i = listenerList.iterator(); i.hasNext();) {
            broker.stop((Listener) i.next());
        }
        
        listenerList.clear();
        
        listenerLock.releaseLock();
        
        broker.removeTopic(this);
        
        closed = true;
    }

    public void unsubscribe(Listener listener) throws TopicClosedException {
        if (closed) {
            throw new TopicClosedException();
        }

        listenerLock.getWriteLock();

        listenerList.remove(broker.unregister(listener));

        if (listenerList.size() == 0) {
            broker.removeTopic(this);
        }

        listenerLock.releaseLock();
    }

    public int getNumberOfListeners() {
        listenerLock.getReadLock();
        int size = listenerList.size();
        listenerLock.releaseLock();

        return size;
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