/**
 * 
 */
package pl.com.dbs.reports.profile.domain;

import pl.com.dbs.reports.api.profile.ClientProfileFilter;


/**
 * Creating external user.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface ClientProfilesFilter extends ClientProfileFilter {
	String getInEncoding();
	String getOutEncoding();
	String getUuid();
}
