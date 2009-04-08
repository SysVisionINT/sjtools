package net.java.sjtools.frameworks.file.processor.validators;

public class BiggerThanValidator implements Validator {

	private String value = null;

	public BiggerThanValidator(String value) {
		this.value = value;
	}

	public boolean isValid(String value) {
		return value.compareTo(this.value) > 0;
	}

	public String toString() {
		return "BiggerThanValidator(" + value + ")";
	}

}
