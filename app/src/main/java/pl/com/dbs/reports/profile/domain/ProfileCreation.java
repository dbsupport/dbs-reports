/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.authority.domain.Authority;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Locally creation profile data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public interface ProfileCreation {
	String getLogin();
	String getPasswd();
	String getFirstName();
	String getLastName();
	String getEmail();
	String getPhone();
	Address getAddress();
	File 	getPhoto();
	boolean isAccepted();
	
	List<Authority> getAuthorities();
	Set<Access> getAccesses();
    Set<Long> getGroups();
	
	interface Address {
		String getStreet();
		String getCity();
		String getState();
		String getZipCode();
	}
}
