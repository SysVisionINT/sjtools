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

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;

import org.apache.struts.tiles.TilesRequestProcessor;

public class BasicController extends TilesRequestProcessor {

	private static Log log = LogFactory.getLog(BasicController.class);

	private BasicControllerConfig config = null;

	public boolean processPreprocess(HttpServletRequest request, HttpServletResponse response) {
		if (config == null) {
			config = (BasicControllerConfig) moduleConfig.getControllerConfig();
		}

		HttpSession session = request.getSession(false);

		String path = getRequestPath(request, response);

		if ((session == null) || (session.getAttribute(config.getSessionAtribute()) == null)) {
			if (isUnprotectAction(path)) {
				return super.processPreprocess(request, response);
			}

			//Se a action existir
			if (moduleConfig.findActionConfig(path) != null) {
				if (log.isWarnEnabled()) {
					log.warn(path + " can not be accessed without a session.");
				}

				try {
					gotoForwardName(request, response, config.getForward());
				} catch (Exception e) {}

				return false;
			}
		}

		return gotoAction(request, response, path);
	}


	protected void gotoForwardName(HttpServletRequest request, HttpServletResponse response, String forwardName) throws IOException, ServletException {
		processForwardConfig(request, response, moduleConfig.findForwardConfig(forwardName));
	}


	protected String getRequestPath(HttpServletRequest request, HttpServletResponse response) {
		String path = "";

		try {
			path = processPath(request, response);
		} catch (Exception e) {}

		return path;
	}


	protected boolean isUnprotectAction(String path) {
		for (Iterator i = config.getUnsecureActionsList().iterator(); i.hasNext();) {
			if (i.next().equals(path)) {
				return true;
			}
		}

		return false;
	}


	protected boolean gotoAction(HttpServletRequest request, HttpServletResponse response, String path) {
		return super.processPreprocess(request, response);
	}

}