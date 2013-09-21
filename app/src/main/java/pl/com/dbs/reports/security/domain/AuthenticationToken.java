package pl.com.dbs.reports.security.domain;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


/**
 * @author krzysztof.kaziura
 *
 */
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private String gsid;
	private String ticket;
	
	public AuthenticationToken(Operator user, String gsid, String ticket) {
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
	
	public Operator getOperator() {
		return (Operator) this.getPrincipal();
	}
}
