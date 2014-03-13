/**
 * 
 */
package pl.com.dbs.reports.activedirectory.dao;

import pl.com.dbs.reports.support.filter.Filter;

/**
 * AD filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ActiveDirectoryFilter extends Filter {
	private static final int DEFAULT_PAGER_SIZE = 10;
	
	private String value;
	
	public ActiveDirectoryFilter() {
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add("number", true);
		getSorter().add("firstname", true);
		getSorter().add("lastname", true);
		getSorter().add("location", true);
		getSorter().add("dismissal", true);
	}

	public String getValue() {
		return value;
	}

	public void putValue(String value) {
		this.value = value;
	}

	
}
