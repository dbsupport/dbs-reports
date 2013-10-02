/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import java.util.List;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface IDaoReader<T extends IEntity, K> {
	/**
	 * Find by id.
	 */
	T find(K id);

	/**
	 * Find by filter.
	 */
	List<T> find(AFilter<T> filter);
}
