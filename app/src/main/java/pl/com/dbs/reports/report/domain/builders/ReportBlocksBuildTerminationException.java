/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import org.springframework.core.NestedRuntimeException;

/**
 * Terminate building report. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBlocksBuildTerminationException extends NestedRuntimeException {
	private static final long serialVersionUID = 800627217419254753L;

	public ReportBlocksBuildTerminationException(String msg) {
		super(msg);
	}	
}
