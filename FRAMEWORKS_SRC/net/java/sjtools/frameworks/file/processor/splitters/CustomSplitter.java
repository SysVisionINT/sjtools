package net.java.sjtools.frameworks.file.processor.splitters;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public class CustomSplitter implements Splitter {

	private Object customSplitterObject = null;
	private Method customSplitterInit = null;
	private Method customSplitterNextRecord = null;

	public CustomSplitter(String javaClass, String initMethod, String nextRecordMethod) throws FileUploadError {
		try {
			Class validatorClass = Thread.currentThread().getContextClassLoader().loadClass(javaClass);

			customSplitterObject = validatorClass.newInstance();

			customSplitterInit = validatorClass.getMethod(initMethod, new Class[] { InputStream.class });

			customSplitterNextRecord = validatorClass.getMethod(nextRecordMethod, null);

			if (customSplitterNextRecord.getReturnType() != List.class) {
				throw new FileUploadError("Method " + nextRecordMethod + " of class " + javaClass + " has to return a List");
			}
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public void init(InputStream inputStream) throws FileUploadError {
		try {
			customSplitterInit.invoke(customSplitterObject, new Object[] { inputStream });
		} catch (Exception e) {
			throw new FileUploadError(e);
		}

	}

	public List nextRecord() throws FileUploadError {
		try {
			return (List) customSplitterNextRecord.invoke(customSplitterObject, null);
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public String toString() {
		return "CustomSplitter(" + customSplitterObject.getClass().getName() + ", " + customSplitterInit.getName() + ", " + customSplitterNextRecord.getName() + ")";
	}

}
