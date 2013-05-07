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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Queue {
	private List queue = Collections.synchronizedList(new LinkedList());
	private Counter counter = new Counter();

	public void push(Object obj) {
		queue.add(obj);
		counter.increment();
	}
	
	public Object poll() {
		if (!queue.isEmpty()) {
			Object obj = queue.remove(0);
			counter.decrement();
			return obj;
		} else {
			return null;
		}
	}
	
	public Object peek() {
		if (!queue.isEmpty()) {
			Object obj = queue.get(0);
			return obj;
		} else {
			return null;
		}
	}	
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public long size() {
		return counter.getValue();
	}
	
    public void clean() {
        queue.clear();
    }
}
