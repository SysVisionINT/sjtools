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
package net.java.sjtools.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.java.sjtools.thread.Lock;

public class SafeMap {
	private Map container = new HashMap();
	private Lock lock = new Lock(container);
	
	public void clear() {
		lock.getWriteLock();
		container.clear();
		lock.releaseLock();
	}

	public boolean containsKey(Object key) {
		lock.getReadLock();
		boolean exists = container.containsKey(key);
		lock.releaseLock();
		
		if (exists) {
			SoftReference ref = getSoftReference(key);
			
			if (ref != null) {
				if (ref.get() == null) {
					removeSoftReference(key);
					
					return false;
				}
				
				return true;
			}	
			
			return true;
		}
		
		return false;
	}

	public Object get(Object key) {
		SoftReference ref = getSoftReference(key);
		
		if (ref != null) {
			Object obj = ref.get();
			
			if (obj == null) {
				removeSoftReference(key);
				
				return null;
			}
			
			return obj;
		}
		
		return null;
	}

	private SoftReference getSoftReference(Object key) {
		lock.getReadLock();
		SoftReference ref = (SoftReference) container.get(key);
		lock.releaseLock();
		
		return ref;
	}

	public boolean isEmpty() {
		lock.getReadLock();
		boolean empty = container.isEmpty();
		lock.releaseLock();
		
		return empty;
	}

	public Set keySet() {
		lock.getReadLock();
		Set keySet = container.keySet();
		lock.releaseLock();
		
		return keySet;
	}

	public void put(Object key, Object value) {
		lock.getWriteLock();
		container.put(key, new SoftReference(value));
		lock.releaseLock();
	}

	public void putAll(Map t) {
		Object key = null;
		
		for (Iterator i = t.keySet().iterator(); i.hasNext();) {
			key = i.next();
			
			put(key, t.get(key));
		}
	}

	public Object remove(Object key) {
		SoftReference ref = removeSoftReference(key);
		
		if (ref != null) {
			return ref.get();
		}
		
		return null;
	}

	private SoftReference removeSoftReference(Object key) {
		lock.getWriteLock();
		SoftReference ref = (SoftReference) container.remove(key);
		lock.releaseLock();
		
		return ref;
	}

	public int size() {
		lock.getReadLock();
		int size = container.size();
		lock.releaseLock();
		
		return size;
	}
}
