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

import net.java.sjtools.messaging.error.NoRouterError;
import net.java.sjtools.messaging.error.TimeoutException;
import net.java.sjtools.messaging.message.Message;
import net.java.sjtools.messaging.message.Request;
import net.java.sjtools.messaging.queue.CallQueue;
import net.java.sjtools.messaging.queue.MessageQueue;
import net.java.sjtools.messaging.queue.RouterQueue;
import net.java.sjtools.messaging.router.LocalRouter;
import net.java.sjtools.messaging.router.Router;
import net.java.sjtools.messaging.util.ReferenceUtil;
import net.java.sjtools.thread.Lock;

public class MessageBroker {
	private static Map routerMap = null;
	private static Lock routerLock = null;
	private static Map routerQueueMap = null;
	private static Lock routerQueueLock = null;
	private static Map callMap = null;
	private static Lock callLock = null;

	public static void registerRouter(String name, Router router) {
		try {
			routerLock.getWriteLock();

			Router queue = (Router) routerMap.get(name);

			if (queue == null) {
				routerMap.put(name, router);

				try {
					routerQueueLock.getWriteLock();
					routerQueueMap.put(name, new RouterQueue(router.getMessageRouter()));
				} finally {
					routerQueueLock.releaseLock();
				}
			}
		} finally {
			routerLock.releaseLock();
		}
	}

	public static String[] getRouterNames() {
		try {
			routerLock.getReadLock();
			Set routerNames = routerMap.keySet();

			return (String[]) routerNames.toArray(new String[routerNames.size()]);
		} finally {
			routerLock.releaseLock();
		}
	}

	public static Router getRouter(String routerName) {
		try {
			routerLock.getReadLock();
			return (Router) routerMap.get(routerName);
		} finally {
			routerLock.releaseLock();
		}
	}
	
	public static LocalRouter getLocalRouter() {
		return (LocalRouter) getRouter(Endpoint.LOCAL_ROUTER);
	}	

	private static MessageQueue getRouterQueue(String routerName) {
		try {
			routerQueueLock.getReadLock();
			return (MessageQueue) routerQueueMap.get(routerName);
		} finally {
			routerQueueLock.releaseLock();
		}
	}

	private static MessageQueue getCallMessageQueue(String callRef) {
		try {
			callLock.getReadLock();
			return (MessageQueue) callMap.get(callRef);
		} finally {
			callLock.releaseLock();
		}
	}

	public static boolean sendMessage(Endpoint endpoint, Message message) {
		MessageQueue queue = null;

		if (endpoint.isLocal()) {
			queue = getCallMessageQueue(endpoint.getDestination());
		}

		if (queue == null) {
			queue = getRouterQueue(endpoint.getRouterName());
		}

		if (queue != null) {
			queue.push(endpoint, message);
			return true;
		}

		return false;
	}

	public static Object call(Endpoint endpoint, Object data) throws NoRouterError {
		try {
			return call(endpoint, data, 0);
		} catch (TimeoutException e) {
			// No TimeoutException with timeout=0
		}
		
		return null;
	}
	
	public static Object call(Endpoint endpoint, Object data, long timeout) throws NoRouterError, TimeoutException {
		String msgRef = ReferenceUtil.getMessageReference();
		String callRef = ReferenceUtil.getCallReference(msgRef);

		try {
			CallQueue callQueue = registerCall(callRef);

			Endpoint callEndpoint = Endpoint.getLocalEndpointForDestination(callRef);

			Request request = new Request(callEndpoint, msgRef, data);

			if (sendMessage(endpoint, request)) {
				Message response = null;
				
				if (timeout == 0) {
					response = callQueue.getMessage();
				} else {
					response = callQueue.getMessage(timeout);
				}

				return response.getMessageObject();
			} else {
				throw new NoRouterError(endpoint.toString());
			}
		} finally {
			unregisterCall(callRef);
		}
	}	

	private static CallQueue registerCall(String callRef) {
		try {
			callLock.getWriteLock();
			CallQueue queue = new CallQueue();

			callMap.put(callRef, queue);

			return queue;
		} finally {
			callLock.releaseLock();
		}
	}

	private static void unregisterCall(String callRef) {
		try {
			callLock.getWriteLock();
			callMap.remove(callRef);
		} finally {
			callLock.releaseLock();
		}
	}

	static {
		routerMap = new HashMap();
		routerLock = new Lock(routerMap);

		callMap = new HashMap();
		callLock = new Lock(callMap);

		routerQueueMap = new HashMap();
		routerQueueLock = new Lock(routerQueueMap);
		
		registerRouter(Endpoint.LOCAL_ROUTER, new LocalRouter());
	}
}