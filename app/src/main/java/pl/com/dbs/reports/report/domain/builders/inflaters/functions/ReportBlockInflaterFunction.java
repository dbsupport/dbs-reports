/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders.inflaters.functions;

import java.util.Map.Entry;

import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;

/**
 * Functions executed while report generation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public interface ReportBlockInflaterFunction {

	boolean supports(final Entry<String, String> param);
	
	void apply(final ReportBlocksBuildContext context, final Entry<String, String> param);
}
