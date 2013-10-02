/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import pl.com.dbs.reports.report.domain.ReportPattern;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternFilter extends AFilter<ReportPattern> {
	private Long id;
	
	public PatternFilter(long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
}
