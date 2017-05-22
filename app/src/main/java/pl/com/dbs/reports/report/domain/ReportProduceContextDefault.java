/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.domain.Profile;

import java.util.List;

/**
 * Default report context production. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public interface ReportProduceContextDefault extends ReportProduceContext {
	Pattern getPattern();
	PatternFormat getFormat();
	List<ReportParameter> getParameters();
	Profile getCreator();
}
