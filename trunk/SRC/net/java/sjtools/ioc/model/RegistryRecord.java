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
package net.java.sjtools.ioc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegistryRecord implements Serializable {
	private static final long serialVersionUID = 5445592576463157449L;
	
	private List alias = new ArrayList();
	private Object value = null;

	public RegistryRecord(String name, Object value) {
		addName(name);
		setValue(value);
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void addName(String name) {
		alias.add(name);
	}

	public List getNameList() {
		return alias;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof RegistryRecord)) {
			return false;
		}

		RegistryRecord other = (RegistryRecord) obj;

		for (Iterator i = alias.iterator(); i.hasNext();) {
			if (other.alias.contains(i.next())) {
				return true;
			}
		}

		return false;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;

		buffer.append("[");

		for (Iterator i = alias.iterator(); i.hasNext();) {
			if (first) {
				first = false;
			} else {
				buffer.append(",");
			}

			buffer.append((String) i.next());
		}

		buffer.append("]");

		return buffer.toString();
	}

}
