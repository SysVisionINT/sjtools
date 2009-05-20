/*
 * SJTools - SysVision Java Tools
 * 
 * Copyright (C) 2009 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.  
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package net.java.sjtools.frameworks.recordProcessor;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import net.java.sjtools.frameworks.recordProcessor.formatters.Formatter;
import net.java.sjtools.frameworks.recordProcessor.model.Column;
import net.java.sjtools.frameworks.recordProcessor.model.RecordProcessor;
import net.java.sjtools.frameworks.recordProcessor.model.RuleSet;
import net.java.sjtools.frameworks.recordProcessor.model.error.ColumnNotFoundError;
import net.java.sjtools.frameworks.recordProcessor.model.error.InvalidElementError;
import net.java.sjtools.frameworks.recordProcessor.model.error.MandatoryElementError;
import net.java.sjtools.frameworks.recordProcessor.model.error.ProcessorError;
import net.java.sjtools.frameworks.recordProcessor.model.error.TooFewRecordsError;
import net.java.sjtools.frameworks.recordProcessor.model.error.TooManyRecordsError;
import net.java.sjtools.frameworks.recordProcessor.util.SimpleBeanUtil;
import net.java.sjtools.frameworks.recordProcessor.validators.Validator;
import net.java.sjtools.util.TextUtil;

public class Processor {

	public static void process(InputStream inputStream, RuleSet ruleSet, RecordProcessor recordProcessor) throws Exception {
		// Check if RecordProcessor is not null
		if (recordProcessor == null) {
			throw new ProcessorError("RecordProcessor is null");
		}

		// Get the return object		
		SimpleBeanUtil beanUtil = null;
		boolean hasReturnObject = ruleSet.getReturnObjectClass() != null;

		// Splitter initialization
		ruleSet.getSplitter().init(inputStream);

		boolean hasMaximumRecords = ruleSet.getMaximumRecords() != null;

		int recordCount = 0;
		List elements = null;
		while ((elements = ruleSet.getSplitter().nextRecord()) != null) {
			recordCount++;

			try {
				// Check record count limits
				if (hasMaximumRecords && recordCount > ruleSet.getMaximumRecords().intValue()) {
					throw new TooManyRecordsError(ruleSet.getMaximumRecords().intValue());
				}

				if (hasReturnObject) {
					beanUtil = new SimpleBeanUtil(ruleSet.getReturnObjectClass());
				}

				// Execute column validations
				boolean hasValidationErrors = false;
				for (Iterator iterator = ruleSet.getColumns().iterator(); iterator.hasNext();) {
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
					} catch (ProcessorError e) {
						recordProcessor.processError(e);
						hasValidationErrors = true;
					}
				}

				// Process the newly created bean
				if (hasReturnObject && !hasValidationErrors) {
					recordProcessor.processReturnObject(beanUtil.getBean());
				}
			} catch (ProcessorError e) {
				recordProcessor.processError(e);
			}
		}

		// Check record count limits
		if (ruleSet.getMinimumRecords() != null && recordCount < ruleSet.getMinimumRecords().intValue()) {
			recordProcessor.processError(new TooFewRecordsError(ruleSet.getMinimumRecords().intValue()));
		}
	}

}
