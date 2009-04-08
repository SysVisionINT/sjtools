package net.java.sjtools.frameworks.file.processor.validators;

public class SmallerThanValidator implements Validator {

	private String value = null;

	public SmallerThanValidator(String value) {
		this.value = value;
	}

	public boolean isValid(String value) {
		return value.compareTo(this.value) < 0;
	}

	public String toString() {
		return "SmallerThanValidator(" + value + ")";
	}

}
