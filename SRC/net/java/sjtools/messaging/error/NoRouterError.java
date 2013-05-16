package net.java.sjtools.messaging.error;

import net.java.sjtools.error.ApplicationError;


public class NoRouterError extends ApplicationError {
	private static final long serialVersionUID = -8033017333944449007L;

	public NoRouterError(String listenerName) {
		super("No router for address '" + listenerName + "'");
	}

}
