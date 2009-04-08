package net.java.sjtools.frameworks.file.processor.model.error;

public class FileUploadError extends Exception {

	private static final long serialVersionUID = 1996067631443775358L;

	public FileUploadError(String errorMsg) {
		super(errorMsg);
	}

	public FileUploadError(String errorMsg, Throwable throwable) {
		super(errorMsg, throwable);
	}

	public FileUploadError(Throwable throwable) {
		super(throwable);
	}

}
