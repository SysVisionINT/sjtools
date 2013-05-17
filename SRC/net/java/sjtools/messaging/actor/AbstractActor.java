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
import net.java.sjtools.messaging.message.Event;
import net.java.sjtools.messaging.message.Message;
import net.java.sjtools.messaging.message.Request;
import net.java.sjtools.messaging.message.Response;
import net.java.sjtools.messaging.router.LocalRouter;
import net.java.sjtools.messaging.router.Router;
import net.java.sjtools.messaging.util.ReferenceUtil;

public abstract class AbstractActor implements Listener {

	private Endpoint actorAddress = null;

	public AbstractActor(String actorName) {
		LocalRouter router = MessageBroker.getLocalRouter();

		actorAddress = router.registerListener(actorName, this);
	}

	public AbstractActor() {
		this(ReferenceUtil.getActorReference());
	}

	public Endpoint getActorAddress() {
		return actorAddress;
	}

	public void onMessage(Message message) {
		try {
			if (message instanceof Event) {
				Event event = (Event) message;

				receiveEvent(event.getEventName(), event.getMessageObject());
			} else if (message instanceof Request) {
				Request request = (Request) message;
				Object response = receiveCall(request.getMessageObject());
				MessageBroker.sendMessage(request.getReplyTo(), request.createResponse(response));
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

	public void cast(Endpoint address, Object messageObject) throws NoRouterError {
		if (!MessageBroker.sendMessage(address, new Message(messageObject))) {
			throw new NoRouterError(address.toString());
		}
	}

	public String asynchronousCall(Endpoint address, Object messageObject) throws NoRouterError {
		String msgRef = ReferenceUtil.getMessageReference();

		if (!MessageBroker.sendMessage(address, new Request(actorAddress, msgRef, messageObject))) {
			throw new NoRouterError(address.toString());
		}

		return msgRef;
	}

	public void subscribeEvent(Endpoint endpoint) throws NoRouterError {
		Router router = MessageBroker.getRouter(endpoint.getRouterName());

		if (router != null) {
			Topic topic = router.getTopic(endpoint.getDestination());

			topic.subscribe(actorAddress);
		} else {
			throw new NoRouterError(endpoint.toString());
		}
	}

	public void unsubscribeEvent(Endpoint endpoint) throws NoRouterError {
		Router router = MessageBroker.getRouter(endpoint.getRouterName());

		if (router != null) {
			Topic topic = router.getTopic(endpoint.getDestination());

			topic.unsubscribe(actorAddress);
		} else {
			throw new NoRouterError(endpoint.toString());
		}
	}

	protected void event(String eventName, Object messageObject) throws NoRouterError {
		Router router = MessageBroker.getLocalRouter();
		Topic topic = router.getTopic(eventName);

		MessageBroker.sendMessage(topic.getEndpoint(), new Event(eventName, messageObject));
	}

	protected Object call(Endpoint address, Object messageObject) throws NoRouterError {
		return MessageBroker.call(address, messageObject);
	}

	protected abstract void receiveAsynchronousCallResponse(String referente, Object messageObject);

	protected abstract void receiveCast(Object messageObject);

	protected abstract Object receiveCall(Object messageObject);

	protected abstract void receiveEvent(String eventName, Object messageObject);

}
