/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import pl.com.dbs.reports.api.report.*;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;

import java.util.List;

/**
 * Interface for report generation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public interface ReportGenerationContext {
	String getName();
	
	PatternFormat getFormat();	
	
    List<List<pl.com.dbs.reports.api.report.ReportParameter>> getParameters();
	
	long getPattern();
}
