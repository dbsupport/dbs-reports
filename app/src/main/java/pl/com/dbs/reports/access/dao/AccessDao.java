/**
 * 
 */
package pl.com.dbs.reports.access.dao;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.domain.Access_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Access CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Repository
public class AccessDao extends ADao<Access, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public List<Access> find(AccessFilter filter) {
		IContextDao<Access> context = new ContextDao<Access>(em, Access.class, filter);
        CriteriaBuilder builder = context.getBuilder();
        Root<Access> root = context.getRoot();
        Predicate p = builder.conjunction();

	    if (!StringUtils.isBlank(filter.getName())) {
	    	p = builder.and(p, builder.like(builder.upper(root.<String>get(Access_.name)), "%" + filter.getName().toUpperCase() + "%"));
        }


        context.getCriteria().where(p);
				
		return executeQuery(context);
	}
	
}
