/**
 * 
 */
package pl.com.dbs.reports.security.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.com.dbs.reports.profile.domain.Profile;

/**
 * Application session context to retrieve profile from Spring Security context.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class SessionContext {
	public static final String ROLE_ADMIN = "Admin";
	public static final String ROLE_MANAGEMENT = "Management";
	public static final String ROLE_USER = "User";
	
	public static Profile getProfile() {
		AuthenticationToken authentication = getAuthentication();
		return authentication != null ? (Profile)authentication.getProfile() : null; 
	}

//	public static void updateProfile(Profile profile) {
//		if (isCurrentProfile(profile)) {
//			profile.initHRAuthorities(profile.getClientAuthorities());
//			SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(profile, "UNDEFINED"));
//		}
//	}	
//	
//	public static boolean isCurrentProfile(Profile profile) {
//		return profile!=null?isCurrentProfile(profile.getId()):false;
//	}
//	
//	public static boolean isCurrentProfile(Long id) {
//		return id!=null?id.equals(getProfile().getId()):false;
//	}
	
	public static AuthenticationToken getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) return (AuthenticationToken)authentication;
		return null;
	}
	
	public static boolean hasAnyRole(String... roles) {
		for (GrantedAuthority ga : getAuthentication().getAuthorities()) 
			for (String role : roles)
				if (ga.getAuthority().equals(role)) return true;
		return false;
	}
}
