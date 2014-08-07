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

	private ProcessorError processorError = null;
	private List record = null;
	private boolean eof = false;

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
		if (eof) {
			return false;
		}

		try {
			processorError = null;

			// Lista para retorno do método nextRecord
			record = new ArrayList();

			StringBuffer buffer = new StringBuffer();
			StringBuffer field = new StringBuffer();

			int b = 0;
			char ch = 0;

			boolean quotedField = false;
			boolean skipedCR = false;

			short quoteCount = 0;

			while (true) {
				b = inputStream.read();

				if (b == -1) { //EOF
					eof = true;

					if (quotedField && quoteCount != 1) { // O último caracter não é uma aspa.
						processorError = new InvalidRecordError(buffer.toString(), toString());
						return true;
					}

					//Se for linha vazia, não adicionar e retornar false (não existem mais registos).
					if (field.length() == 0 && record.isEmpty()) {
						return false;
					}

					record.add(field.toString());
					break;
				} else {
					ch = (char) b;

					// Para saber a totalidade do registo que foi lido
					buffer.append(ch);

					if (quotedField) {
						if (ch == quote) {
							quoteCount++;

							if (quoteCount == 2) {
								field.append(quote);
								quoteCount = 0;

							}

							continue;
						}

						if (quoteCount == 1) {
							if (ch == '\r') {
								skipedCR = true;
								continue;
							}

							if (ch == separator) {
								// Fim do field
								record.add(field.toString());
								field.setLength(0);
								quoteCount = 0;
								quotedField = false;
								continue;
							} else if (ch == '\n') {
								// Fim do field e do record
								record.add(field.toString());
								break;
							} else {
								processorError = new InvalidRecordError(buffer.toString(), toString());
								return true;
							}
						}

						if (skipedCR) {
							skipedCR = false;
							field.append('\r');
						}

						field.append(ch);
					} else { // if (quotedField)
						if (ch == quote) {
							if (field.length() == 0) {
								quotedField = true;
								continue;
							} else {
								processorError = new InvalidRecordError(buffer.toString(), toString());
								return true;
							}
						}

						if (ch == '\r') {
							skipedCR = true;
							continue;
						}

						if (ch == separator) {
							// Fim do field
							record.add(field.toString());
							quoteCount = 0;
							field.setLength(0);
							continue;
						} else if (ch == '\n') {
							// Fim do field e do record
							record.add(field.toString());
							break;
						}

						if (skipedCR) {
							skipedCR = false;
							field.append('\r');
						}

						field.append(ch);
					}
				}
			}

			// Validação do número de elementos
			if (elementCount != null && elementCount.intValue() != record.size()) {
				processorError = new InvalidRecordError(buffer.toString(), toString());
			}

			return true;
		} catch (Exception e) {
			throw new ProcessorError(e);
		}
	}

	public List nextRecord() throws ProcessorError {
		// Caso tenha ocorrido algum erro no processamento dos campos, lançamos a excepção
		if (processorError != null) {
			throw processorError;
		}

		return record;
	}

	public String toString() {
		return "CSVSplitter(" + elementCount + ", " + separator + ", " + quote + ", " + skipHeader + ")";
	}

}
