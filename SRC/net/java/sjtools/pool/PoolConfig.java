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
package net.java.sjtools.pool;

public class PoolConfig {
	/**
	 * Quando usado como MaxSize indica que não existe numero maximo de objectos na pool
	 */
	public static final int NO_MAX_SIZE = Integer.MAX_VALUE;

	/**
	 * Quando usado como ExpireTime indica que os objectos existentes na pool nunca são removidos
	 */
	public static final long NEVER_EXPIRE = Long.MAX_VALUE;

	/**
	 * Quando usado como TimeOut indica que os objectos fornecidos e nunca devolvidos nunca são considerados 
	 * recuperaveis
	 */
	public static final long NEVER_TIMEOUT = Long.MAX_VALUE;

	/**
	 * Indica que se deve esperar por um objecto até que esta seja obtida
	 */
	public static final long WAIT_FOREVER = Long.MAX_VALUE;

	private int minimalSize = 0;
	private int maxSize = NO_MAX_SIZE;
	private long timeOut = 60000;
	private long expireTime = 120000;
	private long validationTime = 180000;
	private boolean validateOnBorrow = false;
	private boolean validateOnInterval = false;
	private boolean validateOnReturn = false;	
	private long waitTime = 1000;

	/**
	 * Returns the expireTime.
	 * @return long
	 */
	public long getExpireTime() {
		return expireTime;
	}

	/**
	 * Returns the initialSize.
	 * @return int
	 */
	public int getMinimalSize() {
		return minimalSize;
	}

	/**
	 * Returns the maxSize.
	 * @return int
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * Returns the timeOut.
	 * @return long
	 */
	public long getTimeOut() {
		return timeOut;
	}

	/**
	 * Sets the expireTime.
	 * @param expireTime The expireTime to set
	 */
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * Sets the minimalSize.
	 * @param initialSize The initialSize to set
	 */
	public void setMinimalSize(int minimalSize) {
		this.minimalSize = minimalSize;
	}

	/**
	 * Sets the maxSize.
	 * @param maxSize The maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * Sets the timeOut.
	 * @param timeOut The timeOut to set
	 */
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * Returns the validationTime.
	 * @return int
	 */
	public long getValidationTime() {
		return validationTime;
	}

	/**
	 * Returns the waitTime.
	 * @return int
	 */
	public long getWaitTime() {
		return waitTime;
	}

	/**
	 * Sets the validationTime.
	 * @param validationTime The validationTime to set
	 */
	public void setValidationTime(long validationTime) {
		this.validationTime = validationTime;
	}

	/**
	 * Sets the waitTime.
	 * @param waitTime The waitTime to set
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public boolean isValidateOnBorrow() {
		return validateOnBorrow;
	}

	public void setValidateOnBorrow(boolean validateOnBorrow) {
		this.validateOnBorrow = validateOnBorrow;
	}

	public boolean isValidateOnInterval() {
		return validateOnInterval;
	}

	public void setValidateOnInterval(boolean validateOnInterval) {
		this.validateOnInterval = validateOnInterval;
	}

	public boolean isValidateOnReturn() {
		return validateOnReturn;
	}

	public void setValidateOnReturn(boolean validateOnReturn) {
		this.validateOnReturn = validateOnReturn;
	}

}
