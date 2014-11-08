/**
 * 
 */
package pl.com.dbs.reports.activedirectory.dao;

import pl.com.dbs.reports.api.activedirectory.ClientActiveDirectoryProfileFilter;
import pl.com.dbs.reports.api.support.db.ClientQueryPager;
import pl.com.dbs.reports.api.support.db.ClientQuerySorter;
import pl.com.dbs.reports.support.filter.Filter;

/**
 * AD filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class ActiveDirectoryFilter extends Filter {
	private static final int DEFAULT_PAGER_SIZE = 10;
	
	private String value;
	
	public ActiveDirectoryFilter() {
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add("number", true);
	}
	
	public ClientActiveDirectoryProfileFilter convert() {
		return (ClientActiveDirectoryProfileFilter)new ClientActiveDirectoryProfileFilter(value)
			.setPager(new ClientQueryPager(getPager().getPageSize())
							.setDataSize(getPager().getDataSize())
							.setPage(getPager().getPage()))
			.setSorter(new ClientQuerySorter(getSorter().getFieldsAsMap()));
	}
	
	public ActiveDirectoryFilter update(ClientActiveDirectoryProfileFilter filter) {
		getPager().setDataSize(filter.getPager().getDataSize());
		getPager().setPage(filter.getPager().getPage());
		return this;
	}

	public String getValue() {
		return value;
	}

	public void putValue(String value) {
		this.value = value;
	}

	
}
