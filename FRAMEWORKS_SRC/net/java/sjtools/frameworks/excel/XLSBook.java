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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.java.sjtools.frameworks.excel.error.XLSException;

public class XLSBook {
	private WritableWorkbook workbook = null;
	
	private XLSBook (OutputStream os, String encoding, Locale locale) throws XLSException {
		try {
			WorkbookSettings settings = new WorkbookSettings();
			
			if (encoding != null) {
				settings.setEncoding(encoding);
			}
			
			if (locale == null) {
				settings.setLocale(locale);
			}
			
			workbook = Workbook.createWorkbook(os, settings);
		} catch (IOException e) {
			throw new XLSException("Error creating workbook", e);
		}
	}
	
	public static XLSBook createBook(File file, String encoding, Locale locale) throws XLSException {
		try {
			return new XLSBook(new FileOutputStream(file), encoding, locale);
		} catch (FileNotFoundException e) {
			throw new XLSException("File not found " + file, e);
		}
	}
	
	public static XLSBook createBook(File file) throws XLSException {
		return createBook(file, null, null);
	}	
	
	public static XLSBook createBook(OutputStream os, String encoding, Locale locale) throws XLSException {
		return new XLSBook(os, encoding, locale);
	}
	
	public static XLSBook createBook(OutputStream os) throws XLSException {
		return createBook(os, null, null);
	}
	
	public XLSSheet createSheet() {
		return createSheet("Sheet".concat(String.valueOf(workbook.getNumberOfSheets() + 1)));
	}
	
	public XLSSheet createSheet(String sheetName) {
		WritableSheet sheet = workbook.createSheet(sheetName, workbook.getNumberOfSheets());
		
		return new XLSSheet(sheet);
	}
	
	public void close() throws XLSException {
		try {
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			throw new XLSException("Error closing workbook", e);
		}
	}
}
