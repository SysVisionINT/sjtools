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
import net.java.sjtools.frameworks.recordProcessor.model.error.ValueSetError;
import net.java.sjtools.frameworks.recordProcessor.util.SimpleBeanUtil;
import net.java.sjtools.frameworks.recordProcessor.validators.Validator;
import net.java.sjtools.util.TextUtil;

public class Processor {

	private RuleSet ruleSet = null;
	private RecordProcessor recordProcessor = null;
	private SimpleBeanUtil beanUtil = null;

	private boolean hasMaximumRecords = false;
	private boolean hasReturnObject = false;

	public Processor(RuleSet ruleSet, RecordProcessor recordProcessor) throws ProcessorError {
		// Check if RuleSet is not null
		if (ruleSet == null) {
			throw new ProcessorError("RuleSet is null");
		}

		// Check if RecordProcessor is not null
		if (recordProcessor == null) {
			throw new ProcessorError("RecordProcessor is null");
		}

		this.ruleSet = ruleSet;
		this.recordProcessor = recordProcessor;

		// Check if there is an object to return		
		if (ruleSet.getReturnObjectClass() != null) {
			hasReturnObject = true;
			beanUtil = new SimpleBeanUtil(ruleSet.getReturnObjectClass());
		}

		if (ruleSet.getMaximumRecords() != null) {
			hasMaximumRecords = true;
		}
	}

	public void process(InputStream inputStream) throws Exception {
		// Splitter initialization
		ruleSet.getSplitter().init(inputStream);

		Column column = null;
		Validator validator = null;
		Formatter formatter = null;

		int recordCount = 0;
		List elements = null;
		String value = null;
		boolean hasValidationErrors = false;

		while (ruleSet.getSplitter().hasNext()) {
			recordCount++;

			recordProcessor.startRecord();

			try {
				elements = ruleSet.getSplitter().nextRecord();

				// Check record count limits
				if (hasMaximumRecords && recordCount > ruleSet.getMaximumRecords().intValue()) {
					throw new TooManyRecordsError(ruleSet.getMaximumRecords().intValue());
				}

				if (hasReturnObject) {
					beanUtil.initialize();
				}

				// Execute column validations
				hasValidationErrors = false;
				for (Iterator iterator = ruleSet.getColumns().iterator(); iterator.hasNext();) {
					try {
						column = (Column) iterator.next();

						try {
							value = (String) elements.get(column.getPosition() - 1);
						} catch (IndexOutOfBoundsException e) {
							throw new ColumnNotFoundError(recordCount, column.getPosition());
						}

						if (TextUtil.isEmptyString(value)) {
							// Check if it is mandatory
							if (column.isMandatory()) {
								throw new MandatoryElementError(recordCount, column.getPosition());
							}
						} else {
							// Validate the value with all pre-format validators
							for (Iterator iterator2 = column.getPreFormatValidators().iterator(); iterator2.hasNext();) {
								validator = (Validator) iterator2.next();

								if (!validator.isValid(value)) {
									throw new InvalidElementError(recordCount, column.getPosition(), validator.toString(), value);
								}
							}

							// Format the value
							for (Iterator iterator2 = column.getFormatters().iterator(); iterator2.hasNext();) {
								formatter = (Formatter) iterator2.next();

								value = formatter.format(value);
							}

							// Validate the value with all post-format validators
							for (Iterator iterator2 = column.getPostFormatValidators().iterator(); iterator2.hasNext();) {
								validator = (Validator) iterator2.next();

								if (!validator.isValid(value)) {
									throw new InvalidElementError(recordCount, column.getPosition(), validator.toString(), value);
								}
							}

							// Set the value (if there is a return object and the column has a return property)
							if (hasReturnObject && column.getReturnObjectProperty() != null) {
								try {
									beanUtil.set(value, column.getReturnObjectProperty(), column.getReturnObjectPropertyFormat());
								} catch (Exception e) {
									throw new ValueSetError(recordCount, column.getPosition(), column.getReturnObjectProperty(), value, column.getReturnObjectPropertyFormat(), e);
								}
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

			recordProcessor.endRecord();
		}

		// Check record count limits
		if (ruleSet.getMinimumRecords() != null && recordCount < ruleSet.getMinimumRecords().intValue()) {
			recordProcessor.processError(new TooFewRecordsError(ruleSet.getMinimumRecords().intValue()));
		}
	}

}
