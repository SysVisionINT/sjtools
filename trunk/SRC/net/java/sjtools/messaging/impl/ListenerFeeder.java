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
import net.java.sjtools.messaging.model.Listener;
import net.java.sjtools.messaging.model.MessageRecord;
import net.java.sjtools.messaging.model.MessageStorage;
import net.java.sjtools.thread.SuperThread;

public class ListenerFeeder implements Listener, Runnable {
    private MessageStorage storage = null;
    private String storageKey = null;
    private Listener listener = null;
    private SuperThread thread = null;
    private int topicCount = 0;
    
    public int getTopicCount() {
        return topicCount;
    }
    
    public void incTopicCount() {
        topicCount++;
    }
    
    public void decTopicCount() {
        topicCount--;
    }    
    
    public ListenerFeeder(Listener listener, MessageStorage messageStorage) {
        this.listener = listener;
        storageKey = listener.getClass().getName();
        this.storage = messageStorage;
        
        thread = new SuperThread();
        thread.setDaemon(false);
        thread.setName("ListenerFeeder(" + thread.getName() + ")");
        thread.start();
    }

    public void process(Message message) {
        storage.store(storageKey, message);

        if (thread.getStatus() == SuperThread.WAITING) {
            thread.start(this);
        }
    }

    public void run() {
        MessageRecord message = null;
        
        while (!storage.isEmpty(storageKey)) {
            message = storage.getNextMessage(storageKey);
            
            try {
                listener.process(message.getMessage());
                storage.deleteMessage(storageKey, message.getRecordKey());
            } catch (Exception e) {
            }
        }
    }
    
    public void stop() {
        thread.die();
    }
    
    public void clean() {
        stop();
        storage.clean(storageKey);
    }    

    public Listener getListener() {
        return listener;
    }
}