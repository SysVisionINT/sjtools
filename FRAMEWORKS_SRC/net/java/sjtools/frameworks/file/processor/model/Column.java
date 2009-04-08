package net.java.sjtools.frameworks.file.processor.model;

import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.formatters.Formatter;

public class Column {

	private int position = -1;
	private boolean mandatory = true;

	private List preFormatValidators = new ArrayList();
	private List formatters = new ArrayList();
	private List postFormatValidators = new ArrayList();
	private String returnObjectProperty = null;
	private String returnObjectPropertyFormat = null;

	public Column(int position, boolean mandatory) {
		this.position = position;
		this.mandatory = mandatory;
	}

	public void addFormatter(Formatter formatter) {
		formatters.add(formatter);
	}

	public int getPosition() {
		return position;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public List getPreFormatValidators() {
		return preFormatValidators;
	}

	public void setPreFormatValidators(List preFormatValidators) {
		this.preFormatValidators = preFormatValidators;
	}

	public List getFormatters() {
		return formatters;
	}

	public void setFormatters(List formatters) {
		this.formatters = formatters;
	}

	public List getPostFormatValidators() {
		return postFormatValidators;
	}

	public void setPostFormatValidators(List postFormatValidators) {
		this.postFormatValidators = postFormatValidators;
	}

	public String getReturnObjectProperty() {
		return returnObjectProperty;
	}

	public void setReturnObjectProperty(String returnObjectProperty) {
		this.returnObjectProperty = returnObjectProperty;
	}

	public String getReturnObjectPropertyFormat() {
		return returnObjectPropertyFormat;
	}

	public void setReturnObjectPropertyFormat(String returnObjectPropertyFormat) {
		this.returnObjectPropertyFormat = returnObjectPropertyFormat;
	}

}
