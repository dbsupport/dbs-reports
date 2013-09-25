package pl.com.dbs.reports.security.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.security.domain.AuthenticationToken;
import pl.com.dbs.reports.security.domain.Operator;

/**
 * Dostawca autentykacji.
 * 
 * @author krzysztof.kaziura@gmail.com
 *
 */
@Component(value="authentication.provider")
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	
	@Autowired 
	private AuthenticationService authenticationService;
	private static final Logger log = Logger.getLogger(AuthenticationProvider.class);
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
			String login = (String) authentication.getPrincipal();
			String password =(String) authentication.getCredentials();
			//String clientIp = ((WebAuthenticationDetails) authentication.getDetails()).getRemoteAddress();
			if (!login.equals("test")&&!password.equals("test"))
				throw new BadCredentialsException("");
			return new AuthenticationToken(new Operator(),"gsid", "ticket");
	    	
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)
		&& authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
