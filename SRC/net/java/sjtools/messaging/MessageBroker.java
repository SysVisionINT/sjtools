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
import java.util.Map;
import java.util.Set;

import net.java.sjtools.messaging.error.MessageBrokerClosedException;
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
    private boolean closed = false;
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

    public Topic createTopic(String topicName) throws MessageBrokerClosedException {
        if (closed) {
            throw new MessageBrokerClosedException();
        }

        Topic topic = getTopic(topicName);

        if (topic == null) {
            topic = new Topic(topicName);
            topic.setBroker(this);

            topicLock.getWriteLock();
            topicMap.put(topicName, topic);
            topicLock.releaseLock();
        }

        return topic;
    }

    protected void removeTopic(Topic topic) {
        topicLock.getWriteLock();
        topicMap.remove(topic.getName());
        topicLock.releaseLock();
    }

    public String[] getTopicNames() {
        topicLock.getReadLock();
        Set topicNames = topicMap.keySet();
        topicLock.releaseLock();

        return (String[]) topicNames.toArray(new String[topicNames.size()]);
    }

    public boolean isClosed() {
        return closed;
    }

    public Topic getTopic(String topicName) throws MessageBrokerClosedException {
        if (closed) {
            throw new MessageBrokerClosedException();
        }
        
        topicLock.getReadLock();
        Topic topic = (Topic) topicMap.get(topicName);
        topicLock.releaseLock();

        return topic;
    }

    public void close() {
        String[] topicNames = getTopicNames();

        Topic topic = null;

        for (int i = 0; i < topicNames.length; i++) {
            try {
                topic = getTopic(topicNames[i]);
            } catch (MessageBrokerClosedException e) {}

            if (topic != null) {
                topic.close();
            }
        }

        if (messageStorage != null) {
            messageStorage.close();
        }
        
        closed = true;
    }

    protected Listener register(Listener listener) {
        listenerLock.getWriteLock();
        ListenerFeeder registed = (ListenerFeeder) listenerMap.get(listener.getClass());

        if (registed == null) {
            registed = new ListenerFeeder(listener, messageStorage);
            listenerMap.put(listener.getClass(), registed);
        }

        registed.incTopicCount();

        listenerLock.releaseLock();

        return registed;
    }

    protected Listener unregister(Listener listener) {
        listenerLock.getWriteLock();

        ListenerFeeder registed = null;

        if (listener instanceof ListenerFeeder) {
            registed = (ListenerFeeder) listenerMap.get(((ListenerFeeder) listener).getListener().getClass());
        } else {
            registed = (ListenerFeeder) listenerMap.get(listener.getClass());
        }

        if (registed != null) {
            registed.decTopicCount();

            if (registed.getTopicCount() == 0) {
                listenerMap.remove(listener.getClass());
                registed.clean();
            }
        }

        listenerLock.releaseLock();

        return registed;
    }

    protected void stop(Listener listener) {
        listenerLock.getWriteLock();

        ListenerFeeder registed = null;

        if (listener instanceof ListenerFeeder) {
            registed = (ListenerFeeder) listenerMap.get(((ListenerFeeder) listener).getListener().getClass());
        } else {
            registed = (ListenerFeeder) listenerMap.get(listener.getClass());
        }

        if (registed != null) {
            registed.decTopicCount();

            if (registed.getTopicCount() == 0) {
                listenerMap.remove(listener.getClass());
                registed.stop();
            }
        }

        listenerLock.releaseLock();
    }
}