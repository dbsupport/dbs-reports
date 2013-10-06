/**
 * 
 */
package pl.com.dbs.reports.support.filter;

import org.apache.commons.lang.Validate;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class Filter {
	private Pager	pager = new Pager();
	private Sorter	sorter = new Sorter();
	
	public boolean isPaging() {
		return getPager() != null && getPager().getPageSize() != Pager.NO_LIMIT;		
	}
	
	public Pager getPager() {
		return pager;
	}
	public Sorter getSorter() {
		return sorter;
	}
	public void setPager(Pager pager) {
		Validate.notNull(pager);
		this.pager = pager;
	}
	public void setSorter(Sorter sorter) {
		Validate.notNull(sorter);
		this.sorter = sorter;
	}
}
