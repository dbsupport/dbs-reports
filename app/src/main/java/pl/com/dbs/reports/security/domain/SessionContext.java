/**
 * 
 */
package pl.com.dbs.reports.security.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.com.dbs.reports.profile.domain.Profile;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class SessionContext {
	
	public static Profile getProfile() {
		AuthenticationToken authentication = getAuthentication();
		return authentication != null ? (Profile)authentication.getProfile() : null; 
	}
	
	public static AuthenticationToken getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) return (AuthenticationToken)authentication;
		return null;
	}
}
