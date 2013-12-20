/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import java.io.File;
import java.util.List;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.authority.domain.Authority;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
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
	
	List<Authority> getAuthorities();
	List<Access> getAccesses();
	
	interface Address {
		String getStreet();
		String getCity();
		String getState();
		String getZipCode();
	}
}
