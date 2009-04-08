package net.java.sjtools.frameworks.file.processor;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.formatters.Formatter;
import net.java.sjtools.frameworks.file.processor.model.Column;
import net.java.sjtools.frameworks.file.processor.model.File;
import net.java.sjtools.frameworks.file.processor.model.RecordProcessor;
import net.java.sjtools.frameworks.file.processor.model.error.ColumnNotFoundError;
import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;
import net.java.sjtools.frameworks.file.processor.model.error.InvalidElementError;
import net.java.sjtools.frameworks.file.processor.model.error.MandatoryElementError;
import net.java.sjtools.frameworks.file.processor.model.error.TooFewRecordsError;
import net.java.sjtools.frameworks.file.processor.model.error.TooManyRecordsError;
import net.java.sjtools.frameworks.file.processor.util.SimpleBeanUtil;
import net.java.sjtools.frameworks.file.processor.validators.Validator;
import net.java.sjtools.util.TextUtil;

public class Processor {

	public static void process(InputStream inputStream, File validation, RecordProcessor recordProcessor) throws FileUploadError {
		try {
			// Check if RecordProcessor is not null
			if (recordProcessor == null) {
				throw new FileUploadError("RecordProcessor is null");
			}

			// Get the return object		
			SimpleBeanUtil beanUtil = null;
			boolean hasReturnObject = validation.getReturnObjectClass() != null;

			// Splitter initialization
			validation.getSplitter().init(inputStream);

			boolean hasMaximumRecords = validation.getMaximumRecords() != null;

			int recordCount = 0;
			List elements = null;
			while ((elements = validation.getSplitter().nextRecord()) != null) {
				recordCount++;

				try {
					// Check record count limits
					if (hasMaximumRecords && recordCount > validation.getMaximumRecords().intValue()) {
						throw new TooManyRecordsError(validation.getMaximumRecords().intValue());
					}

					if (hasReturnObject) {
						beanUtil = new SimpleBeanUtil(validation.getReturnObjectClass());
					}

					// Execute column validations
					boolean hasValidationErrors = false;
					for (Iterator iterator = validation.getColumns().iterator(); iterator.hasNext();) {
						try {
							Column column = (Column) iterator.next();

							String value = null;

							try {
								value = (String) elements.get(column.getPosition() - 1);
							} catch (IndexOutOfBoundsException e) {
								throw new ColumnNotFoundError(recordCount, column.getPosition());
							}

							// Check if it is mandatory
							if (column.isMandatory() && TextUtil.isEmptyString(value)) {
								throw new MandatoryElementError(recordCount, column.getPosition());
							}

							if (!TextUtil.isEmptyString(value)) {
								// Validate the value with all pre-format validators
								for (Iterator iterator2 = column.getPreFormatValidators().iterator(); iterator2.hasNext();) {
									Validator validator = (Validator) iterator2.next();

									if (!validator.isValid(value)) {
										throw new InvalidElementError(recordCount, column.getPosition(), validator.toString(), value);
									}
								}

								// Format the value
								for (Iterator iterator2 = column.getFormatters().iterator(); iterator2.hasNext();) {
									Formatter formatter = (Formatter) iterator2.next();

									value = formatter.format(value);
								}

								// Validate the value with all post-format validators
								for (Iterator iterator2 = column.getPostFormatValidators().iterator(); iterator2.hasNext();) {
									Validator validator = (Validator) iterator2.next();

									if (!validator.isValid(value)) {
										throw new InvalidElementError(recordCount, column.getPosition(), validator.toString(), value);
									}
								}

								// Set the value (if there is a return object and the column has a return property)
								if (hasReturnObject && column.getReturnObjectProperty() != null) {
									beanUtil.set(value, column.getReturnObjectProperty(), column.getReturnObjectPropertyFormat());
								}
							}
						} catch (FileUploadError e) {
							recordProcessor.processValidationError(e);
							hasValidationErrors = true;
						}
					}

					// Process the newly created bean
					if (hasReturnObject && !hasValidationErrors) {
						recordProcessor.processReturnObject(beanUtil.getBean());
					}
				} catch (FileUploadError e) {
					recordProcessor.processValidationError(e);
				}
			}

			// Check record count limits
			if (validation.getMinimumRecords() != null && recordCount < validation.getMinimumRecords().intValue()) {
				recordProcessor.processValidationError(new TooFewRecordsError(validation.getMinimumRecords().intValue()));
			}
		} catch (FileUploadError e) {
			throw e;
		} catch (Exception e) {
			throw new FileUploadError(e);
		}
	}

}
