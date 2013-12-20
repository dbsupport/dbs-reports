package pl.com.dbs.reports.security.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.security.SecurityAuthenticatinException;
import pl.com.dbs.reports.api.security.SecurityService;
import pl.com.dbs.reports.api.security.SecurityUser;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.security.domain.AuthenticationToken;
import pl.com.dbs.reports.security.domain.SecurityContextDefault;

/**
 * Dostawca autentykacji.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component(value="authentication.provider")
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	@Autowired private ProfileService profileService;
	@Autowired private SecurityService securityService;
	
	//private static final Logger log = Logger.getLogger(AuthenticationProvider.class);
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SecurityContextDefault context = new SecurityContextDefault((String) authentication.getPrincipal(), (String) authentication.getCredentials());
		//..is there a LOCAL user?
		Profile profile = profileService.find(context.getLogin());
			
		if (profile == null) throw new BadCredentialsException("authenticate.bad.credentials");
		//..is user a GLOBAL user (HR too)?
		if (profile.isGlobal()) {
			//..then authenticate user in client database..
			SecurityUser user;
			try {
				//..convert received data to Spring User..
				user = securityService.authenticate(context);
			} catch (SecurityAuthenticatinException e) {
				throw new BadCredentialsException("");
			}
			if (user == null) throw new BadCredentialsException("");
			if (!user.getId().equals(profile.getUuid())) throw new BadCredentialsException("");
			
			profile.initHRAuthorities(user.getAuthorities());
		} else {
			//..check local password..
			if (!context.getPassword().equals(profile.getPasswd()))
				throw new BadCredentialsException("authenticate.bad.credentials");
		}
		
		//..return Spring token..
		return new AuthenticationToken(profile, context.getPassword());
	}
	
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
		&& authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	

}
