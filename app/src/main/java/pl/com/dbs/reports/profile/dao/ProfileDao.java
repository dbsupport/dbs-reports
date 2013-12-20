/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileAddress;
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
public class ProfileDao extends ADao<Profile, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public Profile find(String login) {
		Validate.notEmpty(login, "login is no more!");
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Profile> cq = cb.createQuery(Profile.class);
	    final Root<Profile> q = cq.from(Profile.class);
	    
	    cq.where(cb.and(cb.equal(q.get("login"), login)));
		return executeSingleQuery(getEntityManager().createQuery(cq));
	}
	
	public List<Profile> find(final ProfilesFilter filter) {
		IContextDao<Profile> c = new ContextDao<Profile>(em, Profile.class, filter);
	    
	    Predicate p = c.getBuilder().conjunction();
	    
	    if (!StringUtils.isBlank(filter.getName())) {
	    	Predicate ex1 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("name")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex2 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("login")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex3 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("email")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex4 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("phone")), "%"+filter.getName().toUpperCase()+"%");
	    	
	    	Join<Profile, ProfileAddress> a = c.getRoot().join("address", JoinType.LEFT);
	    	Predicate ex5 = c.getBuilder().like(c.getBuilder().upper(a.<String>get("street")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex6 = c.getBuilder().like(c.getBuilder().upper(a.<String>get("city")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex7 = c.getBuilder().like(c.getBuilder().upper(a.<String>get("state")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex8 = c.getBuilder().like(c.getBuilder().upper(a.<String>get("zipcode")), "%"+filter.getName().toUpperCase()+"%");
	    	p = c.getBuilder().or(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8);
	    }
	    
	    c.getCriteria().where(p);
	    
	    return executeQuery(c);
	}	
	
}
