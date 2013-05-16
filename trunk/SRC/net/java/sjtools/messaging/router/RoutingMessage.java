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
package net.java.sjtools.messaging.router;

import java.io.Serializable;

import net.java.sjtools.messaging.Endpoint;
import net.java.sjtools.messaging.message.Message;

public class RoutingMessage implements Serializable {
	private static final long serialVersionUID = -4173224364858370839L;
	
	private Endpoint endpoint = null;
	private Message message = null;
	
	public RoutingMessage(Endpoint endpoint, Message message) {
		this.endpoint = endpoint;
		this.message = message;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	public Message getMessage() {
		return message;
	}
}
