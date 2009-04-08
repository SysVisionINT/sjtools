package net.java.sjtools.frameworks.file.processor.validators;

import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.util.TextUtil;

public class InListValidator implements Validator {

	private List validValues = new ArrayList();

	public void add(String value) {
		validValues.add(value);
	}

	public boolean isValid(String value) {
		return validValues.contains(value);
	}

	public String toString() {
		return "InListValidator([" + TextUtil.toString(validValues) + "])";
	}

}
