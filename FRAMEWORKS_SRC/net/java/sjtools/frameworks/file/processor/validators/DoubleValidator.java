package net.java.sjtools.frameworks.file.processor.validators;

public class DoubleValidator implements Validator {

	public boolean isValid(String value) {
		try {
			new Double(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "DoubleValidator";
	}

}
