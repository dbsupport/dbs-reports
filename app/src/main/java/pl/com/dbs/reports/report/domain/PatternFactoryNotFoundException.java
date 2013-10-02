/**
 * 
 */
package pl.com.dbs.reports.report.domain;


/**
 * Exception thrown if prerequisites for producing report pattern are wrong (i.e. no factory found).
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternFactoryNotFoundException extends Exception {
	private static final long serialVersionUID = 1068361460625579431L;
	private String factory;
	
	public PatternFactoryNotFoundException(String factory) {
		super();
		this.factory = factory;
	}

	public String getFactory() {
		return factory;
	}

	
}
