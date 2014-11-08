/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * Profile filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ProfileFilter extends AFilter<Profile> {
	private static final long serialVersionUID = -8697585651148607505L;
	
	private String login;
	private String passwd;
	private Boolean accepted;
	
	public ProfileFilter(String login, String passwd) {
		this.login = login;
		this.passwd = passwd;
	}
	
	public ProfileFilter accepted() {
		this.accepted = true;
		return this;
	}
	
	public ProfileFilter unaccepted() {
		this.accepted = false;
		return this;		
	}

	public String getLogin() {
		return login;
	}

	public String getPasswd() {
		return passwd;
	}

	public Boolean getAccepted() {
		return accepted;
	}
	
}
