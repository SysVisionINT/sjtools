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

/**
 * Esta classe disponibiliza metodos para parar a execução do thread corrente
 */
public class Sleep {
	/**
	 * Faz um sleep por x milésimos de segundo
	 * 
	 * @param value Numero de milésimos de segundo
	 * @return True se o sleep foi interrompido antes de ter decorrido o tempo especificado
	 */
	public static boolean milliSeconds(long value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException e) {
			return true;
		}

		return false;
	}

	/**
	 * Faz um sleep por x segundos
	 * 
	 * @param value Numero de segundos
	 * @return True se o sleep foi interrompido antes de ter decorrido o tempo especificado
	 */
	public static boolean seconds(int value) {
		return milliSeconds(value * TimeConst.SECOND);
	}
}
