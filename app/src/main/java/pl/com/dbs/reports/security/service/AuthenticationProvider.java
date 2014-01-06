package pl.com.dbs.reports.security.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.profile.ClientProfile;
import pl.com.dbs.reports.api.security.SecurityAuthenticatinException;
import pl.com.dbs.reports.api.security.SecurityService;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.service.ProfileService;
import pl.com.dbs.reports.security.domain.AuthenticationConnectionException;
import pl.com.dbs.reports.security.domain.AuthenticationToken;
import pl.com.dbs.reports.security.domain.SecurityContextDefault;

/**
 * Authentication provider.
 * - authenticates in local db
 * - if global user authenticates in CLIENT db
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component(value="authentication.provider")
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	private static final Logger logger = Logger.getLogger(AuthenticationProvider.class);
	private ProfileService profileService;
	private SecurityService securityService;
	
	@Autowired 
	public AuthenticationProvider(ProfileService profileService, SecurityService securityService) {
		this.profileService = profileService;
		this.securityService = securityService;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SecurityContextDefault context = new SecurityContextDefault((String) authentication.getPrincipal(), 
																	(String) authentication.getCredentials());
		//..is there a LOCAL user?
		Profile profile = null;
		try {
			profile = profileService.findByLoginAccepted(context.getLogin());
		} catch (ProfileException e) {
			logger.error(e.getMessage(), e);
			throw new BadCredentialsException("authenticate.bad.credentials");
		}
			
		if (profile == null) throw new BadCredentialsException("authenticate.bad.credentials");
		//..is user a GLOBAL user (HR too)?
		if (profile.isGlobal()) {
			//..then authenticate user in client database..
			ClientProfile user = null;
			try {
				//..convert received data to Spring User..
				user = securityService.authenticate(context);
			} catch (SecurityAuthenticatinException e) {
				throw new BadCredentialsException("authenticate.bad.credentials");
			} catch (DataAccessException e) {
				throw new AuthenticationConnectionException(e.getMessage());
			}
			if (user == null) throw new BadCredentialsException("authenticate.bad.credentials");
			if (!user.getId().equals(profile.getUuid())) throw new BadCredentialsException("authenticate.bad.credentials");
			
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
