package net.java.sjtools.frameworks.file.processor.splitters;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;
import net.java.sjtools.frameworks.file.processor.model.error.InvalidRecordError;

public abstract class LineSplitter implements Splitter {

	private BufferedReader reader = null;

	public void init(InputStream inputStream) throws FileUploadError {
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public List nextRecord() throws FileUploadError {
		try {
			String line = reader.readLine();

			List ret = null;

			if (line != null) {
				ret = split(line);
			}

			return ret;
		} catch (FileUploadError e) {
			throw e;
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

	public abstract List split(String line) throws InvalidRecordError;

}
