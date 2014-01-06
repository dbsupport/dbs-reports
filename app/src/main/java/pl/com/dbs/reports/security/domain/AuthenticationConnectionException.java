/**
 * 
 */
package pl.com.dbs.reports.security.domain;

import org.springframework.security.core.AuthenticationException;

/**
 * Due to technical problems..
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class AuthenticationConnectionException extends AuthenticationException {
	private static final long serialVersionUID = -6689950459277519917L;

	public AuthenticationConnectionException(String msg) {
		super(msg);
	}

}
