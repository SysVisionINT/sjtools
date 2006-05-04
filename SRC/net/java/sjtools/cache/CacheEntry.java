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
package net.java.sjtools.cache;

import java.lang.ref.SoftReference;

public class CacheEntry {
	private SoftReference myRef = null;
	private long myTimestamp = 0;
	
	public CacheEntry(Object value) {
		myRef = new SoftReference(value);
		myTimestamp = System.currentTimeMillis();
	}

	public Object getObject() {
		return myRef.get();
	}
	
	public long getTimestamp() {
		return myTimestamp;
	}
}
