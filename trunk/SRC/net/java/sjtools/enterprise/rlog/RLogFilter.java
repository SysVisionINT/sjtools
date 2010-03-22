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
package net.java.sjtools.enterprise.rlog;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.logging.plus.RLog;

public class RLogFilter implements Filter {
	private static final String PARAMETER_NAME = "logger";

	private String REQUEST_ATTRIBUTE = RLogFilter.class.getName();
	private String defaultLogName = null;
	private Log defaultLog = null;

	public void destroy() {}

	public void init(FilterConfig config) throws ServletException {
		defaultLogName = config.getInitParameter(PARAMETER_NAME);

		if (defaultLogName != null) {
			defaultLog = LogFactory.getLog(defaultLogName);
		} else {
			throw new SecurityException("No value for parameter " + PARAMETER_NAME);
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request.getAttribute(REQUEST_ATTRIBUTE) == null) {
			request.setAttribute(REQUEST_ATTRIBUTE, defaultLogName);

			RLog.init(defaultLog);
		}

		chain.doFilter(request, response);
	}
}
