/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import pl.com.dbs.reports.report.pattern.dao.PatternFilter;
import pl.com.dbs.reports.support.web.form.AForm;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternListForm extends AForm {
	public static final String KEY = "reportListForm";
	private PatternFilter filter = new PatternFilter(5);
	
	public ReportPatternListForm() {
		
	}

	public PatternFilter getFilter() {
		return filter;
	}

	public void setFilter(PatternFilter filter) {
		this.filter = filter;
	}
	
}
