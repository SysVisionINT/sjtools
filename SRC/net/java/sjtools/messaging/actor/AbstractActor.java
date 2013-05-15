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
package net.java.sjtools.messaging.actor;

import net.java.sjtools.io.IO;
import net.java.sjtools.messaging.Listener;
import net.java.sjtools.messaging.Message;
import net.java.sjtools.messaging.MessageBroker;
import net.java.sjtools.messaging.Topic;
import net.java.sjtools.messaging.error.NoListenerError;
import net.java.sjtools.messaging.message.Event;
import net.java.sjtools.messaging.message.Request;
import net.java.sjtools.messaging.message.Response;
import net.java.sjtools.messaging.util.ReferenceUtil;

public abstract class AbstractActor implements Listener {

	private String actorName = null;
	private MessageBroker broker = null;

	public AbstractActor(String actorName) {
		this.actorName = actorName;
		
		broker = MessageBroker.getInstance();

		broker.registerListener(actorName, this);
	}
	
	public AbstractActor() {
		this(ReferenceUtil.getActorReference());
	}
	
	public String getActorName() {
		return actorName;
	}

	public void onMessage(Message message) {
		try {
			if (message instanceof Event) {
				Event event = (Event) message;

				receiveEvent(event.getEventName(), event.getMessageObject());
			} else if (message instanceof Request) {
				Request request = (Request) message;
				Object response = receiveCall(request.getMessageObject());
				broker.sendMessage(request.getReplyTo(), request.createResponse(response));
			} else if (message instanceof Response) {
				Response response = (Response) message;
				receiveAsynchronousCallResponse(response.getReferente(), response.getMessageObject());
			} else {
				receiveCast(message.getMessageObject());
			}
		} catch (Exception e) {
			e.printStackTrace(IO.err);
		}
	}
	
	public void cast(String listenerName, Object messageObject) throws NoListenerError {
		if (!broker.sendMessage(listenerName, new Message(messageObject))) {
			throw new NoListenerError(listenerName);
		}
	}
	
	public String asynchronousCall(String listenerName, Object messageObject) throws NoListenerError {
		String msgRef = ReferenceUtil.getMessageReference();
		
		if (!broker.sendMessage(listenerName, new Request(actorName, msgRef, messageObject))) {
			throw new NoListenerError(listenerName);
		}
		
		return msgRef;
	}	
	
	public void subscribeEvent(String eventName) {
		Topic topic = broker.getTopic(eventName);
		
		topic.subscribe(actorName);
	}
	
	public void unsubscribeEvent(String eventName) {
		Topic topic = broker.getTopic(eventName);
		
		topic.unsubscribe(actorName);
	}
	
	protected void event(String eventName, Object messageObject) {
		Topic topic = broker.getTopic(eventName);
		
		topic.sendMessage(new Event(eventName, messageObject));
	}
	
	protected Object call(String listenerName, Object messageObject) throws NoListenerError {
		return broker.call(listenerName, messageObject);
	}

	protected abstract void receiveAsynchronousCallResponse(String referente, Object messageObject);

	protected abstract void receiveCast(Object messageObject);

	protected abstract Object receiveCall(Object messageObject);

	protected abstract void receiveEvent(String eventName, Object messageObject);

}
