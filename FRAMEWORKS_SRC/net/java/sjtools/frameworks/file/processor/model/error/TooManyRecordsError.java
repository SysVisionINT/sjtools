package net.java.sjtools.frameworks.file.processor.model.error;

public class TooManyRecordsError extends FileUploadError {

	private static final long serialVersionUID = -6067731522060201932L;

	private int maxRecords = 0;

	public TooManyRecordsError(int max) {
		super("Record count exceeded " + max + " records.");
		this.maxRecords = max;
	}

	public int getMaxRecords() {
		return maxRecords;
	}

}
