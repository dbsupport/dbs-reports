/**
 * 
 */
package pl.com.dbs.reports.report.domain.inflation.rules;

import org.springframework.core.Ordered;

/**
 * Rule to apply while inflating a block.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public interface ReportBlockRule extends Ordered {
	String apply(final String content, final String key, final String value);
}
