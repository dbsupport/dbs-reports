/**
 * 
 */
package pl.com.dbs.reports.report.pattern.web.form;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.dao.PatternFilter;
import pl.com.dbs.reports.support.web.form.AForm;


/**
 * Report patterns form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class PatternListForm extends AForm {
	private static final long serialVersionUID = -4503613041279207128L;
	public static final String KEY = "patternListForm";
	private String value;
	private String access;
	
	private PatternFilter filter = new PatternFilter();
	
	public PatternListForm() {}
	
	@Override
	public void reset() {
		super.reset();
		filter = new PatternFilter();
	}
	
	public void reset(Profile profile) {
		super.reset();
		filter = new PatternFilter().forProfile(profile.getId());
	}

	public PatternFilter getFilter() {
		return filter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
	
}
