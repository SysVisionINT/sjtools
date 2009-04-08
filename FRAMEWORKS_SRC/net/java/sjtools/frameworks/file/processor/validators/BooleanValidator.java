package net.java.sjtools.frameworks.file.processor.validators;

public class BooleanValidator implements Validator {

	public boolean isValid(String value) {
		try {
			new Boolean(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "BooleanValidator";
	}

}
