/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileEditForm {
	public static final String KEY = "profileEditForm";
	
	private String id;
	private String firstName;
	
	public ProfileEditForm() {}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
