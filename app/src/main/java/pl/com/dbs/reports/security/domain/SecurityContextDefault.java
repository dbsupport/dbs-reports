package pl.com.dbs.reports.security.domain;

import pl.com.dbs.reports.api.security.SecurityContext;
import pl.com.dbs.reports.api.support.db.ConnectionContext;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class SecurityContextDefault implements SecurityContext {
	private String login;
	private String password;
	private ConnectionContext connectionContext;
	
	public SecurityContextDefault(String login, String password, ConnectionContext connectionContext) {
		this.login = login;
		this.password = password;
		this.connectionContext = connectionContext;
	}
	
	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public ConnectionContext getConnectionContext() {
		return connectionContext;
	}

}
