/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.support.db.domain.AEntity;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ProfileRole extends AEntity {
	private static final long serialVersionUID = 4370570750104490857L;
	
	private String name;
	
	/**
	 * Tworzy role na podstawie listy stringow.
	 */
	public static List<ProfileRole> create(List<String> roles) {
		List<ProfileRole> result = new ArrayList<ProfileRole>();
		for (String role : roles) result.add(new ProfileRole(role));
		return result;
	}
	
	public ProfileRole() {/* JPA */}
	
	public ProfileRole(String role) {
		Validate.isTrue(StringUtils.isBlank(role), "Role name is EMPTY!");
		this.name = role;
	}
	
	

}
