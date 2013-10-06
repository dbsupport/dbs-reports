/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.inner.report.pattern.Pattern;
import pl.com.dbs.reports.report.pattern.domain.PatternAsset;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBuilder {
	private static final Logger logger = Logger.getLogger(ReportBuilder.class);
	public static final java.util.regex.Pattern VARIABLE_PATTERN = java.util.regex.Pattern.compile("(\\$\\{.+?\\})+",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final String REPORT_NAME_TEMPLATE_DEFAULT = "${filename}-{$date-time}";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmSS");
	public static final java.util.regex.Pattern EXTENSION_PATTERN = java.util.regex.Pattern.compile("^(.+)=(.+)$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	
	private ReportPattern pattern;
	private PatternAsset asset;
	private Map<String, String> params;
	private String ext; 
	private String name;
	private Date generationDate;
	
	public ReportBuilder(ReportPattern pattern, PatternAsset asset, Map<String, String> params) {
		this.pattern = pattern;
		this.asset = asset;
		this.params = params;
		this.generationDate = new Date();
	}
	
	public ReportBuilder ext(String ext) {
		this.ext = ext;
		return this;
	}
	
	public Report build() {
		buildName();
		buildExt();
		buildParams();

		String fullname = name+(!StringUtils.isBlank(ext)?("."+ext):"");
		return new Report(this.pattern, fullname , null);
	}
	
	private void buildName() {
		name = !StringUtils.isBlank(pattern.getAttribute(Pattern.ATTRIBUTE_NAME_TEMPLATE))?
							pattern.getAttribute(Pattern.ATTRIBUTE_NAME_TEMPLATE):
							REPORT_NAME_TEMPLATE_DEFAULT;
							
		Matcher mname = VARIABLE_PATTERN.matcher(name);
	    while (mname.find()) {
	    	String variable = StringUtils.trim(mname.group(1));
	    	ReportNameTemplate template = ReportNameTemplate.of(variable);
	    	switch (template.getId()) {
	    	case  1: 
	    		//..filename..
	    		name = name.replace(variable, asset.getPath());
	    	break;
	    	case 2:
	    		name = name.replace(variable, DATE_TIME_FORMAT.format(generationDate));
	    	break;
	    	case 3:
	    		name = name.replace(variable, DATE_FORMAT.format(generationDate));
	    	break;	
	    	default:
	    		logger.warn("Unknown template variable:"+variable);
	    	}
		}
	}
	    
    private void buildExt() {	    
    	if (!StringUtils.isBlank(ext)) return;
    	
	    //..resolve extension..
	    ext = asset.getExtension();
		String map = pattern.getAttribute(Pattern.ATTRIBUTE_EXTENSION_MAP);
		
		if (!StringUtils.isBlank(map)) {
			StringTokenizer st = new StringTokenizer(map, ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
			    Matcher mext = EXTENSION_PATTERN.matcher(token);
			    while (mext.find()) {
			    	final String file = StringUtils.trim(mext.group(1));
			    	final String e = StringUtils.trim(mext.group(2));
			    	if (asset.getPath().equalsIgnoreCase(file)) {
			    		ext = e; break;
			    	}
			    }
			}
	    }
	}
	
	
	private String buildParams() {
		return null;
	}
	
	
}
