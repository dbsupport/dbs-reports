/**
 * 
 */
package pl.com.dbs.reports.activedirectory.web.form;

import pl.com.dbs.reports.activedirectory.dao.ActiveDirectoryFilter;

/**
 * AD listing/updating form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ActiveDirectoryListForm {
	public static final String KEY = "activeDirectoryListForm";
	private String value;
	
	private ActiveDirectoryFilter filter = new ActiveDirectoryFilter();
	
	public ActiveDirectoryListForm() {
		
	}
	
	public void reset() {
		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ActiveDirectoryFilter getFilter() {
		return filter;
	}
}
