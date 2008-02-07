/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2008 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
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
package net.java.sjtools.io.filter;

import java.io.File;
import java.io.FileFilter;
import java.sql.Timestamp;

import net.java.sjtools.error.UnexpectedError;
import net.java.sjtools.io.error.InvalidFilterException;
import net.java.sjtools.io.filter.model.MinMax;
import net.java.sjtools.time.SuperDate;

public class ModifiedFilter implements FileFilter {

	// Operations
	public static final int EARLIER_EXCLUSIVE = 1;
	public static final int EARLIER_INCLUSIVE = 2;
	public static final int EQUAL = 3;
	public static final int OLDER_INCLUSIVE = 4;
	public static final int OLDER_EXCLUSIVE = 5;
	public static final int BETWEEN_INCLUSIVE = 6;
	public static final int BETWEEN_EXCLUSIVE = 7;

	// Precision
	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	public static final int HOUR = 4;
	public static final int MINUTE = 5;
	public static final int SECOND = 6;

	private int operation = 0;

	private long startMin = -1;
	private long startMax = -1;
	private long endMin = -1;
	private long endMax = -1;

	public ModifiedFilter(int operation, Timestamp start, Timestamp end, int precision) throws InvalidFilterException, UnexpectedError {
		try {
			if (!isValidOperation(operation)) {
				throw new InvalidFilterException("Invalid operation");
			}

			if (!isValidTimestampsForOperation(operation, start, end)) {
				throw new InvalidFilterException("Invalid timestamps for operation");
			}

			if (!isValidPrecision(precision)) {
				throw new InvalidFilterException("Invalid precision");
			}

			this.operation = operation;

			MinMax minMax = getMinMax(precision, new SuperDate(start));

			this.startMin = minMax.getMin();
			this.startMax = minMax.getMax();

			if (end != null) {
				minMax = getMinMax(precision, new SuperDate(end));

				this.endMin = minMax.getMin();
				this.endMax = minMax.getMax();
			}
		} catch (InvalidFilterException e) {
			throw e;
		} catch (Exception e) {
			throw new UnexpectedError(e);
		}
	}

	public boolean accept(File pathname) {
		switch (operation) {
			case EARLIER_EXCLUSIVE:
				return pathname.lastModified() < startMin;
			case EARLIER_INCLUSIVE:
				return pathname.lastModified() < startMax;
			case EQUAL:
				return pathname.lastModified() >= startMin && pathname.lastModified() < startMax;
			case OLDER_INCLUSIVE:
				return pathname.lastModified() >= startMin;
			case OLDER_EXCLUSIVE:
				return pathname.lastModified() >= startMax;
			case BETWEEN_INCLUSIVE:
				return pathname.lastModified() >= startMin && pathname.lastModified() < endMax;
			case BETWEEN_EXCLUSIVE:
				return pathname.lastModified() >= startMax && pathname.lastModified() < endMin;
			default:
				return false;
		}
	}

	private boolean isValidTimestampsForOperation(int operation, Timestamp start, Timestamp end) {
		switch (operation) {
			case BETWEEN_EXCLUSIVE:
			case BETWEEN_INCLUSIVE:
				return start != null && end != null;
			default:
				return start != null;
		}
	}

	private boolean isValidOperation(int operation) {
		return operation >= EARLIER_EXCLUSIVE && operation <= BETWEEN_EXCLUSIVE;
	}

	private boolean isValidPrecision(int precision) {
		return precision >= YEAR && precision <= SECOND;
	}

	private MinMax getMinMax(int precision, SuperDate date) {
		MinMax ret = new MinMax();

		SuperDate work = new SuperDate(date.getTime());

		switch (precision) {
			case YEAR:
				work.setMonth(1);
				work.setDay(1);
				work.setHour(0);
				work.setMinute(0);
				work.setSecond(0);
				work.setMilliseconds(0);
				ret.setMin(work.getTime());

				work.addYears(1);
				ret.setMax(work.getTime());
				break;
			case MONTH:
				work.setDay(1);
				work.setHour(0);
				work.setMinute(0);
				work.setSecond(0);
				work.setMilliseconds(0);
				ret.setMin(work.getTime());

				work.addMonths(1);
				ret.setMax(work.getTime());
				break;
			case DAY:
				work.setHour(0);
				work.setMinute(0);
				work.setSecond(0);
				work.setMilliseconds(0);
				ret.setMin(work.getTime());

				work.addDays(1);
				ret.setMax(work.getTime());
				break;
			case HOUR:
				work.setMinute(0);
				work.setSecond(0);
				work.setMilliseconds(0);
				ret.setMin(work.getTime());

				work.addHours(1);
				ret.setMax(work.getTime());
				break;
			case MINUTE:
				work.setSecond(0);
				work.setMilliseconds(0);
				ret.setMin(work.getTime());

				work.addMinutes(1);
				ret.setMax(work.getTime());
				break;
			case SECOND:
				work.setMilliseconds(0);
				ret.setMin(work.getTime());

				work.addSeconds(1);
				ret.setMax(work.getTime());
				break;
		}

		return ret;
	}

}
