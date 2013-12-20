/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service;

import java.io.File;

import pl.com.dbs.reports.api.report.pattern.PatternFactoryContext;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface PatternFactoryContextDefault extends PatternFactoryContext {

	/**
	 * Import from file.
	 */
	File getFile();
}
