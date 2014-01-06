/**
 * 
 */
package pl.com.dbs.reports.authority.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.authority.domain.Authority_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

/**
 * Authorities CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class AuthorityDao extends ADao<Authority, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public Authority findByName(String name) {
		IContextDao<Authority> c = new ContextDao<Authority>(em, Authority.class);

	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().<String>get(Authority_.name), name));
	    c.getCriteria().where(p);
				
		List<Authority> authorities =  executeQuery(c);
		if (authorities.size()>1) throw new IllegalStateException("Too many authorities found by name!");
		
		return authorities.isEmpty()?null:authorities.get(0);
	}
	
}
