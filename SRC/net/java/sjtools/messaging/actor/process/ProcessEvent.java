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
import net.java.sjtools.messaging.actor.SupportingActor;
import net.java.sjtools.messaging.message.Event;

public class ProcessEvent implements Runnable {
	private SupportingActor actor = null;
	private Event event = null;
	
	public ProcessEvent(SupportingActor actor, Event event) {
		this.actor = actor;
		this.event = event;
	}

	public void run() {
		try {
			actor.receiveEvent(event.getEventName(), event.getMessageObject());
		} catch (Exception e) {
			e.printStackTrace(IO.err);
		}
	}
}
