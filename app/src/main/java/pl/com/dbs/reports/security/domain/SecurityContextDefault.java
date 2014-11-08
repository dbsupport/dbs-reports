package pl.com.dbs.reports.security.domain;

import pl.com.dbs.reports.api.security.SecurityContext;

/**
 * Default security context implementation.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class SecurityContextDefault implements SecurityContext {
	private String login;
	private String password;
	
	public SecurityContextDefault(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public String getPassword() {
		return password;
	}
}
