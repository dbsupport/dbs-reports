/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.File;

import pl.com.dbs.reports.api.report.pattern.PatternProduceContext;

/**
 * Get data from file.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface PatternProduceContextDefault extends PatternProduceContext {

	/**
	 * Import from file.
	 */
	File getFile();
}
