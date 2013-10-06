/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import java.util.ArrayList;
import java.util.List;

import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternFilter extends AFilter<ReportPattern> {
	private Long id;
	private List<String> roles = new ArrayList<String>();
	
	public PatternFilter(int size) {
		getPager().setPageSize(size);
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void addRole(String role) {
		if (!roles.contains(role)) roles.add(role);
	}
	
	public void removeRole(String role) {
		if (roles.contains(role)) roles.remove(role);
	}	

	public Long getId() {
		return id;
	}
}
