/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import java.util.List;

import pl.com.dbs.reports.support.db.domain.AEntity;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class Profile extends AEntity {
	private static final long serialVersionUID = -1140288030193655921L;
	private String firstName;
	private String LastName;
	private ProfileAddress address;
	private List<ProfileRole> roles;

}
