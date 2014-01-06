/**
 * 
 */
package pl.com.dbs.reports.communication.domain;

import java.util.Properties;


/**
 * ! w obecnej wersji nie wymagane !
 * 
 * To setup Mail Sender properties at runtime.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface SenderContext {
	static final String SENDER = "communication.mail.sender";
	
	String getHost();
	
	int getPort();
	
	String getUser();
	
	String getPassword();
	
	Properties getJavaMailProperties();
}
