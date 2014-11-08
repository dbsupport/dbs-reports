/**
 * 
 */
package pl.com.dbs.reports.report.web.controller;

/**
 * Dynamic form marshaling exception.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ReportGenerationException extends RuntimeException {
	private static final long serialVersionUID = -6469285788484655669L;

	
	public ReportGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportGenerationException(Throwable cause) {
		super(cause);
	}
}
