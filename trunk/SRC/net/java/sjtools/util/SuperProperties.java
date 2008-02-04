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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


public class SuperProperties extends Properties {

	private static final long serialVersionUID = -1662922246812272570L;

	private static final String START = "${";
	private static final String END = "}";

	public SuperProperties() {
		super();
	}

	public SuperProperties(Properties defaults) {
		super(defaults);
	}

	public String getProperty(String key) {
		String value = super.getProperty(key);

		if (value != null && value.indexOf(START) != -1) {
			List keyList = getKeyList(value);

			String var = null;
			String varValue = null;

			for (Iterator i = keyList.iterator(); i.hasNext();) {
				var = (String) i.next();
				varValue = getProperty(getPropertyName(var));

				if (!TextUtil.isEmptyString(varValue)) {
					value = TextUtil.replace(value, var, varValue);
				}
			}
		}

		return value;
	}

	private String getPropertyName(String var) {
		return var.substring(2, var.length() - 1);
	}

	private List getKeyList(String value) {
		List list = new ArrayList();

		int startPos = value.indexOf(START);
		int endPos = 0;

		while (startPos != -1) {
			endPos = value.indexOf(END, startPos);

			if (endPos != -1) {
				list.add(value.substring(startPos, endPos + 1));

				startPos = value.indexOf(START, endPos);
			} else {
				break;
			}
		}

		return list;
	}
}
