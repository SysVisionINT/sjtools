/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2009 SysVision - Consultadoria e Desenvolvimento em Sistemas de Inform�tica, Lda.  
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
package net.java.sjtools.frameworks.recordProcessor.model.error;

public class MandatoryElementError extends ProcessorError {

	private static final long serialVersionUID = 3529246356181912397L;

	private int record = 0;
	private int position = 0;

	public MandatoryElementError(int record, int position) {
		super("Missing mandatory element at record " + record + ", column " + position);

		this.record = record;
		this.position = position;
	}

	public int getRecord() {
		return record;
	}

	public int getPosition() {
		return position;
	}

}
