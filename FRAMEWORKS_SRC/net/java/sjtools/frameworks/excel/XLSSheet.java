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
package net.java.sjtools.frameworks.excel;

import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import net.java.sjtools.frameworks.excel.error.XLSException;
import net.java.sjtools.frameworks.excel.format.XLSDateFormat;

public class XLSSheet {
	private static WritableCellFormat integerFormat = null;
	private static WritableCellFormat moneyFormat = null;
	private static WritableCellFormat percentageFormat = null;
	private static WritableCellFormat floatFormat = null;
	private static WritableCellFormat dateFormat = null;

	private WritableSheet sheet = null;
	private int row = 0;
	private int column = 0;

	protected XLSSheet(WritableSheet sheet) {
		this.sheet = sheet;
	}

	public void newRow() {
		row++;
		column = 0;
	}

	public void addFreeCell() {
		column++;
	}

	public void gotoCell(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public void addCell(String value) throws XLSException {
		try {
			sheet.addCell(new Label(column, row, value));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}

	public void addCell(Object value) throws XLSException {
		if (value == null) {
			addFreeCell();
		} else if (value instanceof String) {
			addCell((String) value);
		} else if (value instanceof java.lang.Number) {
			java.lang.Number x = (java.lang.Number) value;

			if (value instanceof Float || value instanceof Double) {
				addCell(x.doubleValue());
			} else {
				addCell(x.longValue());
			}
		} else if (value instanceof java.util.Date) {
			addCell((java.util.Date) value);
		} else {
			addCell(value.toString());
		}
	}

	public void addCell(long value) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, integerFormat));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}

	public void addCell(double value) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, floatFormat));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}
	
	public void addMoneyCell(double value) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, moneyFormat));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}
	
	public void addPercentageCell(double value) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, percentageFormat));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}

	public void addCell(java.util.Date value) throws XLSException {
		addCell(value, dateFormat);
	}
	
	public void addCell(java.util.Date value, XLSDateFormat format) throws XLSException {
		addCell(value, (WritableCellFormat)format.getNativeFormat());
	}
	
	private void addCell(java.util.Date value, WritableCellFormat format) throws XLSException {
		if (value == null) {
			addFreeCell();
			return;
		}
		
		try {
			sheet.addCell(new DateTime(column, row, value, format));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}

	static {
		integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
		moneyFormat = new WritableCellFormat(NumberFormats.ACCOUNTING_FLOAT);
		percentageFormat = new WritableCellFormat(NumberFormats.PERCENT_FLOAT);
		floatFormat = new WritableCellFormat(NumberFormats.FLOAT);
		dateFormat = new WritableCellFormat(DateFormats.DEFAULT);
	}
}
