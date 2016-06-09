/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import pl.com.dbs.reports.access.domain.Access;

import java.util.List;
import java.util.Set;

/**
 * Profiles group creation interface.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2015
 */
public interface ProfileGroupCreation {
    String getName();
	String getDescription();
	Set<Access> getAccesses();
}
