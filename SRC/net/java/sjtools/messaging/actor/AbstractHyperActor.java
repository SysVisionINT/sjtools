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
package net.java.sjtools.messaging.actor;

import net.java.sjtools.messaging.actor.hyper.ProcessEvent;
import net.java.sjtools.messaging.actor.hyper.ProcessMessage;
import net.java.sjtools.messaging.actor.hyper.ProcessRequest;
import net.java.sjtools.messaging.actor.hyper.ProcessResponse;
import net.java.sjtools.messaging.message.Event;
import net.java.sjtools.messaging.message.Message;
import net.java.sjtools.messaging.message.Request;
import net.java.sjtools.messaging.message.Response;
import net.java.sjtools.pool.PoolConfig;
import net.java.sjtools.thread.SuperThreadFactory;
import net.java.sjtools.thread.executor.Executor;
import net.java.sjtools.thread.executor.impl.ThreadPoolExecutor;
import net.java.sjtools.thread.pool.ThreadPool;

public abstract class AbstractHyperActor extends AbstractActor {
	private Executor actorExecutor = null;
	
	public AbstractHyperActor(String actorName, ThreadPool threadPool) {
		super(actorName);

		actorExecutor = new ThreadPoolExecutor(threadPool);
	}
	
	public AbstractHyperActor(String actorName, int maxThreadPoolSize) {
		this(actorName, createThreadPool(actorName, maxThreadPoolSize));
	}
	
	protected void processMessage(Message message) {
		execute(new ProcessMessage(this, message));
	}

	protected void processResponse(Response response) {
		execute(new ProcessResponse(this, response));
	}

	protected void processRequest(Request request) {
		execute(new ProcessRequest(this, request));
	}

	protected void processEvent(Event event) {
		execute(new ProcessEvent(this, event));
	}
	
	private void execute(Runnable method) {
		actorExecutor.execute(method);
	}
	
	private static ThreadPool createThreadPool(String actorName, int maxThreadPoolSize) {
		SuperThreadFactory factory = new SuperThreadFactory(actorName, true, Thread.NORM_PRIORITY);
		PoolConfig poolConfig = ThreadPool.getDefaultConfig();
		poolConfig.setMaxSize(maxThreadPoolSize);
		
		return new ThreadPool(poolConfig, factory);
	}
}
