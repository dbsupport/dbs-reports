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
	private String name;
	private ProfilesFilter filter = new ProfilesFilter();
	
	public ProfileListForm() {}

	public ProfilesFilter getFilter() {
		return filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
