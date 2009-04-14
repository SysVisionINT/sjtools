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

import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.formatters.Formatter;

public class Column {

	private int position = -1;
	private boolean mandatory = true;

	private List preFormatValidators = new ArrayList();
	private List formatters = new ArrayList();
	private List postFormatValidators = new ArrayList();
	private String returnObjectProperty = null;
	private String returnObjectPropertyFormat = null;

	public Column(int position, boolean mandatory) {
		this.position = position;
		this.mandatory = mandatory;
	}

	public void addFormatter(Formatter formatter) {
		formatters.add(formatter);
	}

	public int getPosition() {
		return position;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public List getPreFormatValidators() {
		return preFormatValidators;
	}

	public void setPreFormatValidators(List preFormatValidators) {
		this.preFormatValidators = preFormatValidators;
	}

	public List getFormatters() {
		return formatters;
	}

	public void setFormatters(List formatters) {
		this.formatters = formatters;
	}

	public List getPostFormatValidators() {
		return postFormatValidators;
	}

	public void setPostFormatValidators(List postFormatValidators) {
		this.postFormatValidators = postFormatValidators;
	}

	public String getReturnObjectProperty() {
		return returnObjectProperty;
	}

	public void setReturnObjectProperty(String returnObjectProperty) {
		this.returnObjectProperty = returnObjectProperty;
	}

	public String getReturnObjectPropertyFormat() {
		return returnObjectPropertyFormat;
	}

	public void setReturnObjectPropertyFormat(String returnObjectPropertyFormat) {
		this.returnObjectPropertyFormat = returnObjectPropertyFormat;
	}

}
