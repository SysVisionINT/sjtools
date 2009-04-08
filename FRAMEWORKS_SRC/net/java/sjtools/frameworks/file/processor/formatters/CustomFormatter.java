package net.java.sjtools.frameworks.file.processor.formatters;

import java.lang.reflect.Method;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public class CustomFormatter implements Formatter {

	private Object customFormatterObject = null;
	private Method customFormatterFormat = null;

	public CustomFormatter(String javaClass, String formatMethod) throws FileUploadError {
		try {
			Class validatorClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);

			customFormatterObject = validatorClass.newInstance();

			customFormatterFormat = validatorClass.getMethod(formatMethod, new Class[] { String.class });

			if (customFormatterFormat.getReturnType() != String.class) {
				throw new FileUploadError("Method " + formatMethod + " of class " + javaClass + " has to return a String");
			}
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public String format(String value) throws FileUploadError {
		try {
			return (String) customFormatterFormat.invoke(customFormatterObject, new String[] { value });
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

}
