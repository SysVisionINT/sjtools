package net.java.sjtools.ioc.error;

public class ObjectNotFound extends ObjectRegestryError {
	private static final long serialVersionUID = 8265907419766428718L;

	public ObjectNotFound(String objectName) {
		super(objectName);
	}
}
