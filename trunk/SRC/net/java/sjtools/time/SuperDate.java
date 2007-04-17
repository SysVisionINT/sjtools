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
package net.java.sjtools.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SuperDate extends Timestamp {
	private static final long serialVersionUID = 8939176077976064078L;

	public SuperDate() {
		super(System.currentTimeMillis());
	}
	
	public SuperDate(long time) {
		super(time);
	}

	public SuperDate(Date dt) {
		this(dt.getTime());
	}

	public SuperDate(String date, String format) throws ParseException {
		this();
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dt = sdf.parse(date);
		
		this.setTime(dt.getTime());
	}

	public SuperDate(Calendar calendar) {
		this(calendar.getTime());
	}
	
	public Timestamp getTimestamp() {
		return new Timestamp(getTime());
	}

	public Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this);
		
		return calendar;
	}

	public String getFormatedDate(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(this);
	}

	public void add(int field, int amount) {
		Calendar calendar = getCalendar();
		calendar.add(field, amount);
		this.setTime(calendar.getTime().getTime());
	}

	public void addYears(int value) {
		add(Calendar.YEAR, value);
	}	
	
	public void addMonths(int value) {
		add(Calendar.MONTH, value);
	}		
	
	public void addDays(int value) {
		add(Calendar.DAY_OF_MONTH, value);
	}		
	
	public void addHours(int value) {
		add(Calendar.HOUR_OF_DAY, value);
	}
	
	public void addMinutes(int value) {
		add(Calendar.MINUTE, value);
	}	
	
	public void addSeconds(int value) {
		add(Calendar.SECOND, value);
	}	
	
	public void addMilliseconds(int value) {
		add(Calendar.MILLISECOND, value);
	}	
	
	public void set(int field, int value) {
		Calendar calendar = getCalendar();
		calendar.set(field, value);
		this.setTime(calendar.getTime().getTime());
	}
	
	public void setYear(int value) {
		set(Calendar.YEAR, value);
	}	
	
	public void setMonth(int value) {
		set(Calendar.MONTH, value - 1);
	}		
	
	public void setDay(int value) {
		set(Calendar.DAY_OF_MONTH, value);
	}		
	
	public void setHour(int value) {
		set(Calendar.HOUR_OF_DAY, value);
	}
	
	public void setMinute(int value) {
		set(Calendar.MINUTE, value);
	}	
	
	public void setSecond(int value) {
		set(Calendar.SECOND, value);
	}	
	
	public void setMilliseconds(int value) {
		set(Calendar.MILLISECOND, value);
	}
	
	public int get(int field) {
		return getCalendar().get(field);
	}
	
	public int getYear() {
		return get(Calendar.YEAR);
	}	
	
	public int getMonth() {
		return get(Calendar.MONTH) + 1;
	}		
	
	public int getDay() {
		return get(Calendar.DAY_OF_MONTH);
	}		
	
	public int getHour() {
		return get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMinute() {
		return get(Calendar.MINUTE);
	}	
	
	public int getSecond() {
		return get(Calendar.SECOND);
	}	
	
	public int getMilliseconds() {
		return get(Calendar.MILLISECOND);
	}	
}
