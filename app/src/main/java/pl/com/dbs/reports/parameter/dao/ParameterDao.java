/**
 * 
 */
package pl.com.dbs.reports.parameter.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.parameter.domain.Parameter;
import pl.com.dbs.reports.parameter.domain.Parameter_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

/**
 * Parameter CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Repository
public class ParameterDao extends ADao<Parameter, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public Parameter find(final String key) {
		Validate.notEmpty(key, "key is no more!");
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Parameter> cq = cb.createQuery(Parameter.class);
	    final Root<Parameter> q = cq.from(Parameter.class);
	    
	    cq.where(cb.and(cb.equal(q.get(Parameter_.key), key)));
		return executeSingleQuery(getEntityManager().createQuery(cq));
	}
	
	public List<Parameter> find(ParameterFilter filter) {
		IContextDao<Parameter> c = new ContextDao<Parameter>(em, Parameter.class, filter);

		Predicate p = c.getBuilder().conjunction();
		
		if (filter.getScope()!=null) {
			p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Parameter_.scope), filter.getScope()));
		}

	    c.getCriteria().where(p);
		return executeQuery(c);
	}
	
}
