package pl.com.dbs.reports.security.domain;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String gsid;
	private String ticket;
	
	public AuthenticationToken(ProfileUser user, String gsid, String ticket) {
		super(user, user.getPassword(), user.getAuthorities());
		this.gsid = gsid;
		this.ticket = ticket;
	}

	public String getGsid() {
		return gsid;
	}

	public String getTicket() {
		return ticket;
	}
	
	public ProfileUser getOperator() {
		return (ProfileUser) this.getPrincipal();
	}
}
