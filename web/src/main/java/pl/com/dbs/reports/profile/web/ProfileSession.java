/**
 * 
 */
package pl.com.dbs.reports.profile.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;

import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.security.domain.AuthenticationToken;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Takes care of holding profile into session and spring context;
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ProfileSession {
	public static final String CURRENT_PROFILE = "currentprofile";
	public static final String HELP_FILE = "helpfile";

	/**
	 * Puts profile into spring session context 
	 * and http session but ONLY if this is current session profile.
	 */
	public static void update(Profile profile, HttpServletRequest request) {
		if (SessionContext.getProfile().isSame(profile)) {
			profile.initHRAuthorities(SessionContext.getProfile().getClientAuthorities());
			SecurityContextHolder.getContext().setAuthentication(new AuthenticationToken(profile, "UNDEFINED"));
			request.getSession().setAttribute(CURRENT_PROFILE, profile);
		}
	}
	
	public static void update(Parameter helpfile, HttpServletRequest request) {
		request.getSession().setAttribute(HELP_FILE, helpfile!=null&&helpfile.getValue()!=null);
	}
}
