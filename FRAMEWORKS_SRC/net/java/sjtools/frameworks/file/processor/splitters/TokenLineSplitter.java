package net.java.sjtools.frameworks.file.processor.splitters;

import java.util.List;

import net.java.sjtools.frameworks.file.processor.model.error.InvalidRecordError;
import net.java.sjtools.util.TextUtil;

public class TokenLineSplitter extends LineSplitter {

	private Integer elementCount = null;
	private String token = null;
	private boolean optionalLastToken = false;

	public TokenLineSplitter(Integer elementCount, String token, boolean optionalLastToken) {
		this.elementCount = elementCount;
		this.token = token;
		this.optionalLastToken = optionalLastToken;
	}

	public List split(String line) throws InvalidRecordError {
		List ret = TextUtil.split(line, token);

		if (elementCount != null) {
			switch (ret.size() - elementCount.intValue()) {
				case 0:
					if (!optionalLastToken) {
						throw new InvalidRecordError(line, toString());
					}
					break;
				case 1:
					if (!line.endsWith(token)) {
						throw new InvalidRecordError(line, toString());
					}
					break;
				default:
					throw new InvalidRecordError(line, toString());
			}
		}

		return ret;
	}

	public String toString() {
		return "TokenLineSplitter(" + elementCount + ", " + token + ", " + optionalLastToken + ")";
	}
}
