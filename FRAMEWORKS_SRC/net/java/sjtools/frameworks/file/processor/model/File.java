package net.java.sjtools.frameworks.file.processor.model;

import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.splitters.Splitter;

public class File {

	private String name = null;
	private Splitter splitter = null;
	private Integer minimumRecords = null;
	private Integer maximumRecords = null;
	private String returnObjectClass = null;
	private List columns = new ArrayList();

	public File(String name, Integer minimumRecords, Integer maximumRecords, String returnObjectClass) {
		this.name = name;
		this.minimumRecords = minimumRecords;
		this.maximumRecords = maximumRecords;
		this.returnObjectClass = returnObjectClass;
	}

	public void add(Column column) {
		columns.add(column);
	}

	public String getName() {
		return name;
	}

	public Splitter getSplitter() {
		return splitter;
	}

	public void setSplitter(Splitter splitter) {
		this.splitter = splitter;
	}

	public Integer getMinimumRecords() {
		return minimumRecords;
	}

	public Integer getMaximumRecords() {
		return maximumRecords;
	}

	public String getReturnObjectClass() {
		return returnObjectClass;
	}

	public List getColumns() {
		return columns;
	}

}
