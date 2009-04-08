package net.java.sjtools.frameworks.file.processor.util;

import java.util.Iterator;

import net.java.sjtools.frameworks.file.processor.model.File;
import net.java.sjtools.frameworks.file.processor.model.Files;
import net.java.sjtools.frameworks.file.processor.model.error.ValidationRuleError;

public class FileUtil {

	public static File getValidationFile(Files files, String validationFile) throws ValidationRuleError {
		File file = null;
		boolean found = false;

		for (Iterator iterator = files.getFiles().iterator(); iterator.hasNext();) {
			file = (File) iterator.next();

			if (file.getName().equals(validationFile)) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new ValidationRuleError(validationFile);
		}

		return file;
	}

}
