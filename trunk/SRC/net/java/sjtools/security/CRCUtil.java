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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class CRCUtil {	
	public static int crc16(byte[] source) throws IOException {
		return crc16(new InputStreamReader(new ByteArrayInputStream(source)));
	}
	
	public static int crc16(Reader in) throws IOException {
		short crc = (short) 0xFFFF;
		int c;
		boolean c15, bit;

		while ((c = in.read()) != -1) {
			for (int i = 0; i < 8; i++) {
				c15 = ((crc >> 15 & 1) == 1);
				bit = ((c >> (7 - i) & 1) == 1);
				crc <<= 1;

				if (c15 ^ bit) {
					crc ^= 0x1021;
				}
			}
		}

		return crc;
	}	

	public static int crc32(byte[] source) throws IOException {
		return crc32(new InputStreamReader(new ByteArrayInputStream(source)));
	}
	
	public static int crc32(Reader in) throws IOException {
		int crc = 0xFFFFFFFF;
		int c;
		boolean c31, bit;

		while ((c = in.read()) != -1) {
			for (int i = 0; i < 8; i++) {
				c31 = ((crc >>> 31 & 1) == 1);
				bit = ((c >>> (7 - i) & 1) == 1);
				crc <<= 1;

				if (c31 ^ bit) {
					crc ^= 0x04C11DB7;
				}
			}
		}

		return crc;
	}
	
	public static int crcXor(byte[] source) throws IOException {
		return crcXor(new InputStreamReader(new ByteArrayInputStream(source)));
	}
	
	public static byte crcXor(Reader in) throws IOException{
		byte crc = 0;
		int c;
		
		while ((c = in.read()) != -1) {
			crc = (byte)(crc ^ c);
		}
		
		return crc;
	}
}
