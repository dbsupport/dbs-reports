package pl.com.dbs.reports.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * Authentication events listener. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Component(value="authentication.listener")
public class AuthenticationListener implements ApplicationListener<AbstractAuthenticationEvent> {

	final static Logger log = LoggerFactory.getLogger(AuthenticationListener.class);
	
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		UsernamePasswordAuthenticationToken source = (UsernamePasswordAuthenticationToken) event.getSource();
		WebAuthenticationDetails details = (WebAuthenticationDetails)source.getDetails();
		
		if (event instanceof AbstractAuthenticationFailureEvent) {
			String login = (String) source.getPrincipal();
			log.info("AuthenticationFailure login: " + login + "\" ip: \"" + details.getRemoteAddress() + "\"");
		} else if(event instanceof AbstractAuthenticationEvent) {
			User user = (User) source.getPrincipal();
			log.info("Authentication login: " + user.getUsername() + "\" ip: \"" + details.getRemoteAddress() + "\"");
		}
	}

}
