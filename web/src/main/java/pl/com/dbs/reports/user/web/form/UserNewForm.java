/**
 * 
 */
package pl.com.dbs.reports.user.web.form;

/**
 * @author krzysztof.kaziura@gmail.com
 * 
 **/
public class UserNewForm {
	public static final String KEY = "userNewForm";
	
	private String firstName;
	
	public UserNewForm() {}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
}
