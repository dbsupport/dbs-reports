/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBuilder {
	private static final Logger logger = Logger.getLogger(ReportBuilder.class);
	public static final java.util.regex.Pattern VARIABLE_PATTERN = java.util.regex.Pattern.compile("(\\$\\{.+?\\})+",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmSS");
	
	private Pattern pattern;
	private Map<String, String> params;
	private ReportType type; 
	private String name;
	private Date generationDate;
	private Report report;
	
	@Autowired private PatternDao patternDao;
	
	public ReportBuilder(ReportGeneration gen) {
		this.pattern = patternDao.find(gen.getPatternId());
		this.params = gen.getParams();
		this.type = gen.getType();
		this.generationDate = new Date();
	}
	

	public ReportBuilder construct() {
		buildName();
		buildParams();

		String fullname = name+"."+type.getExt();
		this.report = new Report(this.pattern, fullname , null);
		return this;
	}
	
	public Report getReport() {
		return report;
	}
	
	
	private void buildName() {
		//name = pattern.getNameTemplate();
							
		Matcher mname = VARIABLE_PATTERN.matcher(name);
	    while (mname.find()) {
	    	String variable = StringUtils.trim(mname.group(1));
	    	ReportNameTemplate template = ReportNameTemplate.of(variable);
	    	switch (template.getId()) {
	    	case  1: 
	    		//..filename..
	    		name = name.replace(variable, null);
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
	    
//    private void buildExt() {	    
//    	if (!StringUtils.isBlank(ext)) return;
//    	
//	    //..resolve extension..
//	    ext = asset.getExtension();
//		String map = pattern.getAttribute(ReportPattern.ATTRIBUTE_EXTENSION_MAP);
//		
//		if (!StringUtils.isBlank(map)) {
//			StringTokenizer st = new StringTokenizer(map, ";");
//			while (st.hasMoreTokens()) {
//				final String token = StringUtils.trim(st.nextToken());
//			    Matcher mext = EXTENSION_PATTERN.matcher(token);
//			    while (mext.find()) {
//			    	final String file = StringUtils.trim(mext.group(1));
//			    	final String e = StringUtils.trim(mext.group(2));
//			    	if (asset.getPath().equalsIgnoreCase(file)) {
//			    		ext = e; break;
//			    	}
//			    }
//			}
//	    }
//	}
	
	
	private String buildParams() {
		return null;
	}
	
	
}