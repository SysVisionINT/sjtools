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
package net.java.sjtools.logging.plus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import net.java.sjtools.logging.Log;
import net.java.sjtools.logging.LogFactory;
import net.java.sjtools.logging.util.LogConfigReader;
import net.java.sjtools.time.SuperDate;
import net.java.sjtools.util.TextUtil;

public class RLog {

	private static final String DEFAULT_LOG_PROPERTY = "sjtools.logging.plus.default.logger";
	private static final String DEFAULT_LOG_VALUE = "RLOG_DEFAULT_LOGGER";

	private static ThreadLocal localLog = new ThreadLocal();
	private static ThreadLocal localID = new ThreadLocal();
	private static ThreadLocal localApplID = new ThreadLocal();
	private static ThreadLocal localTimestamp = new ThreadLocal();

	private static String baseRequestID = null;

	private static long lastID = 0;

	public static boolean isTraceEnabled() {
		return getCurrentLog().isTraceEnabled();
	}

	public static boolean isDebugEnabled() {
		return getCurrentLog().isDebugEnabled();
	}

	public static boolean isErrorEnabled() {
		return getCurrentLog().isErrorEnabled();
	}

	public static boolean isInfoEnabled() {
		return getCurrentLog().isInfoEnabled();
	}

	public static boolean isFatalEnabled() {
		return getCurrentLog().isFatalEnabled();
	}

	public static boolean isWarnEnabled() {
		return getCurrentLog().isWarnEnabled();
	}

	public static void trace(Object obj) {
		Log log = getCurrentLog();

		if (log.isTraceEnabled()) {
			log.trace(getMessage(obj));
		}
	}

	public static void trace(Object obj, Throwable throwable) {
		Log log = getCurrentLog();

		if (log.isTraceEnabled()) {
			log.trace(getMessage(obj), throwable);
		}
	}

	public static void debug(Object obj) {
		Log log = getCurrentLog();

		if (log.isDebugEnabled()) {
			log.debug(getMessage(obj));
		}
	}

	public static void debug(Object obj, Throwable throwable) {
		Log log = getCurrentLog();

		if (log.isDebugEnabled()) {
			log.debug(getMessage(obj), throwable);
		}
	}

	public static void info(Object obj) {
		Log log = getCurrentLog();

		if (log.isInfoEnabled()) {
			log.info(getMessage(obj));
		}
	}

	public static void info(Object obj, Throwable throwable) {
		Log log = getCurrentLog();

		if (log.isInfoEnabled()) {
			log.info(getMessage(obj), throwable);
		}
	}

	public static void warn(Object obj) {
		Log log = getCurrentLog();

		if (log.isWarnEnabled()) {
			log.warn(getMessage(obj));
		}
	}

	public static void warn(Object obj, Throwable throwable) {
		Log log = getCurrentLog();

		if (log.isWarnEnabled()) {
			log.warn(getMessage(obj), throwable);
		}
	}

	public static void error(Object obj) {
		getCurrentLog().error(getMessage(obj));
	}

	public static void error(Object obj, Throwable throwable) {
		getCurrentLog().error(getMessage(obj), throwable);
	}

	public static void fatal(Object obj) {
		getCurrentLog().fatal(getMessage(obj));
	}

	public static void fatal(Object obj, Throwable throwable) {
		getCurrentLog().fatal(getMessage(obj), throwable);
	}

	public static Log getCurrentLog() {
		Log log = (Log) localLog.get();

		if (log == null) {
			String loggerName = LogConfigReader.getParameter(DEFAULT_LOG_PROPERTY);

			if (loggerName == null) {
				loggerName = DEFAULT_LOG_VALUE;
			}

			log = LogFactory.getLog(loggerName);

			localLog.set(log);
		}

		return log;
	}

	public static String getRequestID() {
		String id = (String) localID.get();

		if (id == null) {
			return "00";
		}

		return id;
	}

	public static Object getApplID() {
		Object id = localApplID.get();

		if (id != null) {
			return id;
		}

		return null;
	}

	public static SuperDate getInitTimestamp() {
		SuperDate ts = (SuperDate) localTimestamp.get();

		if (ts != null) {
			return ts;
		}

		return null;
	}

	public static void setApplID(Object id) {
		localApplID.set(id);
	}

	private static String getMessage(Object msg) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getCurrentMethod());
		buffer.append("[");
		buffer.append(getRequestID());
		buffer.append("]");

		Object applID = getApplID();

		if (applID != null) {
			buffer.append("{");
			buffer.append(TextUtil.toString(applID));
			buffer.append("}");
		}

		buffer.append(" - ");

		buffer.append(msg);

		return buffer.toString();
	}

	private static String getCurrentMethod() {
		StackTraceElement ste = new Throwable().getStackTrace()[3];

		StringBuffer buffer = new StringBuffer();
		buffer.append(ste.getClassName());
		buffer.append(".");
		buffer.append(ste.getMethodName());

		return buffer.toString();
	}

	private static String getBaseRequestID() {
		if (baseRequestID == null) {
			generateBaseRequestID();
		}

		return baseRequestID;
	}

	private synchronized static void generateBaseRequestID() {
		if (baseRequestID != null) {
			return;
		}

		String ret = null;

		try {
			ret = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			ret = String.valueOf(new Random(System.currentTimeMillis()).nextInt());
		}

		baseRequestID = Long.toHexString(ret.hashCode()).concat("-");
	}

	private synchronized static long getNextID() {
		if (lastID == Long.MAX_VALUE) {
			return 1;
		} else {
			return ++lastID;
		}
	}

	public static void init(Log log) {
		localLog.set(log);
		localID.set(getBaseRequestID().concat(Long.toHexString(getNextID())));
		localApplID.set(null);
		localTimestamp.set(new SuperDate());
	}

	public static void changeLog(Log log) {
		localLog.set(log);
	}
}
