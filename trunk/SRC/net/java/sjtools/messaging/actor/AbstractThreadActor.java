package net.java.sjtools.messaging.actor;

import net.java.sjtools.messaging.util.ReferenceUtil;

public abstract class AbstractThreadActor extends AbstractActor implements Runnable {
	public AbstractThreadActor(String actorName) {
		super(actorName);
		
		Thread thread = new Thread(this, actorName);
		thread.start();
	}
	
	public AbstractThreadActor() {
		this(ReferenceUtil.getActorReference());
	}
}
