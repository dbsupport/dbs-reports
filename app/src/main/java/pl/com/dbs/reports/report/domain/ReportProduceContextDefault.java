/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;

import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.Pattern;

/**
 * Default report context production. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface ReportProduceContextDefault extends ReportProduceContext {
	Pattern getPattern();
	ReportType getFormat();
	Map<String, String> getParameters();
}
