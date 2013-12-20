/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportFilter extends AFilter<Report> {
	private Long id;
	private List<String> authorities = new ArrayList<String>();
	
	public ReportFilter(int size) {
		getPager().setPageSize(size);
	}
	
	public List<String> getAuthorities() {
		return authorities;
	}

	public void setCurrentAuthorities() {
		Profile profile = SessionContext.getProfile();
		Validate.notNull(profile, "Profile is no more!");
		this.authorities = profile.getAuthoritiesAsString();
	}
	
	public Long getId() {
		return id;
	}
}
