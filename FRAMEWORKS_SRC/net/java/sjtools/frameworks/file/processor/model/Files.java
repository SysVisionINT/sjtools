package net.java.sjtools.frameworks.file.processor.model;

import java.util.ArrayList;
import java.util.List;

public class Files {

	private List files = new ArrayList();

	public void add(File file) {
		files.add(file);
	}

	public List getFiles() {
		return files;
	}

}
