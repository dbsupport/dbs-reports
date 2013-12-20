/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pl.com.dbs.reports.support.db.domain.IEntity;
import pl.com.dbs.reports.support.filter.Filter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface IContextDao<T extends IEntity> {
	CriteriaBuilder getBuilder();
	Root<T> getRoot();
	CriteriaQuery<T> getCriteria();
	Filter getFilter(); 
}
