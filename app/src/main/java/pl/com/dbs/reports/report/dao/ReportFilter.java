/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.access.domain.Access;
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
	private static final int DEFAULT_PAGER_SIZE = 10;
	private Long id;
	private String name;
	private List<String> accesses = new ArrayList<String>();
	
	public ReportFilter() {
		Profile profile = SessionContext.getProfile();
		Validate.notNull(profile, "Profile is no more!");
		putAccesses(profile);
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add("generationDate", false);
		getSorter().add("name", true);
		getSorter().add("format", true);
	}
	
	public ReportFilter(long id) {
		this();
		this.id = id;
	}
	
	public void putName(String name) {
		this.name = name;
	}
	
	public void putId(Long id) {
		this.id = id;
	}	
	
	public List<String> getAccesses() {
		return accesses;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void putAccesses(Profile profile) {
		this.accesses = new ArrayList<String>();
		for (Access access : profile.getAccesses())
			this.accesses.add(access.getName());
	}
}
