package net.java.sjtools.frameworks.recordProcessor.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldAndMethod {

	private Field field = null;
	private Method method = null;

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
