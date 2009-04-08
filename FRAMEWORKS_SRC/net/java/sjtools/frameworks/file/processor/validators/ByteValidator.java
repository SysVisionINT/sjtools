package net.java.sjtools.frameworks.file.processor.validators;

public class ByteValidator implements Validator {

	public boolean isValid(String value) {
		try {
			new Byte(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "ByteValidator";
	}

}
