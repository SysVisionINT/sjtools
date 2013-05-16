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

import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.thread.Lock;

public class Topic {
	private List subscriberList = null;
	private Lock subscriberLock = null;
	private Endpoint endpoint = null;

	public Topic(Endpoint endpoint) {
		subscriberList = new ArrayList();
		subscriberLock = new Lock(subscriberList);

		this.endpoint = endpoint;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void subscribe(Endpoint endpoint) {
		if (!isSubscriber(endpoint)) {			
			try {
				subscriberLock.getWriteLock();
				subscriberList.add(endpoint);
			} finally {
				subscriberLock.releaseLock();
			}
		}
	}

	public void unsubscribe(Endpoint endpoint) {
		if (isSubscriber(endpoint)) {
			try {
				subscriberLock.getWriteLock();
				subscriberList.remove(endpoint);
			} finally {
				subscriberLock.releaseLock();
			}
		}
	}

	private boolean isSubscriber(Endpoint endpoint) {
		try {
			subscriberLock.getReadLock();

			return subscriberList.contains(endpoint);
		} finally {
			subscriberLock.releaseLock();
		}
	}

	public Endpoint[] getSubscribers() {
		try {
			subscriberLock.getReadLock();
			return (Endpoint[]) subscriberList.toArray(new Endpoint[subscriberList.size()]);
		} finally {
			subscriberLock.releaseLock();
		}
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof Topic)) {
			return false;
		}

		Topic other = (Topic) obj;

		return other.endpoint.equals(endpoint);
	}
}