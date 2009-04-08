package net.java.sjtools.frameworks.file.processor.model.error;

public class ColumnNotFoundError extends FileUploadError {

	private static final long serialVersionUID = -4559463440047430110L;

	private int record = 0;
	private int position = 0;

	public ColumnNotFoundError(int record, int position) {
		super("Column number " + position + " not found in record number " + record);

		this.record = record;
		this.position = position;
	}

	public int getRecord() {
		return record;
	}

	public int getPosition() {
		return position;
	}

}
