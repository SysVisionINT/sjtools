package net.java.sjtools.frameworks.file.processor.formatters;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public interface Formatter {

	public String format(String value) throws FileUploadError;

}
