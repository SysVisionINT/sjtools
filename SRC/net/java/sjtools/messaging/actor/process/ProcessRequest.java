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
package net.java.sjtools.messaging.actor.process;

import net.java.sjtools.io.IO;
import net.java.sjtools.messaging.MessageBroker;
import net.java.sjtools.messaging.actor.SupportingActor;
import net.java.sjtools.messaging.message.Request;

public class ProcessRequest implements Runnable {
	private SupportingActor actor = null;
	private Request request = null;
	
	public ProcessRequest(SupportingActor actor, Request request) {
		this.actor = actor;
		this.request = request;
	}

	public void run() {
		try {
			Object response = actor.receiveCall(request.getMessageObject());
			MessageBroker.sendMessage(request.getReplyTo(), request.createResponse(response));
		} catch (Exception e) {
			e.printStackTrace(IO.err);
		}
	}

}
