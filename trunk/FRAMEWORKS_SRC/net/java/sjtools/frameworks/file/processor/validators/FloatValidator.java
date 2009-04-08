package net.java.sjtools.frameworks.file.processor.validators;

public class FloatValidator implements Validator {

	public boolean isValid(String value) {
		try {
			new Float(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "FloatValidator";
	}

}
