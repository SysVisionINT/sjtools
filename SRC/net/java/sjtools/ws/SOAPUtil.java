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
package net.java.sjtools.ws;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SOAPUtil {

	public static InputStream makeRequest(String url, String action, byte[] envelope) throws SOAPError {
		try {
			HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();

			httpConn.setRequestProperty("Content-Length", String.valueOf(envelope.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", action);

			httpConn.setRequestMethod("POST");

			//TODO Quando se usar  java > 1.4, colocar timeout

			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);

			OutputStream outputStream = null;
			try {
				outputStream = httpConn.getOutputStream();

				outputStream.write(envelope);
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (Exception e) {}
				}
			}

			return httpConn.getInputStream();
		} catch (Exception e) {
			throw new SOAPError("Error connecting", e);
		}
	}

	public static InputStream makeRequest(String url, String action, String envelope) throws SOAPError {
		return makeRequest(url, action, envelope.getBytes());
	}

	public static byte[] request(String url, String action, byte[] envelope) throws SOAPError {
		InputStream inputStream = null;
		try {
			inputStream = makeRequest(url, action, envelope);

			byte[] responseEnvelope = new byte[inputStream.available()];

			inputStream.read(responseEnvelope);

			return responseEnvelope;
		} catch (SOAPError e) {
			throw e;
		} catch (Exception e) {
			throw new SOAPError("Error reading response", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {}
			}
		}
	}

	public static String request(String url, String action, String envelope) throws SOAPError {
		return new String(request(url, action, envelope.getBytes()));
	}

}
