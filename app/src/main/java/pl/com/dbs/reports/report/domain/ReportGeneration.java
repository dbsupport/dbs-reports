/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;

import pl.com.dbs.reports.api.report.ReportType;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface ReportGeneration {

	long getPatternId();
	
	Map<String, String> getParams();
	
	ReportType getType();
}
