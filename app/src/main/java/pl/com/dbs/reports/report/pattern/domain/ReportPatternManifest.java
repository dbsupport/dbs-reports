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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.report.pattern.PatternManifest;
import pl.com.dbs.reports.report.domain.ReportNameTemplate;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternManifest extends Manifest implements PatternManifest, Serializable {
	private static final long serialVersionUID = 1469586204373265839L;
	private static final Logger logger = Logger.getLogger(ReportPatternManifest.class);
	
	public static final String ATTRIBUTE_SECTION = "Database-Support-Reports";
	public static final String ATTRIBUTE_PATTERN_NAME = "Reports-Pattern-Name";
	public static final String ATTRIBUTE_PATTERN_VERSION = "Reports-Pattern-Version";
	public static final String ATTRIBUTE_PATTERN_FACTORY = "Reports-Pattern-Factory";
	public static final String ATTRIBUTE_PATTERN_AUTHOR = "Reports-Pattern-Author";
	public static final String ATTRIBUTE_ACCESSES = "Reports-Accesses";
	public static final String ATTRIBUTE_INIT_SQL = "Reports-Init-Sql";
	public static final String ATTRIBUTE_EXTENSION_MAP = "Reports-Extension-Map";
	public static final String ATTRIBUTE_NAME_TEMPLATE = "Reports-Name-Template";
	public static final String ATTRIBUTE_FORM_FILENAME = "Reports-Form-Filename";
	
	public static final java.util.regex.Pattern MANIFEST_PATTERN = java.util.regex.Pattern.compile("^manifest(\\..+)*$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	public static final java.util.regex.Pattern INFLATER_PATTERN = java.util.regex.Pattern.compile("^.+(\\.sql){1}$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	public static final java.util.regex.Pattern EXTENSION_PATTERN = java.util.regex.Pattern.compile("^(.+)=(.+)$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	public static final java.util.regex.Pattern VARIABLE_PATTERN = java.util.regex.Pattern.compile("(\\$\\{.+?\\})+",  java.util.regex.Pattern.CASE_INSENSITIVE);	
	
	public static final String REPORT_NAME_TEMPLATE_DEFAULT = ReportNameTemplate.FILENAME.getVkey()+"-"+ReportNameTemplate.DATE_TIME.getVkey();
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmSS");
	
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
	
	/**
	 * Constructs report name by name template. 
	 */
	@Override
	public String getNameTemplate() {
		String result = getPatternAttribute(ATTRIBUTE_NAME_TEMPLATE);
		if (StringUtils.isBlank(result)) result = REPORT_NAME_TEMPLATE_DEFAULT;
		Date now = new Date();
		Matcher name = VARIABLE_PATTERN.matcher(result);
	    while (name.find()) {
	    	String variable = StringUtils.trim(name.group(1));
	    	ReportNameTemplate template = ReportNameTemplate.of(variable);
	    	switch (template.getId()) {
	    	case  1: 
	    		//..filename..
	    		result = result.replace(variable, null);
	    	break;
	    	case 2:
	    		result = result.replace(variable, DATE_TIME_FORMAT.format(now));
	    	break;
	    	case 3:
	    		result = result.replace(variable, DATE_FORMAT.format(now));
	    	break;	
	    	default:
	    		logger.warn("Unknown template variable:"+variable);
	    	}
		}
	    return result;
	}
	
	@Override
	public String getName() {
		return getPatternAttribute(ATTRIBUTE_PATTERN_NAME);
	}
	
	@Override
	public String getFactory() {
		return getPatternAttribute(ATTRIBUTE_PATTERN_FACTORY);
	}
	
	@Override
	public String getVersion() {
		return getPatternAttribute(ATTRIBUTE_PATTERN_VERSION);
	}
}
