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
	
	public ProfilesFilter(int size) {
		getPager().setPageSize(size);
		getSorter().add("name", true);
	}

}
