package net.java.sjtools.frameworks.file.processor.xml;

import java.io.InputStream;

import net.java.sjtools.frameworks.file.processor.model.Files;
import net.java.sjtools.xml.SimpleParser;

public class Reader {

	public static Files read(InputStream inputStream) throws Exception {
		SimpleParser parser = new SimpleParser(new Handler(), true);

		parser.addDTD("-//SJTools//DTD File Validator 1.0//EN", Thread.currentThread().getContextClassLoader().getResource("fileprocessor.dtd").toString());

		return (Files) parser.parse(inputStream);
	}

}
