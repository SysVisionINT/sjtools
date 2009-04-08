package net.java.sjtools.frameworks.file.processor.validators;

public class IntegerValidator implements Validator {

	public boolean isValid(String value) {
		try {
			new Integer(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "IntegerValidator";
	}

}
