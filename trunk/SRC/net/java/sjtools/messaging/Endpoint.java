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

import net.java.sjtools.messaging.error.InvalidEndpointException;

public class Endpoint implements Serializable {
	private static final long serialVersionUID = -4321920104279012067L;
	
	public static final String LOCAL_ROUTER = "local";
	private static final String ROUTER_SEPARATOR = "://";
	private static final String TYPE_SEPARATOR = "/";
	private static final String TYPE_TOPIC = "topic";
	private static final String TYPE_LISTENER = "listener";
	
	private String routerName = null;
	private String destination = null;
	private String type = null;
	
	private Endpoint(String routerName, String type, String destination) {
		this.routerName = routerName;
		this.type = type;
		this.destination = destination;
	}
	
	public static Endpoint getEndpointForDestination(String routerName, String destination) {
		return new Endpoint(routerName, TYPE_LISTENER, destination);
	}
	
	public static Endpoint getEndpointForTopic(String routerName, String topicName) {
		return new Endpoint(routerName, TYPE_TOPIC, topicName);
	}
	
	public static Endpoint getLocalEndpointForDestination(String destination) {
		return getEndpointForDestination(LOCAL_ROUTER, destination);
	}
	
	public static Endpoint getLocalEndpointForTopic(String topicName) {
		return getEndpointForTopic(LOCAL_ROUTER, topicName);
	}	
	
	public static Endpoint getEndpointFromString(String address) throws InvalidEndpointException {
		String routerName = null;
		String destination = null;
		String type = null;
		
		int routeSeparatorPos = address.indexOf(ROUTER_SEPARATOR);
		
		if (routeSeparatorPos <= 0) {
			throw new InvalidEndpointException(address);
		}
		
		routerName = address.substring(0, routeSeparatorPos);
		int startTypePos = routeSeparatorPos + ROUTER_SEPARATOR.length();
		
		int typeSeparatorPos = address.indexOf(TYPE_SEPARATOR, startTypePos);
		
		if (typeSeparatorPos == -1) {
			throw new InvalidEndpointException(address);
		}
		
		type = address.substring(startTypePos, typeSeparatorPos);
		
		if (!type.equals(TYPE_TOPIC) && !type.equals(TYPE_LISTENER)) {
			throw new InvalidEndpointException(address);
		}
		
		int startDestinationPos = typeSeparatorPos + TYPE_SEPARATOR.length();
		
		if (startDestinationPos >= address.length()) {
			throw new InvalidEndpointException(address);
		}
		
		destination = address.substring(startDestinationPos);
		
		return new Endpoint(routerName, type, destination);
	}
	
	public String getRouterName() {
		return routerName;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public boolean isLocal() {
		return routerName.equals(LOCAL_ROUTER);
	}
	
	public boolean isTopic() {
		return type.equals(TYPE_TOPIC);
	}
	
	public boolean isListener() {
		return type.equals(TYPE_LISTENER);
	}	

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(routerName);
		buffer.append(ROUTER_SEPARATOR);
		buffer.append(type);
		buffer.append(TYPE_SEPARATOR);
		buffer.append(destination);
		
		return buffer.toString();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (! (o instanceof Endpoint)) {
			return false;
		}
		
		Endpoint endpoint = (Endpoint) o;
		
		if (!routerName.equals(endpoint.routerName)) {
			return false;
		}
		
		if (!type.equals(endpoint.type)) {
			return false;
		}
		
		return destination.equals(endpoint.destination);
	}
}

