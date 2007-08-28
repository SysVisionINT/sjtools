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
package net.java.sjtools.struts.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.struts.config.ControllerConfig;

public class BasicControllerConfig extends ControllerConfig {
	private static final long serialVersionUID = -5029442802553619443L;
	
	private String forward = "";
	private String unsecureActions = "";
	private String sessionAtribute = "";
	private List unsecureActionsList = null;

	public String getForward() {
		return forward;
	}

	public void setForward(String value) {
		forward = value;
	}

	private void parseUnsecureActions() {
		StringTokenizer st = new StringTokenizer(unsecureActions, ",");

		unsecureActionsList = new ArrayList();

		while (st.hasMoreTokens()) {
			unsecureActionsList.add(st.nextToken().trim());
		}
	}

	public String getUnsecureActions() {
		return unsecureActions;
	}

	public void setUnsecureActions(String value) {
		unsecureActions = value;
		parseUnsecureActions();
	}

	public String getSessionAtribute() {
		return sessionAtribute;
	}

	public void setSessionAtribute(String value) {
		sessionAtribute = value;
	}

	public List getUnsecureActionsList() {
		return unsecureActionsList;
	}

}