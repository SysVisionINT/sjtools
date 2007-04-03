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

import net.java.sjtools.time.SuperDate;
import net.java.sjtools.time.TimeConst;

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
		try {
			SuperDate date = new SuperDate(inputDate, format);

			if (inputDate.equals(date.getFormatedDate(format))) {
				return true;
			} else {
				return false;
			}
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
		return (int) (millisBetween(firstDate, lastDate) / TimeConst.DAY);
	}
	
    /**
     * Retorna o n&uacute;mero de horas entre duas datas
     * @param firstDate Data inicial
     * @param lastDate Data final
     * @return N&uacute;mero de horas
     */
	public static int hoursBetween(Date firstDate, Date lastDate) {
		return (int) (millisBetween(firstDate, lastDate) / TimeConst.HOUR);
	}	
	
    /**
     * Retorna o n&uacute;mero de minutos entre duas datas
     * @param firstDate Data inicial
     * @param lastDate Data final
     * @return N&uacute;mero de minutos
     */
	public static int minutesBetween(Date firstDate, Date lastDate) {
		return (int) (millisBetween(firstDate, lastDate) / TimeConst.MINUTE);
	}	
	
    /**
     * Retorna o n&uacute;mero de segundos entre duas datas
     * @param firstDate Data inicial
     * @param lastDate Data final
     * @return N&uacute;mero de segundos
     */
	public static int secondsBetween(Date firstDate, Date lastDate) {
		return (int) (millisBetween(firstDate, lastDate) / TimeConst.SECOND);
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
