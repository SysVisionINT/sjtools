package net.java.sjtools.frameworks.file.processor.model.error;

public class ObjectCreationError extends FileUploadError {

	private static final long serialVersionUID = 4191992987294342695L;

	private String objClass = null;
	private String objValue = null;
	private String objFormat = null;

	public ObjectCreationError(String objClass, String objValue, String objFormat) {
		super("Unable to create an object for class " + objClass + ": value=" + objValue + ", format=" + objFormat + ". Additional info: Maybe a (String) or (String, String) constructor is missing.");

		this.objClass = objClass;
		this.objValue = objValue;
		this.objFormat = objFormat;
	}

	public String getObjClass() {
		return objClass;
	}

	public String getObjValue() {
		return objValue;
	}

	public String getObjFormat() {
		return objFormat;
	}

}
