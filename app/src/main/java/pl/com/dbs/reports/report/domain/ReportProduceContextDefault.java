/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;

import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * Default report context production. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface ReportProduceContextDefault extends ReportProduceContext {
	ReportPattern getPattern();
	
	String getName();
	
	Map<String, String> getParameters();
	
	PatternFormat getFormat();
}
