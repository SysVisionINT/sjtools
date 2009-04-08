package net.java.sjtools.frameworks.file.processor.model.error;

public class TooFewRecordsError extends FileUploadError {

	private static final long serialVersionUID = 2296942124531945792L;

	private int minRecords = 0;

	public TooFewRecordsError(int min) {
		super("Record count didn't reach " + min + " record.");
		this.minRecords = min;
	}

	public int getMinRecords() {
		return minRecords;
	}

}
