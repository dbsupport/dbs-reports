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
public class ReportArchivesForm extends AForm {
	public static final String KEY = "reportArchivesForm";
	private String name;
	private ReportFilter filter = new ReportFilter();
	
	public ReportArchivesForm() {}
	
	public void reset(Long id) {
		super.reset();
		this.filter = new ReportFilter();
		this.filter.putId(id);
	}
	
	public void reset() {
		super.reset();
		this.filter = new ReportFilter();
	}

	public void reset(Profile profile) {
		super.reset();
		this.filter = new ReportFilter();
		this.filter.putProfileId(profile.getId());
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
