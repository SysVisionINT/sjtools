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

import java.util.Date;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.java.sjtools.mail.attach.MailAttach;
import net.java.sjtools.mail.util.ByteArrayDataSource;

public class MailSender {
	private static final String UTF = "utf-8";
	private static final String HTML = "text/html";

	private Session mailSession = null;
	private String mailSystemName = MailSender.class.getName();

	private MailSender(Session session) {
		mailSession = session;
	}

	public static MailSender getInstance(Session session) {
		return new MailSender(session);
	}

	public String getMailSystemName() {
		return mailSystemName;
	}

	public void setMailSystemName(String mailSystemName) {
		this.mailSystemName = mailSystemName;
	}

	public void send(MailMessage mailMessage) throws MessagingException {
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(mailMessage.getFrom());

		if (!mailMessage.isTOEmpty()) {
			message.addRecipients(Message.RecipientType.TO, mailMessage.getTo());
		}

		if (!mailMessage.isCCEmpty()) {
			message.addRecipients(Message.RecipientType.CC, mailMessage.getCC());
		}

		if (!mailMessage.isBCCEmpty()) {
			message.addRecipients(Message.RecipientType.BCC, mailMessage.getBCC());
		}

		message.setSubject(mailMessage.getSubject(), UTF);

		if (mailMessage.isPriorityMail()) {
			message.setHeader("X-Priority", "1");
		}

		message.setHeader("X-Mailer", getMailSystemName());

		message.setSentDate(new Date());

		MimeBodyPart messageBodyPart = new MimeBodyPart();

		if (mailMessage.isHtml()) {
			messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(mailMessage.getMessage(), HTML)));
		} else {
			messageBodyPart.setText(mailMessage.getMessage(), UTF);
		}

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		MailAttach attach = null;

		for (Iterator i = mailMessage.getMailAttaches().iterator(); i.hasNext();) {
			attach = (MailAttach) i.next();

			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(new DataHandler(attach.getData()));
			messageBodyPart.setFileName(attach.getFileName());
			multipart.addBodyPart(messageBodyPart);
		}

		message.setContent(multipart);

		Transport.send(message);
	}
}
