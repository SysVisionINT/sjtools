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
package net.java.sjtools.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class Base64Util {
	public static final char[] MASTER_TABLE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'+', '/' };

	private static int DECODE_TABLE[] = new int[128];
	private static final char PAD = '=';
	private static final String CRLF = "\r\n";
	
	public static String encode(byte[] source) throws IOException {
		StringWriter writer = new StringWriter();
		
		encode(new InputStreamReader(new ByteArrayInputStream(source)), writer);
		
		return writer.toString();
	}
	
	public static byte[] decode(String source) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputStreamWriter os = new OutputStreamWriter(out);
		
		decode(new StringReader(source), os);
		
		os.flush();
		
		return out.toByteArray();
	}	

	public static void encode(Reader in, Writer out) throws IOException {
		int c, d, e, k = 0, end = 0;

		int u, v, w, x;

		while (end == 0) {
			if ((c = in.read()) == -1) {
				c = 0;
				end = 1;
			}

			if ((d = in.read()) == -1) {
				d = 0;
				end += 1;
			}

			if ((e = in.read()) == -1) {
				e = 0;
				end += 1;
			}  
			
			u = MASTER_TABLE[(c & 0xFC) >> 2];
			v = MASTER_TABLE[(0x03 & c) << 4 | (d & 0xF0) >> 4];
			w = MASTER_TABLE[(0x0F & d) << 2 | (e & 0xC0) >> 6];
			x = MASTER_TABLE[e & 0x3F];

			if (k == 76) {
				k = 0;

				out.write(CRLF);
			}

			if (end >= 1) {
				x = PAD;

				if (end == 2) {
					w = PAD;
				}
			}

			if (end < 3) {
				out.write(u);
				out.write(v);
				out.write(w);
				out.write(x);
			}

			k += 4;
		}
	}

	public static void decode(Reader in, Writer out) throws IOException {
		int c = 0, d = 0, e = 0, f = 0, i = 0, n = 0;

		do {
			f = in.read();

			if (f >= 0 && f < 128 && (i = DECODE_TABLE[f]) != -1) {
				if (n % 4 == 0) {
					c = i << 2;
				} else if (n % 4 == 1) {
					c = c | (i >> 4);
					d = (i & 0x0f) << 4;
				} else if (n % 4 == 2) {
					d = d | (i >> 2);
					e = (i & 0x03) << 6;
				} else {
					e = e | i;
				}

				n++;

				if (n % 4 == 0) {
					out.write(c);
					out.write(d);
					out.write(e);
				}
			}
		} while (f != -1);

		if (n % 4 == 3) {
			out.write(c);
			out.write(d);
		} else if (n % 4 == 2) {
			out.write(c);
		}
	}

	static {
		for (int i = 0; i < 128; i++) {
			DECODE_TABLE[i] = -1;
		}

		for (int i = 0; i < 64; i++) {
			DECODE_TABLE[(int) MASTER_TABLE[i]] = i;
		}
	}
}
