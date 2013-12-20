/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfilesFilter extends AFilter<Profile> {
	private static final int DEFAULT_PAGER_SIZE = 10;
	private String name;
	
	public ProfilesFilter() {
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add("name", true);
		getSorter().add("login", true);
	}
	
	public String getName() {
		return name;
	}
	public void putName(String name) {
		this.name = name;
	}

	
}
