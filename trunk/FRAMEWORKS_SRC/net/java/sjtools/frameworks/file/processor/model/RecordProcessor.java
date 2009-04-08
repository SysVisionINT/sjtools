package net.java.sjtools.frameworks.file.processor.model;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public interface RecordProcessor {

	public void processReturnObject(Object returnObject) throws Exception;

	public void processValidationError(FileUploadError error) throws Exception;

}
