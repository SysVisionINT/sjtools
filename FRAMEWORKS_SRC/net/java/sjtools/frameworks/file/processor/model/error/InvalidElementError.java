package net.java.sjtools.frameworks.file.processor.model.error;

public class InvalidElementError extends FileUploadError {

	private static final long serialVersionUID = -2036956442665785032L;

	private int record = 0;
	private int position = 0;
	private String validator = null;
	private String value = null;

	public InvalidElementError(int record, int position, String validator, String value) {
		super("Invalid element at record " + record + ", column " + position + ". Validator: " + validator + ". Value:" + value);

		this.record = record;
		this.position = position;
		this.validator = validator;
		this.value = value;
	}

	public int getRecord() {
		return record;
	}

	public int getPosition() {
		return position;
	}

	public String getValidator() {
		return validator;
	}

	public String getValue() {
		return value;
	}

}
