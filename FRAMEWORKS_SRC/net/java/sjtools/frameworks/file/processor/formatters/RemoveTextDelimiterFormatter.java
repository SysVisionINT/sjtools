package net.java.sjtools.frameworks.file.processor.formatters;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public class RemoveTextDelimiterFormatter implements Formatter {

	private String delimiter = null;

	public RemoveTextDelimiterFormatter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String format(String value) throws FileUploadError {
		try {
			if (value.startsWith(delimiter) && value.endsWith(delimiter)) {
				value = value.substring(delimiter.length(), value.length() - delimiter.length());
			}

			return value;
		} catch (Exception e) {
			throw new FileUploadError("RemoveTextDelimiterFormatter: Error while removing delimiter (" + delimiter + ") from value (" + value + ")");
		}
	}

}
