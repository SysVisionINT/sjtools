package net.java.sjtools.frameworks.file.processor.validators;

public class ShortValidator implements Validator {

	public boolean isValid(String value) {
		try {
			new Short(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "ShortValidator";
	}

}
