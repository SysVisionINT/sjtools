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
package net.java.sjtools.cache;

public class CacheConfig {
	/**
	 * Se usado como <b>MaxCapacity</b> deixa de existir limite para o numero de entradas na cache
	 */
	public static final int NO_MAX_SIZE = Integer.MAX_VALUE;

	/**
	 * Se usado como <b>ExpireTime</b> os objectos existentes na cache nunca são removidos
	 */
	public static final long NEVER_EXPIRE = Long.MAX_VALUE;

	private int myInitialCapacity = 100;
	private int myMaxCapacity = 1000;
	private long myExpireTime = 3600000;

	/**
	 * Indica a capacidade (numero de entradas) inicial da cache
	 */
	public int getInitialCapacity() {
		return myInitialCapacity;
	}

	/**
	 * Permite indicar a capacidade (numero de entradas) inicial da cache
	 */
	public void setInitialCapacity(int initialCapacity) {
		myInitialCapacity = initialCapacity;

		if (myMaxCapacity < myInitialCapacity) {
			myMaxCapacity = myInitialCapacity;
		}
	}

	/**
	 * Indica a capacidade (numero de entradas) maxima da cache
	 */
	public int getMaxCapacity() {
		return myMaxCapacity;
	}

	/**
	 * Permite indicar a capacidade (numero de entradas) maxima da cache, quando é atingida
	 * a capacidade maxima a entrada que não é acedida a mais tempo é removida
	 */
	public void setMaxCapacity(int maxCapacity) {
		myMaxCapacity = maxCapacity;

		if (myMaxCapacity < myInitialCapacity) {
			myInitialCapacity = myMaxCapacity;
		}
	}

	/**
	 * Indica o intervalo de tempo a partir do qual um objecto guardado na cache passa a ser considerado como
	 * <i>DEATH</i> e deve ser removido
	 */
	public long getExpireTime() {
		return myExpireTime;
	}

	/**
	 * Permite indicar o intervalo de tempo a partir do qual um objecto guardado na cache passa a ser considerado como
	 * <i>DEATH</i> e deve ser removido
	 */
	public void setExpireTime(long expireTime) {
		myExpireTime = expireTime;
	}
}
