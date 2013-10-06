/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import pl.com.dbs.reports.api.inner.report.pattern.PatternFactory;


/**
 * Exception thrown if factory couldnt produce pattern.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternFactoryProduceException extends Exception {
	private static final long serialVersionUID = 1068361460625579431L;
	private PatternFactory factory;
	
	public PatternFactoryProduceException(PatternFactory factory) {
		super();
		this.factory = factory;
	}

	public PatternFactory getFactory() {
		return factory;
	}

	
}
