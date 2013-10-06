/**
 * 
 */
package pl.com.dbs.reports.report.application;

import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.inner.report.Report;
import pl.com.dbs.reports.report.domain.ReportBuilder;
import pl.com.dbs.reports.report.domain.ReportGeneration;
import pl.com.dbs.reports.report.pattern.domain.PatternAsset;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.service")
public class ReportService {

	/**
	 * Build Report object...
	 */
	//public Report generate(ReportGeneration form) {
	public Report generate(ReportPattern pattern) {		
		//ReportPattern pattern = null;
		PatternAsset asset = (PatternAsset)pattern.getAssets().get(0);
		ReportBuilder builder = new ReportBuilder(pattern, asset, null);
		//builder.ext(form.getExt());
		return builder.build();
	}
}
