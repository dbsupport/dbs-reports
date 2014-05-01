/**
 * 
 */
package pl.com.dbs.reports.report.domain.rules;

import java.util.Map;

import org.springframework.core.Ordered;

import pl.com.dbs.reports.report.domain.ReportBlockException;

/**
 * Rule to apply while inflating a block.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface ReportBlockRule extends Ordered {
	StringBuilder apply(final StringBuilder content, final Map<String, String> params) throws ReportBlockException;
}
