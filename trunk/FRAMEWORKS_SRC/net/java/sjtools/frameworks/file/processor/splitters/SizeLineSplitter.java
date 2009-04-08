package net.java.sjtools.frameworks.file.processor.splitters;

import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.model.error.InvalidRecordError;
import net.java.sjtools.util.TextUtil;

public class SizeLineSplitter extends LineSplitter {

	private Integer elementCount = null;
	private int elementLength = 0;

	public SizeLineSplitter(Integer elementCount, int elementLength) {
		this.elementCount = elementCount;
		this.elementLength = elementLength;
	}

	public List split(String line) throws InvalidRecordError {
		List ret = new ArrayList();

		if (!TextUtil.isEmptyString(line)) {
			char[] lineArray = line.toCharArray();

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < lineArray.length; i++) {
				buffer.append(lineArray[i]);

				if (buffer.length() == elementLength || i == lineArray.length - 1) {
					ret.add(buffer.toString());
					buffer = new StringBuffer();
				}
			}

			if (elementCount != null && ret.size() != elementCount.intValue()) {
				throw new InvalidRecordError(line, toString());
			}
		}

		return ret;
	}

	public String toString() {
		return "SizeLineSplitter(" + elementCount + ", " + elementLength + ")";
	}
}
