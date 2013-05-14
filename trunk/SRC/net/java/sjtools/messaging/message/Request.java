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
package net.java.sjtools.messaging.message;

import net.java.sjtools.messaging.Message;
import net.java.sjtools.messaging.util.ReferenceUtil;

public class Request extends Message {
	private static final long serialVersionUID = 4353178246757520713L;
	
	private String replyTo = null;
	private String referente = null;
	
	public Request(String replyTo, Object data) {
		this(replyTo, ReferenceUtil.getMessageReference(), data);
	}
	
	public Request(String replyTo, String referente, Object data) {
		super(data);
		this.replyTo = replyTo;
		this.referente = referente;
	}
	
	public String getReplyTo() {
		return replyTo;
	}
	
	public String getReferente() {
		return referente;
	}

	public Response createResponse(Object data) {
		return new Response(referente, data);
	}
}
