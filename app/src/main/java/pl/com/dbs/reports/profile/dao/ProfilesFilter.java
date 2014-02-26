/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.Profile_;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * Profiles filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfilesFilter extends AFilter<Profile> {
	private static final int DEFAULT_PAGER_SIZE = 10;
	private String name;
	private boolean accepted = true;
	
	public ProfilesFilter() {
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add(Profile_.lastname.getName(), true);
		getSorter().add(Profile_.firstname.getName(), true);
		getSorter().add(Profile_.description.getName(), true);
		getSorter().add(Profile_.login.getName(), true);
	}
	
	public String getName() {
		return name;
	}
	public void putName(String name) {
		this.name = name;
	}

	public boolean isAccepted() {
		return accepted;
	}
	public void putAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
}
