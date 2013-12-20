/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import pl.com.dbs.reports.report.dao.ReportFilter;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportArchivesForm {
	public static final String KEY = "reportArchivesForm";
	private ReportFilter filter = new ReportFilter(5);
	
	public ReportArchivesForm() {}
	
	public ReportFilter getFilter() {
		return filter;
	}

	public void setFilter(ReportFilter filter) {
		this.filter = filter;
	}	
}
