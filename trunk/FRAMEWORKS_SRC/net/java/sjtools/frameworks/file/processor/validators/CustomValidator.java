package net.java.sjtools.frameworks.file.processor.validators;

import java.lang.reflect.Method;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public class CustomValidator implements Validator {

	private Object customValidatorObject = null;
	private Method customValidatorMethod = null;

	public CustomValidator(String javaClass, String method) throws FileUploadError {
		try {
			Class validatorClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);
			customValidatorObject = validatorClass.newInstance();
			customValidatorMethod = validatorClass.getMethod(method, new Class[] { String.class });

			if (customValidatorMethod.getReturnType() != Boolean.TYPE && customValidatorMethod.getReturnType() != Boolean.class) {
				throw new FileUploadError("Method " + method + " of class " + javaClass + " has to return a (Bb)oolean");
			}
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public boolean isValid(String value) {
		try {
			return ((Boolean) customValidatorMethod.invoke(customValidatorObject, new Object[] { value })).booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		return "CustomValidator(" + customValidatorObject.getClass().getName() + ", " + customValidatorMethod.getName() + ")";
	}

}
