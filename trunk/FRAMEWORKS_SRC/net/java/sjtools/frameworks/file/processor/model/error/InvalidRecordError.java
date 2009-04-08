package net.java.sjtools.frameworks.file.processor.model.error;

public class InvalidRecordError extends FileUploadError {

	private static final long serialVersionUID = -4577030631817632291L;

	private String record = null;
	private String splitter = null;

	public InvalidRecordError(String record, String splitter) {
		super("Invalid record for splitter " + splitter + ": " + record);

		this.record = record;
		this.splitter = splitter;
	}

	public String getRecord() {
		return record;
	}

	public String getSplitter() {
		return splitter;
	}

}
