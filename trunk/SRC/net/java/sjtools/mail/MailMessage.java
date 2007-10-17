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
package net.java.sjtools.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import net.java.sjtools.mail.attach.MailAttach;

public class MailMessage implements Serializable {
	private static final long serialVersionUID = -1483657954547235668L;
	
	private boolean priorityMail = false;
	private InternetAddress from = null;
	private List to = new ArrayList();
	private List cc = new ArrayList();
	private List bcc = new ArrayList();
	private String subject = "";
	private String message = "";
	private boolean html = false;
	private List attaches = new ArrayList();
	
	public MailMessage(InternetAddress from) {
		this.from = from;
	}
	
	public MailMessage(String from) throws AddressException {
		this.from = new InternetAddress(from);
	}

	public boolean isPriorityMail() {
		return priorityMail;
	}

	public void setPriorityMail(boolean priorityMail) {
		this.priorityMail = priorityMail;
	}

	public InternetAddress getFrom() {
		return from;
	}
	
	public InternetAddress[] getTo() {
		return (InternetAddress[]) to.toArray(new InternetAddress[to.size()]);
	}

	public void addTo(InternetAddress to) {
		this.to.add(to);
	}
	
	public void setTo(String to) throws AddressException {
		InternetAddress[] addresses = InternetAddress.parse(to, false);
		
		for (int i = 0; i < addresses.length; i++) {
			this.to.add(addresses[i]);
		}
	}
	
	public boolean isTOEmpty() {
		return to.isEmpty();
	}
	
	public InternetAddress[] getCC() {
		return (InternetAddress[]) cc.toArray(new InternetAddress[cc.size()]);
	}

	public void addCC(InternetAddress cc) {
		this.cc.add(cc);
	}
	
	public void setCC(String cc) throws AddressException {
		InternetAddress[] addresses = InternetAddress.parse(cc, false);
		
		for (int i = 0; i < addresses.length; i++) {
			this.cc.add(addresses[i]);
		}
	}	
	
	public boolean isCCEmpty() {
		return cc.isEmpty();
	}
	
	public InternetAddress[] getBCC() {
		return (InternetAddress[]) bcc.toArray(new InternetAddress[bcc.size()]);
	}

	public void addBCC(InternetAddress bcc) {
		this.cc.add(bcc);
	}
	
	public void setBCC(String bcc) throws AddressException {
		InternetAddress[] addresses = InternetAddress.parse(bcc, false);
		
		for (int i = 0; i < addresses.length; i++) {
			this.bcc.add(addresses[i]);
		}
	}	
	
	public boolean isBCCEmpty() {
		return cc.isEmpty();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}	
	
	public void setMessage(String text, boolean isHtml) {
		message = text;
		html = isHtml;
	}

	public boolean isHtml() {
		return html;
	}

	public String getMessage() {
		return message;
	}
	
	public void addMailAttach(MailAttach attach) {
		attaches.add(attach);
	}
	
	public Collection getMailAttaches() {
		return attaches;
	}
}
