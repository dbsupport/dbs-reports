/**
 * 
 */
package pl.com.dbs.reports.parameter.dao;

import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ParameterFilter extends AFilter<Parameter> {
	
	public ParameterFilter() {
		getSorter().add("key", true);
	}
	
	
}
