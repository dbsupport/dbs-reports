/**
 * 
 */
package pl.com.dbs.reports.access.web.form;

import pl.com.dbs.reports.access.dao.AccessFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class AccessListForm {
	public static final String KEY = "accessListForm";
	private String name;
	private AccessFilter filter = new AccessFilter();
	
	public AccessListForm() {}

	public AccessFilter getFilter() {
		return filter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
