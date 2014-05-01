/**
 * 
 */
package pl.com.dbs.reports.report.domain;

/**
 * Error while inflating/building a block. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBlockException extends Exception {
	private static final long serialVersionUID = 800627217419254753L;

	public ReportBlockException(Exception e) {
		super(e);
	}
	
	public ReportBlockException(String msg) {
		super(msg);
	}
}
