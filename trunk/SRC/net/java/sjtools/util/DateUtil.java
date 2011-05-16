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
package net.java.sjtools.util;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.java.sjtools.time.SuperDate;

/**
 * Classe com utilit&aacute;rios para tratamento / valida&ccedil;&atilde;o de String com datas
 */
public class DateUtil {

	/**
	 * Verifica se uma String com uma data respeita um determinado formato
	 * @param inputDate A String com uma data a ser validada
	 * @param format String com o formato a ser respeitado
	 * @return <strong>true</strong> se <I>inputDate</I> respeitar </I>format</I>
	 */
	public static boolean isValidDate(String inputDate, String format) {
		return isValidDate(inputDate, format, Locale.getDefault(), TimeZone.getDefault());
	}

	public static boolean isValidDate(String inputDate, String format, Locale locale, TimeZone zone) {
		try {
			new SuperDate(inputDate, format, locale, zone);

			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * Retorna o n&uacute;mero de dias entre duas datas
	 * @param firstDate Data inicial
	 * @param lastDate Data final
	 * @return N&uacute;mero de dias
	 */
	public static int daysBetween(Date firstDate, Date lastDate) {
		if (firstDate.equals(lastDate)) {
			return 0;
		}

		SuperDate small = new SuperDate(firstDate.before(lastDate) ? firstDate : lastDate);
		SuperDate last = new SuperDate(firstDate.after(lastDate) ? firstDate : lastDate);

		int count = 0;
		small.addDays(1);

		while (small.before(last) || small.equals(last)) {
			count++;
			small.addDays(1);
		}

		return count;
	}

	/**
	 * Retorna o n&uacute;mero de horas entre duas datas
	 * @param firstDate Data inicial
	 * @param lastDate Data final
	 * @return N&uacute;mero de horas
	 */
	public static int hoursBetween(Date firstDate, Date lastDate) {
		if (firstDate.equals(lastDate)) {
			return 0;
		}

		SuperDate small = new SuperDate(firstDate.before(lastDate) ? firstDate : lastDate);
		SuperDate last = new SuperDate(firstDate.after(lastDate) ? firstDate : lastDate);

		int count = 0;
		small.addHours(1);

		while (small.before(last) || small.equals(last)) {
			count++;
			small.addHours(1);
		}

		return count;
	}

	/**
	 * Retorna o n&uacute;mero de minutos entre duas datas
	 * @param firstDate Data inicial
	 * @param lastDate Data final
	 * @return N&uacute;mero de minutos
	 */
	public static int minutesBetween(Date firstDate, Date lastDate) {
		if (firstDate.equals(lastDate)) {
			return 0;
		}

		SuperDate small = new SuperDate(firstDate.before(lastDate) ? firstDate : lastDate);
		SuperDate last = new SuperDate(firstDate.after(lastDate) ? firstDate : lastDate);

		int count = 0;
		small.addMinutes(1);

		while (small.before(last) || small.equals(last)) {
			count++;
			small.addMinutes(1);
		}

		return count;
	}

	/**
	 * Retorna o n&uacute;mero de segundos entre duas datas
	 * @param firstDate Data inicial
	 * @param lastDate Data final
	 * @return N&uacute;mero de segundos
	 */
	public static int secondsBetween(Date firstDate, Date lastDate) {
		if (firstDate.equals(lastDate)) {
			return 0;
		}

		SuperDate small = new SuperDate(firstDate.before(lastDate) ? firstDate : lastDate);
		SuperDate last = new SuperDate(firstDate.after(lastDate) ? firstDate : lastDate);

		int count = 0;
		small.addSeconds(1);

		while (small.before(last) || small.equals(last)) {
			count++;
			small.addSeconds(1);
		}

		return count;
	}

	/**
	 * Retorna o n&uacute;mero de milissegundos entre duas datas
	 * @param firstDate Data inicial
	 * @param lastDate Data final
	 * @return N&uacute;mero de milissegundos
	 */
	public static long millisBetween(Date firstDate, Date lastDate) {
		long t1 = firstDate.getTime();
		long t2 = lastDate.getTime();
		long dif = t1 - t2;

		if (dif < 0) {
			dif = dif * (-1);
		}

		return dif;
	}
}
