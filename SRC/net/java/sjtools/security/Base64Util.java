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

import java.io.UnsupportedEncodingException;

public class Base64Util {
	private static final int MAX_LINE_LENGTH = 76;
	private static final byte NEW_LINE = (byte) '\n';
	private static final byte EQUALS_SIGN = (byte) '=';

	private static final byte WHITE_SPACE_ENC = -5;
	private static final byte EQUALS_SIGN_ENC = -1;

	public static final byte[] MASTER_TABLE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' };

	private static final byte[] DECODE_TABLE = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9,
			-9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62,
			-9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28,
			29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };

	public static final String encode(byte[] source) {
		return encode(source, 0, source.length);
	}

	public static final String encode(final byte[] src, final int off, final int len) {
		final int len43 = len * 4 / 3;
		final byte[] outBuff = new byte[len43 // Main 4:3
				+ ((len % 3) > 0 ? 4 : 0) // Account for padding
				+ (len43 / MAX_LINE_LENGTH)]; // New lines
		int d = 0;
		int e = 0;
		final int len2 = len - 2;
		int lineLength = 0;
		for (; d < len2; d += 3, e += 4) {
			encode3to4(src, d + off, 3, outBuff, e);
			lineLength += 4;
			if (lineLength == MAX_LINE_LENGTH) {
				outBuff[e + 4] = NEW_LINE;
				e++;
				lineLength = 0;
			}
		}

		if (d < len) { // padding needed
			encode3to4(src, d + off, len - d, outBuff, e);
			e += 4;
		}

		return new String(outBuff, 0, e);
	}

	public static final byte[] decode(final String s) throws UnsupportedEncodingException {
		final byte[] bytes;
		bytes = s.getBytes("US-ASCII");
		return decode(bytes, 0, bytes.length);
	}

	public static byte[] decode(final byte[] src, final int off, final int len) {
		final int len34 = len * 3 / 4;
		final byte[] outBuff = new byte[len34]; // Upper limit on size of output
		int outBuffPosn = 0;
		final byte[] b4 = new byte[4];
		int b4Posn = 0;
		int i;
		byte sbiCrop, sbiDecode;
		for (i = off; i < off + len; i++) {
			sbiCrop = (byte) (src[i] & 0x7F); // Only the low seven bits
			sbiDecode = DECODE_TABLE[sbiCrop];
			if (sbiDecode >= WHITE_SPACE_ENC) { // White space, Equals sign or better
				if (sbiDecode >= EQUALS_SIGN_ENC) {
					b4[b4Posn++] = sbiCrop;
					if (b4Posn > 3) {
						outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
						b4Posn = 0;
						// If that was the equals sign, break out of 'for' loop
						if (sbiCrop == EQUALS_SIGN)
							break;
					} // end if: quartet built
				} // end if: equals sign or better
			} else {
				throw new IllegalArgumentException("Illegal BASE-64 character at #" + i + ": " + src[i] + "(decimal)");
			}
		}

		final byte[] result = new byte[outBuffPosn];
		System.arraycopy(outBuff, 0, result, 0, outBuffPosn);

		return result;
	}

	private static final byte[] encode3to4(final byte[] src, final int sOffset, final int numBytes, final byte[] dest,
			final int dOffset) {
		final int inBuff = (numBytes > 0 ? ((src[sOffset] << 24) >>> 8) : 0)
				| (numBytes > 1 ? ((src[sOffset + 1] << 24) >>> 16) : 0)
				| (numBytes > 2 ? ((src[sOffset + 2] << 24) >>> 24) : 0);
		switch (numBytes) {
		case 3:
			dest[dOffset] = MASTER_TABLE[(inBuff >>> 18)];
			dest[dOffset + 1] = MASTER_TABLE[(inBuff >>> 12) & 0x3F];
			dest[dOffset + 2] = MASTER_TABLE[(inBuff >>> 6) & 0x3F];
			dest[dOffset + 3] = MASTER_TABLE[(inBuff) & 0x3F];
			break;
		case 2:
			dest[dOffset] = MASTER_TABLE[(inBuff >>> 18)];
			dest[dOffset + 1] = MASTER_TABLE[(inBuff >>> 12) & 0x3F];
			dest[dOffset + 2] = MASTER_TABLE[(inBuff >>> 6) & 0x3F];
			dest[dOffset + 3] = EQUALS_SIGN;
			break;
		case 1:
			dest[dOffset] = MASTER_TABLE[(inBuff >>> 18)];
			dest[dOffset + 1] = MASTER_TABLE[(inBuff >>> 12) & 0x3F];
			dest[dOffset + 2] = EQUALS_SIGN;
			dest[dOffset + 3] = EQUALS_SIGN;
			break;
		}
		return dest;
	}

	private static final int decode4to3(final byte[] src, final int sOffset, final byte[] dest, final int dOffset) {
		if (src[sOffset + 2] == EQUALS_SIGN) { // Example: Dk==
			final int outBuff = ((DECODE_TABLE[src[sOffset]] & 0xFF) << 18)
					| ((DECODE_TABLE[src[sOffset + 1]] & 0xFF) << 12);
			dest[dOffset] = (byte) (outBuff >>> 16);
			return 1;
		}

		if (src[sOffset + 3] == EQUALS_SIGN) { // Example: DkL=
			final int outBuff = ((DECODE_TABLE[src[sOffset]] & 0xFF) << 18)
					| ((DECODE_TABLE[src[sOffset + 1]] & 0xFF) << 12) | ((DECODE_TABLE[src[sOffset + 2]] & 0xFF) << 6);
			dest[dOffset] = (byte) (outBuff >>> 16);
			dest[dOffset + 1] = (byte) (outBuff >>> 8);
			return 2;
		}

		try { // Example: DkLE
			final int outBuff = ((DECODE_TABLE[src[sOffset]] & 0xFF) << 18)
					| ((DECODE_TABLE[src[sOffset + 1]] & 0xFF) << 12) | ((DECODE_TABLE[src[sOffset + 2]] & 0xFF) << 6)
					| ((DECODE_TABLE[src[sOffset + 3]] & 0xFF));
			dest[dOffset] = (byte) (outBuff >> 16);
			dest[dOffset + 1] = (byte) (outBuff >> 8);
			dest[dOffset + 2] = (byte) outBuff;
			return 3;
		} catch (Exception x) {
			return -1;
		}
	}
}
