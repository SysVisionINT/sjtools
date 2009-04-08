package net.java.sjtools.frameworks.file.processor.model.error;

public class MandatoryElementError extends FileUploadError {

	private static final long serialVersionUID = 3529246356181912397L;

	private int record = 0;
	private int position = 0;

	public MandatoryElementError(int record, int position) {
		super("Missing mandatory element at record " + record + ", column " + position);

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
