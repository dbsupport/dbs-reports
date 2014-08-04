/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

/**
 * Error while inflating a block. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBlockInflationException extends Exception {
	private static final long serialVersionUID = 1322825716797007499L;
	
	public ReportBlockInflationException(String msg) {
		super(msg);
	}
}
