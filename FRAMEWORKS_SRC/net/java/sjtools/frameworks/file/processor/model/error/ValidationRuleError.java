package net.java.sjtools.frameworks.file.processor.model.error;

public class ValidationRuleError extends FileUploadError {

	private static final long serialVersionUID = 1395899596964611418L;

	private String validationRule = null;

	public ValidationRuleError(String validationRule) {
		super("Validation rule not found: " + validationRule);
		this.validationRule = validationRule;
	}

	public String getValidationRule() {
		return validationRule;
	}

}
