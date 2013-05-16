package net.java.sjtools.messaging.router;

import net.java.sjtools.messaging.Endpoint;
import net.java.sjtools.messaging.message.Message;


public interface MessageRouter {
	public void route(Endpoint endpoint, Message message);
}
