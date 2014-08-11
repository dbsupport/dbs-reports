/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.support.web.form.AForm;


/**
 * Report archives form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportsArchivedForm extends AForm {
	private static final long serialVersionUID = 2768961218389914470L;
	public static final String KEY = "reportArchivesForm";
	private String name;
	private ReportFilter filter;
	
	public ReportsArchivedForm() {
		this.filter = new ReportFilter().archived().fine();
	}
	
	public void reset(Long id) {
		this.reset();
		this.filter.onlyFor(id);
	}
	
	public void reset() {
		super.reset();
		this.filter = new ReportFilter().archived();
	}

	public void reset(Profile profile) {
		this.reset();
		this.filter.onlyFor(profile);
	}

	
	public ReportFilter getFilter() {
		return filter;
	}

	public void setFilter(ReportFilter filter) {
		this.filter = filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
