/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileAccess;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternFilter extends AFilter<ReportPattern> {
	private Long id;
	private String version;
	private String name;
	private String factory;
	private List<String> accesses = new ArrayList<String>();

	public PatternFilter(String name, String version, String factory) {
		this.name = name;
		this.version = version;
		this.factory = factory;
	}
	
	
	public PatternFilter(int size) {
		getPager().setPageSize(size);
	}
	
	public List<String> getAccesses() {
		return accesses;
	}

	public void setCurrentAccesses() {
		Profile profile = SessionContext.getProfile();
		Validate.notNull(profile, "Profile is no more!");
		this.accesses = new ArrayList<String>();
		for (ProfileAccess access : profile.getAccesses())
			this.accesses.add(access.getName());
	}
	
	public Long getId() {
		return id;
	}


	public String getVersion() {
		return version;
	}


	public String getName() {
		return name;
	}


	public String getFactory() {
		return factory;
	}
}
