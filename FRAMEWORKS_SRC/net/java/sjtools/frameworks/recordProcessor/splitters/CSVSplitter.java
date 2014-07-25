/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2011 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
package net.java.sjtools.frameworks.recordProcessor.splitters;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.model.error.InvalidRecordError;
import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;

public class CSVSplitter implements Splitter, Serializable {

	private static final long serialVersionUID = -8485539742010434147L;

	private Integer elementCount = null;
	private char separator = '\0';
	private char quote = '\0';
	private boolean skipHeader = false;

	private InputStream inputStream = null;

	public CSVSplitter(Integer elementCount, String separator, String quote, boolean skipHeader) throws ProcessorError {
		try {
			if (separator.length() != 1) {
				throw new ProcessorError("Separator [" + separator + "] should be a char");
			}

			if (quote.length() != 1) {
				throw new ProcessorError("Quote [" + quote + "] should be a char");
			}

			this.elementCount = elementCount;
			this.separator = separator.charAt(0);
			this.quote = quote.charAt(0);
			this.skipHeader = skipHeader;
		} catch (ProcessorError e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public void init(InputStream inputStream) throws ProcessorError {
		this.inputStream = inputStream;

		if (skipHeader && hasNext()) {
			nextRecord(); // Return is ignored
		}
	}

	public boolean hasNext() throws ProcessorError {
		try {
			return inputStream.available() != 0;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public List nextRecord() throws ProcessorError {
		try {
			StringBuffer record = new StringBuffer();
			StringBuffer field = new StringBuffer();

			List ret = new ArrayList();

			int b = 0;
			char ch = 0;
			char lastCh = 0;

			boolean quotedField = false;
			boolean skipedCR = false;

			while (true) {
				b = inputStream.read();

				if (b == -1) { //EOF
					if (field.length() > 0) {
						ret.add(field.toString());
					}

					if (ret.size() == 0) {
						// net.java.sjtools.frameworks.recordProcessor.Processor.process stops when nextRecord returns null
						ret = null;
					}

					break;
				}

				ch = (char) b;

				record.append(ch);

				if (!quotedField && (lastCh == '\r' || skipedCR) && ch != '\n') {
					skipedCR = false;
					field.append('\r');
				}

				if (!quotedField || lastCh == quote) {
					if (ch == '\r') {
						skipedCR = true;
						continue;
					}

					if (ch == separator || ch == '\n') {
						ret.add(field.toString());
						field = new StringBuffer();

						quotedField = false;
						lastCh = 0;

						if (ch == '\n') {
							break;
						}

						continue;
					}
				}

				if (ch == quote) {
					if (!quotedField) {
						if (field.length() == 0) {
							quotedField = true;
							continue;
						}
					}

					if (quotedField) {
						if (lastCh == quote) {
							field.append(ch);
							lastCh = 0;
						} else {
							lastCh = ch;
						}

						continue;
					} else {
						throw new InvalidRecordError(record.toString(), toString());
					}
				}

				lastCh = ch;

				if (quotedField || ch != '\r') {
					field.append(ch);
				}
			}

			if (ret != null && elementCount != null && elementCount.intValue() != ret.size()) {
				throw new InvalidRecordError(record.toString(), toString());
			}

			return ret;
		} catch (ProcessorError e) {
			throw e;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public String toString() {
		return "CSVSplitter(" + elementCount + ", " + separator + ", " + quote + ", " + skipHeader + ")";
	}
}
