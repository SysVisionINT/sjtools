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
package net.java.sjtools.frameworks.tft.impl.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import net.java.sjtools.frameworks.tft.error.NotConnectedError;
import net.java.sjtools.frameworks.tft.error.OperationNotSupportedException;
import net.java.sjtools.frameworks.tft.error.TFTException;
import net.java.sjtools.frameworks.tft.impl.AbstractProtocolImpl;
import net.java.sjtools.frameworks.tft.impl.URLData;
import net.java.sjtools.util.NumberUtil;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;

public class HTTPClient extends AbstractProtocolImpl {

	private static final String TIMEOUT = "timeout";
	private static final String PROXY_HOST = "proxyHost";
	private static final String PROXY_PORT = "proxyPort";

	private HttpClient client = null;
	private boolean doAuthentication = false;
	private HttpMethod currentMethod = null;
	private String currentDir = null;

	public HTTPClient(URLData data) {
		super(data);
	}

	public void connect() throws TFTException {
		connect(null, null);
	}

	public void connect(String login, String password) throws TFTException {
		client = new HttpClient();

		if (login != null) {
			client.getState().setCredentials(null, null, new UsernamePasswordCredentials(login, password));
			doAuthentication = true;
		}

		if (getURLData().getProperty(TIMEOUT) != null) {
			String timeout = getURLData().getProperty(TIMEOUT);

			if (NumberUtil.isValidInteger(timeout)) {
				int timeoutValue = Integer.parseInt(timeout);

				client.setTimeout(timeoutValue);
				client.setConnectionTimeout(timeoutValue);
			} else {
				client = null;
				throw new TFTException("Parameter '" + TIMEOUT + "' is not valid!");
			}
		}

		if (getURLData().getProperty(PROXY_HOST) != null) {
			String proxyHost = getURLData().getProperty(PROXY_HOST);
			String proxyPort = getURLData().getProperty(PROXY_PORT);

			if (NumberUtil.isValidInteger(proxyPort)) {
				int portValue = Integer.parseInt(proxyPort);

				HostConfiguration configuration = new HostConfiguration();
				configuration.setProxy(proxyHost, portValue);

				client.setHostConfiguration(configuration);
			} else {
				client = null;
				throw new TFTException("Parameter '" + PROXY_PORT + "' is not valid!");
			}
		}

		if (getURLData().getPath() != null) {
			currentDir = getURLData().getPath();

			if (!currentDir.endsWith("/")) {
				currentDir = currentDir.concat("/");
			}
		} else {
			currentDir = "/";
		}
	}

	public void disconnect() {
		release();

		client = null;
	}

	private void release() {
		if (currentMethod != null) {
			currentMethod.releaseConnection();
			currentMethod = null;
		}
	}

	public void chdir(String path) throws TFTException {
		if (client == null) {
			throw new NotConnectedError();
		}

		if (path == null) {
			return;
		}

		String tmp = path;

		if (!tmp.endsWith("/")) {
			tmp = tmp.concat("/");
		}

		currentDir = currentDir.concat(tmp);
	}

	public void mkdir(String path) throws TFTException {
		throw new OperationNotSupportedException("mkdir()");
	}

	public Collection list() throws TFTException {
		throw new OperationNotSupportedException("list()");
	}

	public void delete(String fileName) throws TFTException {
		if (client == null) {
			throw new NotConnectedError();
		}

		release();

		String url = getURLForFile(fileName);

		DeleteMethod method = new DeleteMethod(url);

		if (doAuthentication) {
			method.setDoAuthentication(true);
		}

		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode == HttpStatus.SC_NOT_FOUND) {
					method.releaseConnection();
					throw new TFTException("File '" + fileName + "' not found!");
				} else {
					method.releaseConnection();
					throw new TFTException("Server response was " + statusCode + " to request: DELETE '" + url + "'");
				}
			}
		} catch (HttpException e) {
			method.releaseConnection();
			throw new TFTException("Error executing: delete('" + fileName + "')", e);
		} catch (IOException e) {
			method.releaseConnection();
			throw new TFTException("Error executing: delete('" + fileName + "')", e);
		} finally {
			method.releaseConnection();
		}
	}

	public void rename(String oldFileName, String newFileName) throws TFTException {
		throw new OperationNotSupportedException("rename()");
	}

	public InputStream get(String fileName) throws TFTException {
		if (client == null) {
			throw new NotConnectedError();
		}

		release();

		String url = getURLForFile(fileName);

		GetMethod method = new GetMethod(url);

		if (doAuthentication) {
			method.setDoAuthentication(true);
		}

		InputStream inputStream = null;

		try {
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode == HttpStatus.SC_NOT_FOUND) {
					method.releaseConnection();
					throw new TFTException("File '" + fileName + "' not found!");
				} else {
					method.releaseConnection();
					throw new TFTException("Server response was " + statusCode + " to request: GET '" + url + "'");
				}
			}

			inputStream = method.getResponseBodyAsStream();
		} catch (HttpException e) {
			method.releaseConnection();
			throw new TFTException("Error executing: get('" + fileName + "')", e);
		} catch (IOException e) {
			method.releaseConnection();
			throw new TFTException("Error executing: get('" + fileName + "')", e);
		}

		currentMethod = method;

		return inputStream;
	}

	private String getURLForFile(String fileName) {
		StringBuffer buffer = new StringBuffer();

		URLData data = getURLData();

		buffer.append(data.getProtocol());
		buffer.append("://");
		buffer.append(data.getServerName());

		if (data.getPortNumber() != null) {
			buffer.append(":");
			buffer.append(data.getPortNumber());
		}

		buffer.append("/");
		buffer.append(currentDir);
		buffer.append(fileName);

		return buffer.toString();
	}

	public void put(InputStream is, String fileName) throws TFTException {
		if (client == null) {
			throw new NotConnectedError();
		}

		release();

		String url = getURLForFile(fileName);

		PutMethod method = new PutMethod(url);

		if (doAuthentication) {
			method.setDoAuthentication(true);
		}

		try {
			method.setRequestBody(is);

			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				method.releaseConnection();
				throw new TFTException("Server response was " + statusCode + " to request: PUT '" + url + "'");
			}
		} catch (HttpException e) {
			method.releaseConnection();
			throw new TFTException("Error executing: put(InputStream, '" + fileName + "')", e);
		} catch (IOException e) {
			method.releaseConnection();
			throw new TFTException("Error executing: put(InputStream, '" + fileName + "')", e);
		} finally {
			method.releaseConnection();
		}
	}
}
