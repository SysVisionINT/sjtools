package net.java.sjtools.frameworks.file.processor.validators;

public class LongValidator implements Validator {

	public boolean isValid(String value) {
		try {
			new Long(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "LongValidator";
	}

}
