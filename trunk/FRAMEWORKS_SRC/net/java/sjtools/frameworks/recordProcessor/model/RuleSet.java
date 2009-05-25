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
package net.java.sjtools.frameworks.recordProcessor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.splitters.Splitter;

public class RuleSet implements Serializable {

	private static final long serialVersionUID = -8478911447456009968L;

	private String name = null;
	private Splitter splitter = null;
	private Integer minimumRecords = null;
	private Integer maximumRecords = null;
	private String returnObjectClass = null;
	private List columns = new ArrayList();

	public RuleSet(String name, Integer minimumRecords, Integer maximumRecords, String returnObjectClass) {
		this.name = name;
		this.minimumRecords = minimumRecords;
		this.maximumRecords = maximumRecords;
		this.returnObjectClass = returnObjectClass;
	}

	public void add(Column column) {
		columns.add(column);
	}

	public String getName() {
		return name;
	}

	public Splitter getSplitter() {
		return splitter;
	}

	public void setSplitter(Splitter splitter) {
		this.splitter = splitter;
	}

	public Integer getMinimumRecords() {
		return minimumRecords;
	}

	public Integer getMaximumRecords() {
		return maximumRecords;
	}

	public String getReturnObjectClass() {
		return returnObjectClass;
	}

	public List getColumns() {
		return columns;
	}

}
