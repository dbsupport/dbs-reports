/**
 * 
 */
package pl.com.dbs.reports.access.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
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
		IContextDao<Access> c = new ContextDao<Access>(em, Access.class, filter);

	    Predicate p = c.getBuilder().conjunction();

	    if (!StringUtils.isBlank(filter.getName())) {
	    	p = c.getBuilder().and(p, c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("name")), "%"+filter.getName().toUpperCase()+"%"));
	    }		
	    
	    c.getCriteria().where(p);
				
		return executeQuery(c);
	}
	
}
