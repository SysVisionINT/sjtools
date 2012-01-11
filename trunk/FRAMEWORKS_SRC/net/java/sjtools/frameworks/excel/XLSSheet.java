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

import jxl.CellView;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import net.java.sjtools.frameworks.excel.error.XLSException;
import net.java.sjtools.frameworks.excel.format.XLSDateFormat;
import net.java.sjtools.frameworks.excel.format.XLSFormat;
import net.java.sjtools.frameworks.excel.format.base.DateFormat;
import net.java.sjtools.frameworks.excel.format.base.FloatFormat;
import net.java.sjtools.frameworks.excel.format.base.IntegerFormat;
import net.java.sjtools.frameworks.excel.format.base.MoneyFormat;
import net.java.sjtools.frameworks.excel.format.base.PercentageFormat;

public class XLSSheet {
	private static XLSFormat integerFormat = null;
	private static XLSFormat moneyFormat = null;
	private static XLSFormat percentageFormat = null;
	private static XLSFormat floatFormat = null;
	private static XLSFormat dateFormat = null;

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
	
	public void setColumnSize(int column, int size) {
		CellView view = sheet.getColumnView(column);
		view.setSize(size);
	    sheet.setColumnView(column, view);
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
	
	public void addCell(Object value, XLSFormat format) throws XLSException {
		if (value == null) {
			addFreeCell();
		} else if (value instanceof String) {
			addCell((String) value, format);
		} else if (value instanceof java.lang.Number) {
			java.lang.Number x = (java.lang.Number) value;

			if (value instanceof Float || value instanceof Double) {
				addCell(x.doubleValue(), format);
			} else {
				addCell(x.longValue(), format);
			}
		} else if (value instanceof java.util.Date) {
			addCell((java.util.Date) value, format);
		} else {
			addCell(value.toString(), format);
		}
	}	
	
	public void addCell(String value) throws XLSException {
		addCell(value, null);
	}
	
	public void addCell(String value, XLSFormat format) throws XLSException {
		try {
			if (format == null) {
				sheet.addCell(new Label(column, row, value));
			} else {
				sheet.addCell(new Label(column, row, value, (WritableCellFormat)format.getNativeFormat()));
			}
			
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}	

	public void addCell(long value) throws XLSException {
		addCell(value, integerFormat);
	}
	
	public void addCell(long value, XLSFormat format) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, (WritableCellFormat)format.getNativeFormat()));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}

	public void addCell(double value) throws XLSException {
		addCell(value, floatFormat);
	}
	
	public void addCell(double value, XLSFormat format) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, (WritableCellFormat)format.getNativeFormat()));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}
	
	public void addMoneyCell(double value) throws XLSException {
		addMoneyCell(value, moneyFormat);
	}
	
	public void addMoneyCell(double value, XLSFormat format) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, (WritableCellFormat)format.getNativeFormat()));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}
	
	public void addPercentageCell(double value) throws XLSException {
		addPercentageCell(value, percentageFormat);
	}
	
	public void addPercentageCell(double value, XLSFormat format) throws XLSException {
		try {
			sheet.addCell(new Number(column, row, value, (WritableCellFormat)format.getNativeFormat()));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}

	public void addCell(java.util.Date value) throws XLSException {
		addCell(value, dateFormat);
	}
	
	public void addCell(java.util.Date value, XLSDateFormat format) throws XLSException {
		addCell(value, format);
	}
	
	public void addCell(java.util.Date value, XLSFormat format) throws XLSException {
		if (value == null) {
			addFreeCell();
			return;
		}
		
		try {
			sheet.addCell(new DateTime(column, row, value, (WritableCellFormat)format.getNativeFormat()));
			column++;
		} catch (Exception e) {
			throw new XLSException("Error adding a value to cell: " + value, e);
		}
	}

	static {
		integerFormat = new IntegerFormat();
		moneyFormat = new MoneyFormat();
		percentageFormat = new PercentageFormat();
		floatFormat = new FloatFormat();
		dateFormat = new DateFormat();
	}
}
