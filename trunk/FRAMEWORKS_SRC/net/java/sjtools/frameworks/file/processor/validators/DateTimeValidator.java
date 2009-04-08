package net.java.sjtools.frameworks.file.processor.validators;

import net.java.sjtools.time.SuperDate;

public class DateTimeValidator implements Validator {

	private String format = null;

	public DateTimeValidator(String format) {
		this.format = format;
	}

	public boolean isValid(String value) {
		try {
			new SuperDate(value, format);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "DateTimeValidator(" + format + ")";
	}

}
