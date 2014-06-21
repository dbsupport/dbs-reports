/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;

/**
 * Interface for report generation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface ReportGenerationContext {
	String getName();
	
	PatternFormat getFormat();	
	
    Map<String, String> getParameters(); 
	
	long getPattern();
}
