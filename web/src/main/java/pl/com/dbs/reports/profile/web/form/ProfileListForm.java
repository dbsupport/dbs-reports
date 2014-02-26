/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

import pl.com.dbs.reports.profile.dao.ProfilesFilter;

/**
 * Profiles form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileListForm {
	public static final String KEY = "profileListForm";
	private String name;
	private boolean accepted = true;
	
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

	public boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	
}
