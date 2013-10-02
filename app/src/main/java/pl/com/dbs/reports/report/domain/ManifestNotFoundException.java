/**
 * 
 */
package pl.com.dbs.reports.report.domain;


/**
 * Exception thrown if prerequisites for producing report pattern are wrong (i.e. no manifest found).
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ManifestNotFoundException extends Exception {
	private static final long serialVersionUID = 8701874169828554493L;

	public ManifestNotFoundException() {
		super();
	}
	
}
