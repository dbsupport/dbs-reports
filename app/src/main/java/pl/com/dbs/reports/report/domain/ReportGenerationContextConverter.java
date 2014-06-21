/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;

import com.google.common.collect.Sets;

/**
 * Converts binary data to generation context and vice versa.
 *  
 * Serialize object using JAXB.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
//@Converter(autoApply=true)
@Deprecated
public class ReportGenerationContextConverter implements AttributeConverter<ReportGenerationContext, String> {
	private static final Logger logger = Logger.getLogger(ReportGenerationContextConverter.class);
	private static Set<Class<?>> classes = Sets.<Class<?>>newHashSet(
																	PatternFormat.class,
																	ReportType.class,
																	ReportGenerationContextDefault.class);		
	
	
	@Override
	public String convertToDatabaseColumn(ReportGenerationContext value) {
		if (value instanceof ReportGenerationContextDefault) {
			return marshal((ReportGenerationContextDefault)value);
		}
		return null;
	}

	@Override
	public ReportGenerationContext convertToEntityAttribute(String value) {
		return unmarshall(value);
	}
	
	
	private String marshal(ReportGenerationContextDefault context) {
		try {
			OutputStream os = new ByteArrayOutputStream();
			JAXBContext jaxbcontext = org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(classes.toArray(new Class[classes.size()]), null);
		    Marshaller m = jaxbcontext.createMarshaller();
		    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    m.marshal(context, os);
		    os.close();
		    return os.toString();
		} catch (Exception e) {
			logger.error("Error marshalling: "+context.getClass().getCanonicalName(), e);
		}
		return null;
	}
	
	private ReportGenerationContext unmarshall(String value) {
		try {
			byte[] content = value.getBytes();
			JAXBContext jaxbcontext = org.eclipse.persistence.jaxb.JAXBContextFactory.createContext(classes.toArray(new Class[classes.size()]), null);
			
		    Unmarshaller unmarshaller = jaxbcontext.createUnmarshaller();
		    
		    ByteArrayInputStream in = new ByteArrayInputStream(content);
		    return (ReportGenerationContextDefault) unmarshaller.unmarshal(in);
		} catch (Exception e) {
			logger.error("Error unmarshalling to "+ReportGenerationContextDefault.class.getCanonicalName(), e);
		}
		return null;
	}
}
