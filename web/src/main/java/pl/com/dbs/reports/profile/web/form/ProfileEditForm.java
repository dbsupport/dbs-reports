/**
 * 
 */
package pl.com.dbs.reports.profile.web.form;

/**
 * @author krzysztof.kaziura@gmail.com
 * 
 **/
public class ProfileEditForm {
	public static final String KEY = "profileEditForm";
	
	private String firstName;
	
	public ProfileEditForm() {}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
