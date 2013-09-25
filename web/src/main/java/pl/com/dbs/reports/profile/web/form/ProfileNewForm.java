/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

/**
 * @author krzysztof.kaziura@gmail.com
 * 
 **/
public class ProfileNewForm {
	public static final String KEY = "profileNewForm";
	
	private String firstName;
	
	public ProfileNewForm() {}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
