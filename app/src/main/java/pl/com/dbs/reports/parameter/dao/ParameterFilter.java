/**
 * 
 */
package pl.com.dbs.reports.parameter.dao;

import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.parameter.domain.ParameterScope;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * Parameters scope filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ParameterFilter extends AFilter<Parameter> {
	private ParameterScope scope;
	
	public ParameterFilter() {
		getSorter().add("key", true);
	}
	
	public ParameterFilter scope(ParameterScope scope) {
		this.scope = scope;
		return this;
	}

	public ParameterScope getScope() {
		return scope;
	}
	
}
