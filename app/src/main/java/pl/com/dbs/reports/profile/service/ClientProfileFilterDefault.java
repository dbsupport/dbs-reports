/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import pl.com.dbs.reports.api.profile.ClientProfileFilter;

/**
 * Implementation of client side filter for querying profiles.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ClientProfileFilterDefault implements ClientProfileFilter {
	private int max = 10;
	private String uuid;
	
	public ClientProfileFilterDefault(int max) {
		this.max = max;
	}
	
	public ClientProfileFilter next(String uuid) {
		this.uuid = uuid;
		return this;
	}
	
	@Override
	public String getLogin() {
		return null;
	}

	@Override
	public String getPasswd() {
		return null;
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	@Override
	public Integer getMax() {
		return max;
	}
}
