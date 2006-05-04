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
package net.java.sjtools.logging;

import java.io.Serializable;

public interface Log extends Serializable {
	public String getLoggerName();
	
    public boolean isDebugEnabled();

    public boolean isErrorEnabled();

    public boolean isInfoEnabled();
    
    public boolean isFatalEnabled();

    public boolean isWarnEnabled();    

    public void debug(Object message);

    public void debug(Object message, Throwable t);

    public void info(Object message);

    public void info(Object message, Throwable t);

    public void warn(Object message);

    public void warn(Object message, Throwable t);
    
    public void error(Object message);

    public void error(Object message, Throwable t);
    
    public void fatal(Object message);

    public void fatal(Object message, Throwable t);
}
