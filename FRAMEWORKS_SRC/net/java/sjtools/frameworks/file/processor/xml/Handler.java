package net.java.sjtools.frameworks.file.processor.xml;

import java.util.ArrayList;
import java.util.List;

import net.java.sjtools.frameworks.file.processor.formatters.CustomFormatter;
import net.java.sjtools.frameworks.file.processor.formatters.RemoveTextDelimiterFormatter;
import net.java.sjtools.frameworks.file.processor.model.Column;
import net.java.sjtools.frameworks.file.processor.model.File;
import net.java.sjtools.frameworks.file.processor.model.Files;
import net.java.sjtools.frameworks.file.processor.model.error.FileUploadError;
import net.java.sjtools.frameworks.file.processor.splitters.CustomSplitter;
import net.java.sjtools.frameworks.file.processor.splitters.SizeLineSplitter;
import net.java.sjtools.frameworks.file.processor.splitters.TokenLineSplitter;
import net.java.sjtools.frameworks.file.processor.validators.BiggerThanValidator;
import net.java.sjtools.frameworks.file.processor.validators.BooleanValidator;
import net.java.sjtools.frameworks.file.processor.validators.ByteValidator;
import net.java.sjtools.frameworks.file.processor.validators.CharValidator;
import net.java.sjtools.frameworks.file.processor.validators.CustomValidator;
import net.java.sjtools.frameworks.file.processor.validators.DateTimeValidator;
import net.java.sjtools.frameworks.file.processor.validators.DoubleValidator;
import net.java.sjtools.frameworks.file.processor.validators.FloatValidator;
import net.java.sjtools.frameworks.file.processor.validators.InListValidator;
import net.java.sjtools.frameworks.file.processor.validators.IntegerValidator;
import net.java.sjtools.frameworks.file.processor.validators.LongValidator;
import net.java.sjtools.frameworks.file.processor.validators.RegexpValidator;
import net.java.sjtools.frameworks.file.processor.validators.ShortValidator;
import net.java.sjtools.frameworks.file.processor.validators.SmallerThanValidator;
import net.java.sjtools.util.TextUtil;
import net.java.sjtools.xml.SimpleHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Handler extends SimpleHandler {

	public void error(SAXParseException e) throws SAXException {}

	public Object proccessElement(String elementType, Object currentObject, Attributes attributes) throws SAXException {
		try {
			if (elementType.equals("files")) {
				return new Files();
			} else if (elementType.equals("file")) {
				String name = attributes.getValue("name");

				String auxMinimumRecords = attributes.getValue("minimum-records");
				Integer minimumRecords = null;
				if (!TextUtil.isEmptyString(auxMinimumRecords)) {
					minimumRecords = new Integer(auxMinimumRecords);
				}

				String auxMaximumRecords = attributes.getValue("maximum-records");
				Integer maximumRecords = null;
				if (!TextUtil.isEmptyString(auxMaximumRecords)) {
					maximumRecords = new Integer(auxMaximumRecords);
				}

				String auxRetObjClass = attributes.getValue("return-object-class");
				String returnObjectClass = null;
				if (!TextUtil.isEmptyString(auxRetObjClass)) {
					returnObjectClass = auxRetObjClass;
				}

				File file = new File(name, minimumRecords, maximumRecords, returnObjectClass);

				((Files) currentObject).add(file);

				return file;
			} else if (elementType.equals("token-line-splitter")) {
				String auxElementCount = attributes.getValue("element-count");
				Integer elementCount = null;
				if (!TextUtil.isEmptyString(auxElementCount)) {
					elementCount = new Integer(auxElementCount);
				}

				String token = attributes.getValue("token");

				boolean optionalLastToken = new Boolean(attributes.getValue("optional-last-token")).booleanValue();

				((File) currentObject).setSplitter(new TokenLineSplitter(elementCount, token, optionalLastToken));

				return null;
			} else if (elementType.equals("size-line-splitter")) {
				String auxElementCount = attributes.getValue("element-count");
				Integer elementCount = null;
				if (!TextUtil.isEmptyString(auxElementCount)) {
					elementCount = new Integer(auxElementCount);
				}

				int elementLength = Integer.parseInt(attributes.getValue("element-length"));

				((File) currentObject).setSplitter(new SizeLineSplitter(elementCount, elementLength));

				return null;
			} else if (elementType.equals("custom-splitter")) {
				String javaClass = attributes.getValue("java-class");
				String initMethod = attributes.getValue("init-method");
				String nextRecordMethod = attributes.getValue("next-record-method");

				((File) currentObject).setSplitter(new CustomSplitter(javaClass, initMethod, nextRecordMethod));

				return null;
			} else if (elementType.equals("column")) {
				int position = Integer.parseInt(attributes.getValue("position"));
				boolean mandatory = new Boolean(attributes.getValue("mandatory")).booleanValue();

				Column column = new Column(position, mandatory);

				((File) currentObject).add(column);

				return column;
			} else if (elementType.equals("pre-format-validators")) {
				List preFormatValidators = new ArrayList();

				((Column) currentObject).setPreFormatValidators(preFormatValidators);

				return preFormatValidators;
			} else if (elementType.equals("formatters")) {
				// It's just a place holder
				return null;
			} else if (elementType.equals("remove-text-delimiter-formatter")) {
				((Column) currentObject).addFormatter(new RemoveTextDelimiterFormatter(attributes.getValue("delimiter")));

				return null;
			} else if (elementType.equals("custom-formatter")) {
				String javaClass = attributes.getValue("java-class");
				String formatMethod = attributes.getValue("format-method");

				((Column) currentObject).addFormatter(new CustomFormatter(javaClass, formatMethod));

				return null;
			} else if (elementType.equals("post-format-validators")) {
				List postFormatValidators = new ArrayList();

				((Column) currentObject).setPostFormatValidators(postFormatValidators);

				return postFormatValidators;
			} else if (elementType.equals("return-property")) {
				String name = attributes.getValue("name");

				String auxFormat = attributes.getValue("format");
				String format = null;
				if (!TextUtil.isEmptyString(auxFormat)) {
					format = auxFormat;
				}

				Column currentColumn = (Column) currentObject;
				currentColumn.setReturnObjectProperty(name);
				currentColumn.setReturnObjectPropertyFormat(format);

				return null;
			} else if (elementType.equals("boolean-validator")) {
				((List) currentObject).add(new BooleanValidator());

				return null;
			} else if (elementType.equals("byte-validator")) {
				((List) currentObject).add(new ByteValidator());

				return null;
			} else if (elementType.equals("char-validator")) {
				((List) currentObject).add(new CharValidator());

				return null;
			} else if (elementType.equals("short-validator")) {
				((List) currentObject).add(new ShortValidator());

				return null;
			} else if (elementType.equals("integer-validator")) {
				((List) currentObject).add(new IntegerValidator());

				return null;
			} else if (elementType.equals("long-validator")) {
				((List) currentObject).add(new LongValidator());

				return null;
			} else if (elementType.equals("float-validator")) {
				((List) currentObject).add(new FloatValidator());

				return null;
			} else if (elementType.equals("double-validator")) {
				((List) currentObject).add(new DoubleValidator());

				return null;
			} else if (elementType.equals("date-time-validator")) {
				((List) currentObject).add(new DateTimeValidator(attributes.getValue("format")));

				return null;
			} else if (elementType.equals("in-list-validator")) {
				InListValidator inListValidator = new InListValidator();

				((List) currentObject).add(inListValidator);

				return inListValidator;
			} else if (elementType.equals("bigger-than-validator")) {
				((List) currentObject).add(new BiggerThanValidator(attributes.getValue("value")));

				return null;
			} else if (elementType.equals("smaller-than-validator")) {
				((List) currentObject).add(new SmallerThanValidator(attributes.getValue("value")));

				return null;
			} else if (elementType.equals("regexp-validator")) {
				((List) currentObject).add(new RegexpValidator(attributes.getValue("pattern")));

				return null;
			} else if (elementType.equals("custom-validator")) {
				((List) currentObject).add(new CustomValidator(attributes.getValue("java-class"), attributes.getValue("method")));

				return null;
			}

			return null;
		} catch (FileUploadError e) {
			throw new SAXException(e);
		}
	}

	public void processPCDATA(String elementType, Object currentObject, String value) throws SAXException {
		if (elementType.equals("value")) {
			((InListValidator) currentObject).add(value);
		}
	}
}
