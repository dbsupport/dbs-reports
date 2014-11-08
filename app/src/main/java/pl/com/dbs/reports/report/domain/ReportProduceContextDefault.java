/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;

import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;

/**
 * Default report context production. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public interface ReportProduceContextDefault extends ReportProduceContext {
	Pattern getPattern();
	PatternFormat getFormat();
	Map<String, String> getParameters();
}
