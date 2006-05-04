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

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 2816641117920581583L;
	
	private String sender = null;
    private long timestamp = 0;
    private Object messageObject = null;
    private String replayTo = null;
    private String messageID = null;

    public Message(Object messageObject) {       
        this.messageObject = messageObject;
        
        timestamp = System.currentTimeMillis();
    }

    public Object getMessageObject() {
        return messageObject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public String getMessageID() {
        if (messageID != null) {
            return messageID;
        }
        
        StringBuffer buffer = new StringBuffer();
        
        if (sender != null) {
            buffer.append(sender);
            buffer.append(":");
        }
        
        buffer.append(String.valueOf(timestamp));
        
        return buffer.toString();
    }
    
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
    
    public String getReplayTo() {
        return replayTo;
    }
    
    public void setReplayTo(String replayTo) {
        this.replayTo = replayTo;
    }
    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (!(obj instanceof Message)) {
            return false;
        }
        
        Message other = (Message)obj;
                
        return other.getMessageID().equals(getMessageID());
    }
}