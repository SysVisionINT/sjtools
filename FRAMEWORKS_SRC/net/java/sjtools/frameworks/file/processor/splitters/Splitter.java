package net.java.sjtools.frameworks.file.processor.splitters;

import java.io.InputStream;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;

public interface Splitter {

	public void init(InputStream inputStream) throws FileUploadError;

	public List nextRecord() throws FileUploadError;

}
