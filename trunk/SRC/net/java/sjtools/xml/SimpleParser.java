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
package net.java.sjtools.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class SimpleParser {
    private SimpleHandler handler = null;
    private XMLReader reader = null;
    
    public SimpleParser(SimpleHandler handler) throws Exception {
        this(handler, false);
    }
    
    public SimpleParser(SimpleHandler handler, boolean validate) throws Exception {
        this.handler = handler;
        
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setValidating(validate);
        saxParserFactory.setNamespaceAware(true);
        
        SAXParser saxParser = saxParserFactory.newSAXParser();
        reader = saxParser.getXMLReader();
        
        reader.setContentHandler(handler);
        reader.setEntityResolver(handler);
        reader.setErrorHandler(handler);
        reader.setDTDHandler(handler);        
    }    
    
    public void addDTD(String publicId, String dtdName) {
        handler.addDTD(publicId, dtdName);
    }
    
    public Object parse(String fileName) throws Exception {
    	File file = new File(fileName);

        reader.parse(new InputSource(file.toURI().toASCIIString()));

        return handler.getRootElement();
    }
    
    public Object parse(InputStream iStream) throws Exception {
        reader.parse(new InputSource(iStream));

        return handler.getRootElement();
	}
}