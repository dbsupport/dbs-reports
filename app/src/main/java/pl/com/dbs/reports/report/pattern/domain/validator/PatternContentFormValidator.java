/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternForm;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;


/**
 * Validate form definition if given.
 *
 * http://www.rgagnon.com/javadetails/java-0669.html
 * http://stackoverflow.com/questions/18301213/xml-validation-against-many-xsds-of-incorrect-xml-doc-not-report-any-errors-in
 * http://stackoverflow.com/questions/4864681/jaxb-2-0-schema-validation-problem
 * http://stackoverflow.com/questions/2342808/problem-validating-an-xml-file-using-java-with-an-xsd-having-an-include
 * http://stackoverflow.com/questions/7113219/saxparseexception-in-xsd-validation-does-not-give-element-name
 * 
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternContentFormValidator extends PatternValidator implements ErrorHandler {
	private static final Logger logger = Logger.getLogger(PatternContentFormValidator.class);
	private static final String SCHEMA = "/pl/com/dbs/reports/form-schema-1.0.0.xsd";
	
	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		List<? extends PatternForm> forms = pattern.getForms();
		if (forms.isEmpty()) return;
   		validateWithoutJAXB(forms);
   		//validateWithoutJAXB2(forms);
	}
	
	/**
	 * The first is to use the javax.xml.validation APIs to validate your document against an XML schema without JAXB.
	 */
	private void validateWithoutJAXB(List<? extends PatternForm> forms) throws PatternValidationException {
    	SAXParserFactory factory = SAXParserFactory.newInstance();
    	factory.setValidating(false);
    	factory.setNamespaceAware(true);

    	try {
			URL url = getClass().getResource(SCHEMA); //URL url = Resources.getResource(SCHEMA);//NOT WORKING in web app!
			
	    	SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    	factory.setSchema(schemaFactory.newSchema(url));
	    	logger.debug("Schema: "+Resources.toString(url, Charsets.UTF_8));
	    	
	    	SAXParser parser = factory.newSAXParser();
	    	
	    	for (PatternForm form : forms) {
	    		try {
	    			logger.debug("Validating:"+new String(form.getContent()));
					ByteArrayInputStream in = new ByteArrayInputStream(form.getContent());
					InputSource source = new InputSource(in);
					XMLReader reader = parser.getXMLReader();
					reader.setErrorHandler(this);
					reader.parse(source);
	    		} catch (Exception e) {
	    			throw new PatternValidationException(e, "report.pattern.import.content.validation.detailed.error", Arrays.asList(new String[]{form.getName(), e.getMessage()}));
	    		}
	    	}		
		} catch (ParserConfigurationException e) {
			throw new PatternValidationException(e, "report.pattern.import.schema.parser.not.valid");
		} catch (SAXException e) {
			throw new PatternValidationException(e, "report.pattern.import.schema.not.valid");
		} catch (IOException e) {
			throw new PatternValidationException(e, "report.pattern.import.schema.not.found");
		}
	}
	
//	private void validateWithoutJAXB2(List<? extends PatternForm> forms) throws PatternValidationException {
//		try {
//			URL url = getClass().getResource(SCHEMA); //URL url = Resources.getResource(SCHEMA);//NOT WORKING in web app!
//			logger.debug("Schema:"+Resources.toString(url, Charsets.UTF_8));
//			
//			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//			Schema schema = sf.newSchema(url);
//			
//			for (PatternForm form : forms) {
//		        try {
//		        	logger.debug("Validating:"+new String(form.getContent()));
//					ByteArrayInputStream in = new ByteArrayInputStream(form.getContent());
//					InputSource source = new InputSource(in);		
//			        SAXSource saxsource = new SAXSource(source);
//			        
//			        Validator validator = schema.newValidator();
//			        validator.setErrorHandler(this);
//			        validator.validate(saxsource);
//				} catch (Exception e) {
//					throw new PatternValidationException(e, "report.pattern.import.content.validation.detailed.error", Arrays.asList(new String[]{form.getName(), e.getMessage()}));
//				}
//			}
//		} catch (SAXException e) {
//			throw new PatternValidationException(e, "report.pattern.import.schema.not.valid");
//		} catch (IOException e) {
//			throw new PatternValidationException(e, "report.pattern.import.schema.not.found");
//		}
//		
//	}
	
//	private void validateWithoutJAXB3(List<? extends PatternForm> forms) throws PatternValidationException {
//		try {
//			URL url = getClass().getResource(SCHEMA);
//
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//	        factory.setValidating(true);
//	        factory.setAttribute(
//	                "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
//	                "http://www.w3.org/2001/XMLSchema");
//	        factory.setAttribute(
//	                "http://java.sun.com/xml/jaxp/properties/schemaSource",
//	                new InputSource(url.openStream()));
//	        DocumentBuilder parser = factory.newDocumentBuilder();
//	        parser.setErrorHandler(this);
//	        
//	        logger.debug("Schema:"+factory.getSchema());
//	        for (PatternForm form : forms) {
//		    	try {
//		        	logger.debug("Validating:"+new String(form.getContent()));
//					ByteArrayInputStream in = new ByteArrayInputStream(form.getContent());
//					InputSource source = new InputSource(in);		
//		            parser.parse(source); 
//				} catch (Exception e) {
//					throw new PatternValidationException(e, "report.pattern.import.content.validation.detailed.error", Arrays.asList(new String[]{form.getName(), e.getMessage()}));
//				}    		
//	        }
//		} catch (ParserConfigurationException e) {
//			throw new PatternValidationException(e, "report.pattern.import.schema.parser.not.valid");
////		} catch (SAXException e) {
////			throw new PatternValidationException(e, "report.pattern.import.schema.not.valid");
//		} catch (IOException e) {
//			throw new PatternValidationException(e, "report.pattern.import.schema.not.found");
//		}
//	}	
	
//	/**
//	 * The second approach is to validate while performing an unmarshal operation with JAXB.
//	 */
//	private void validateWitJAXB(List<? extends PatternForm> forms) throws PatternValidationException {
//        JAXBContext jc = JAXBContext.newInstance(Destination.class);
//        Unmarshaller unmarshaller = jc.createUnmarshaller();
//
//        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        Schema schema = sf.newSchema(getClass().getResource("/pl/com/dbs/reports/form-schema-1.0.0.xsd"));
//        unmarshaller.setSchema(schema);
//        unmarshaller.setEventHandler(this);
//
//        String xmlFileLocation = "src/validate/blog/input.xml";
//        unmarshaller.unmarshal(new InputSource(xmlFileLocation));		
//	}
	
	@Override
	public void warning(SAXParseException e) throws SAXParseException {
	    throw e;
	}
		
	@Override
	public void error(SAXParseException e) throws SAXParseException {
	    throw e;
	}
	
	@Override
	public void fatalError(SAXParseException e) throws SAXParseException {
		throw e;
	}
		

	@Override
	public int getOrder() {
		return 6;
	}	
}
