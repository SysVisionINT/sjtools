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
import net.java.sjtools.messaging.Endpoint;
import net.java.sjtools.messaging.Listener;
import net.java.sjtools.messaging.MessageBroker;
import net.java.sjtools.messaging.Topic;
import net.java.sjtools.messaging.error.NoRouterError;
import net.java.sjtools.messaging.error.TimeoutException;
import net.java.sjtools.messaging.message.Event;
import net.java.sjtools.messaging.message.Message;
import net.java.sjtools.messaging.message.Request;
import net.java.sjtools.messaging.message.Response;
import net.java.sjtools.messaging.router.LocalRouter;
import net.java.sjtools.messaging.router.Router;
import net.java.sjtools.messaging.util.ReferenceUtil;

public abstract class AbstractActor implements Listener {

	private Endpoint endpoint = null;

	public AbstractActor(String actorName) {
		LocalRouter router = MessageBroker.getLocalRouter();

		endpoint = router.registerListener(actorName, this);
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void onMessage(Message message) {
		try {
			if (message instanceof Event) {
				Event event = (Event) message;
				processEvent(event);
			} else if (message instanceof Request) {
				Request request = (Request) message;
				processRequest(request);
			} else if (message instanceof Response) {
				Response response = (Response) message;
				processResponse(response);
			} else {
				processMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace(IO.err);
		}
	}

	protected void processMessage(Message message) {
		receiveCast(message.getMessageObject());
	}

	protected void processResponse(Response response) {
		receiveAsynchronousCallResponse(response.getReference(), response.getMessageObject());
	}

	protected void processRequest(Request request) {
		Object response = receiveCall(request.getMessageObject());
		MessageBroker.sendMessage(request.getReplyTo(), request.createResponse(response));
	}

	protected void processEvent(Event event) {
		receiveEvent(event.getEventName(), event.getMessageObject());
	}

	public void subscribeTopic(Endpoint endpoint) throws NoRouterError {
		Router router = MessageBroker.getRouter(endpoint.getRouterName());
		
		if (router != null) {
			Topic topic = router.getTopic(endpoint.getDestination());

			if (topic != null) {
				topic.subscribe(getEndpoint());
			} else {
				throw new NoRouterError(endpoint.toString());
			}
		} else {
			throw new NoRouterError(endpoint.toString());
		}
	}

	public void unsubscribeTopic(Endpoint endpoint) throws NoRouterError {
		Router router = MessageBroker.getRouter(endpoint.getRouterName());

		if (router != null) {
			Topic topic = router.getTopic(endpoint.getDestination());

			if (topic != null) {
				topic.unsubscribe(getEndpoint());
			} else {
				throw new NoRouterError(endpoint.toString());
			}
		} else {
			throw new NoRouterError(endpoint.toString());
		}
	}

	protected void event(String eventName, Object messageObject) throws NoRouterError {
		Router router = MessageBroker.getLocalRouter();
		Topic topic = router.getTopic(getTopicNameForEvent(eventName));
		
		MessageBroker.sendMessage(topic.getEndpoint(), new Event(eventName, messageObject));
	}

	public String getTopicNameForEvent(String eventName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("event/");
		buffer.append(eventName);
		
		return buffer.toString();
	}

	protected Object call(Endpoint address, Object messageObject) throws NoRouterError {
		return MessageBroker.call(address, messageObject);
	}
	
	protected Object call(Endpoint address, Object messageObject, long timeout) throws NoRouterError, TimeoutException {
		return MessageBroker.call(address, messageObject, timeout);
	}

	protected void cast(Endpoint address, Object messageObject) throws NoRouterError {
		if (!MessageBroker.sendMessage(address, new Message(messageObject))) {
			throw new NoRouterError(address.toString());
		}
	}

	protected String asynchronousCall(Endpoint address, Object messageObject) throws NoRouterError {
		String msgRef = ReferenceUtil.getMessageReference();

		if (!MessageBroker.sendMessage(address, new Request(endpoint, msgRef, messageObject))) {
			throw new NoRouterError(address.toString());
		}

		return msgRef;
	}	
	
	public abstract void receiveAsynchronousCallResponse(String referente, Object messageObject);

	public abstract void receiveCast(Object messageObject);

	public abstract Object receiveCall(Object messageObject);

	public abstract void receiveEvent(String eventName, Object messageObject);
}
