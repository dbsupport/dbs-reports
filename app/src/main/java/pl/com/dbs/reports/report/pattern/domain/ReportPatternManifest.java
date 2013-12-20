/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import pl.com.dbs.reports.api.report.pattern.PatternManifest;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternManifest extends Manifest implements PatternManifest, Serializable {
	private static final long serialVersionUID = 1469586204373265839L;
	public static final String ATTRIBUTE_SECTION = "Database-Support-Reports";
	public static final String ATTRIBUTE_PATTERN_NAME = "Reports-Pattern-Name";
	public static final String ATTRIBUTE_PATTERN_VERSION = "Reports-Pattern-Version";
	public static final String ATTRIBUTE_PATTERN_FACTORY = "Reports-Pattern-Factory";
	public static final String ATTRIBUTE_PATTERN_AUTHOR = "Reports-Pattern-Author";
	public static final String ATTRIBUTE_ACCESSES = "Reports-Accesses";
	public static final String ATTRIBUTE_INIT_SQL = "Reports-Init-Sql";
	public static final String ATTRIBUTE_EXTENSION_MAP = "Reports-Extension-Map";
	public static final String ATTRIBUTE_NAME_TEMPLATE = "Reports-Name-Template";
	public static final String REPORT_NAME_TEMPLATE_DEFAULT = "${filename}-{$date-time}";
	
	public static final java.util.regex.Pattern MANIFEST_PATTERN = java.util.regex.Pattern.compile("^manifest(\\..+)*$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	public static final java.util.regex.Pattern INFLATER_PATTERN = java.util.regex.Pattern.compile("^.+(\\.sql){1}$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	public static final java.util.regex.Pattern EXTENSION_PATTERN = java.util.regex.Pattern.compile("^(.+)=(.+)$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	
	public ReportPatternManifest() {
		super();
	}
	
	public ReportPatternManifest(InputStream is) throws IOException {
		super(is);
	}
	
	public ReportPatternManifest(String content) throws IOException {
		super(new ByteArrayInputStream(content.getBytes()));
	}
	
	@Override
	public Attributes getPatternAttributes() {
		return getAttributes(ATTRIBUTE_SECTION);
	}
	
	@Override
	public String getPatternAttribute(String attribute) {
		Attributes attributes = getPatternAttributes();
		return attributes!=null?attributes.getValue(attribute):null;
	}
	
	public String getText() {
		try {
			OutputStream os = new ByteArrayOutputStream();
			write(os);
			os.close();
			return os.toString();
		} catch (IOException e) {}		
		return null;
	}

}
