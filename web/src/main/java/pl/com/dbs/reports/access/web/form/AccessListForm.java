/**
 * 
 */
package pl.com.dbs.reports.access.web.form;

import java.io.Serializable;

import pl.com.dbs.reports.access.dao.AccessFilter;

/**
 * Access list form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class AccessListForm implements Serializable {
	private static final long serialVersionUID = 7305640384457352435L;
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
