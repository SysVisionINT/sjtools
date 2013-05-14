package net.java.sjtools.messaging.error;

import net.java.sjtools.error.ApplicationError;


public class NoListenerError extends ApplicationError {
	private static final long serialVersionUID = -8033017333944449007L;

	public NoListenerError(String listenerName) {
		super("No listener '" + listenerName + "'");
	}

}
