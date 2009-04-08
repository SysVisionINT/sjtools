package net.java.sjtools.frameworks.file.processor.validators;

import java.util.regex.Pattern;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public class RegexpValidator implements Validator {

	private Pattern pattern = null;

	public RegexpValidator(String regexp) throws FileUploadError {
		try {
			pattern = Pattern.compile(regexp);
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public boolean isValid(String value) {
		return pattern.matcher(value).matches();
	}

	public String toString() {
		return "RegexpValidator(" + pattern.pattern() + ")";
	}

}
