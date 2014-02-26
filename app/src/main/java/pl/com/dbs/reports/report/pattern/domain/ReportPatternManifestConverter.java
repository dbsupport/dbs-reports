/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts binary data to manifest data. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Converter(autoApply=true)
public class ReportPatternManifestConverter implements AttributeConverter<ReportPatternManifest, String> {
	
	@Override
	public String convertToDatabaseColumn(ReportPatternManifest value) {
		try {
			OutputStream os = new ByteArrayOutputStream();
			value.write(os);
			os.close();
			return os.toString();
		} catch (IOException e) {}
		return null;
	}

	@Override
	public ReportPatternManifest convertToEntityAttribute(String value) {
		try {
			return new ReportPatternManifest(value);
		} catch (IOException e) {}	    	
		return null;
	}
}
