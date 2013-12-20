/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import pl.com.dbs.reports.profile.dao.ProfilesFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileListForm {
	public static final String KEY = "profileListForm";
	private ProfilesFilter filter = new ProfilesFilter(20);
	
	public ProfileListForm() {}

	public ProfilesFilter getFilter() {
		return filter;
	}

	public void setFilter(ProfilesFilter filter) {
		this.filter = filter;
	}
	
}
