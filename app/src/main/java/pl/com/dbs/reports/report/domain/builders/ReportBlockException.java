/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import org.springframework.core.NestedRuntimeException;

/**
 * Error while inflating/building a block. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ReportBlockException extends NestedRuntimeException {
	private static final long serialVersionUID = 800627217419254753L;

	public ReportBlockException(Throwable e) {
		super("report.block.exception", e);
	}
	
	public ReportBlockException(String msg) {
		super(msg);
	}	
	
	public ReportBlockException(String msg, Throwable e) {
		super(msg, e);
	}
}
