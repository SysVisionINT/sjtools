package net.java.sjtools.frameworks.file.processor.validators;

public class CharValidator implements Validator {

	public boolean isValid(String value) {
		try {
			return value.length() == 1;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "CharValidator";
	}

}
