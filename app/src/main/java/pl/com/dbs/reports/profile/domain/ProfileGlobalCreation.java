/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import java.util.List;

import pl.com.dbs.reports.api.profile.ClientProfileAuthority;

/**
 * Creating external user.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public interface ProfileGlobalCreation extends ProfileCreation {
	String getUuid();
	String getDescription();
	List<ClientProfileAuthority> getClientAuthorities();
}
