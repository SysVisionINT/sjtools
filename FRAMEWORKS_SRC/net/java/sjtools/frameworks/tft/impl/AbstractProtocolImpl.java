/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2009 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
package net.java.sjtools.frameworks.tft.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.frameworks.tft.error.TFTException;
import net.java.sjtools.frameworks.tft.model.TFTClient;
import net.java.sjtools.frameworks.tft.model.TFTFile;
import net.java.sjtools.frameworks.tft.model.TFTFileFilter;

public abstract class AbstractProtocolImpl implements TFTClient {
	private URLData data = null;
	
	public AbstractProtocolImpl(URLData data) {
		this.data = data;
	}
	
	protected URLData getURLData() {
		return data;
	}
	
	public Collection list(TFTFileFilter filter) throws TFTException {
		Collection allFiles = list();
		
		if (filter == null) {
			return allFiles;
		}
		
		List filtered = new ArrayList();
		
		for (Iterator i = allFiles.iterator(); i.hasNext();) {
			TFTFile file = (TFTFile) i.next();
			
			if (filter.accept(file)) {
				filtered.add(file);
			}
		}
		
		return filtered;
	}
}
