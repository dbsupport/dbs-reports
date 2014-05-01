/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.Map;

import pl.com.dbs.reports.report.domain.ReportBlockException;


/**
 * Inflates blocks with params.
 * Result is appened to content.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface ReportTextBlockInflater {

	void inflate(final ReportTextBlock root, final Map<String, String> params, final StringBuilder content) throws ReportBlockException;
	
}
