/**
 * 
 */
package pl.com.dbs.reports.security.domain;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import pl.com.dbs.reports.profile.domain.Profile;

/**
 * Extra custom data for Spring user.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class AuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = -8551936501903237500L;
	private Profile profile;
	
	public AuthenticationToken(Profile profile, String passwd) {
		super(new User(profile.getLogin(), passwd, profile.getAuthorities()), passwd, profile.getAuthorities());
		this.profile = profile;
	}

	public Profile getProfile() {
		return profile;
	}
	
}
