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
package net.java.sjtools.frameworks.recordProcessor.xml;

import java.io.InputStream;

import net.java.sjtools.frameworks.recordProcessor.model.RuleSets;
import net.java.sjtools.xml.SimpleParser;

public class Reader {

	public static RuleSets read(InputStream inputStream) throws Exception {
		SimpleParser parser = new SimpleParser(new Handler(), true);

		parser.addDTD("-//SJTools//DTD Record Processor 1.0//EN", Thread.currentThread().getContextClassLoader().getResource("recordprocessor.dtd").toString());

		return (RuleSets) parser.parse(inputStream);
	}

}
