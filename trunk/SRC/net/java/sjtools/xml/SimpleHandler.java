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

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class SimpleHandler extends DefaultHandler {
    private Stack currentElement = new Stack();
    private XMLElement rootObject = null;
    private Stack currentObject = new Stack();

    private Map dtdResolver = new HashMap();

    public void addDTD(String publicId, String dtdName) {
        dtdResolver.put(publicId, dtdName);
    }

    public Object getRootElement() {
        return rootObject.getElementObject();
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        Object fileName = dtdResolver.get(publicId);

        if (fileName != null) {
            InputSource is = new InputSource((String) fileName);
            is.setPublicId(publicId);
            return is;
        } else {
            throw new SAXException(publicId + " is not known!");
        }
    }

    public void startElement(String namespace, String localname, String type, Attributes attributes)
            throws SAXException {

        XMLElement current = null;
        Object obj = null;

        if (!currentObject.empty()) {
            current = (XMLElement) currentObject.peek();
            obj = proccessElement(localname, current.getElementObject(), attributes);
        } else {
            obj = proccessElement(localname, null, attributes);
        }

        currentElement.push(new XMLElement(localname, obj));
        
        if (obj != null) {
            currentObject.push(currentElement.peek());

            if (rootObject == null) {
                rootObject = (XMLElement) currentElement.peek();
            }
        }
    }

    public abstract Object proccessElement(String elementType, Object currentObject, Attributes attributes) throws SAXException;

    public void endElement(String namespace, String localname, String type) throws SAXException {
        XMLElement element = (XMLElement) currentElement.peek();

        if (element.getElementName().equals(localname)) {
            currentElement.pop();
            
            element = (XMLElement) currentObject.peek();
            
            if (element.getElementName().equals(localname)) {
                currentObject.pop();
            }
        } else {
            throw new SAXException("Element " + localname + " ended when expected " + element.getElementName());
        }
    }

    public void characters(char[] ch, int start, int len) throws SAXException {
        String text = new String(ch, start, len);

        if (text.trim().length() > 0) {
            XMLElement elementName = (XMLElement) currentElement.peek();
            XMLElement elementObject = (XMLElement) currentObject.peek();
            
            processPCDATA(elementName.getElementName(), elementObject.getElementObject(), text);
        }
    }

    public abstract void processPCDATA(String elementType, Object currentObject, String value) throws SAXException;
}