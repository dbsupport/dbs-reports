/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.support.db.dao.ADao;

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
	
	public List<Profile> find(ProfilesFilter filter) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Profile> cq = cb.createQuery(Profile.class);
	    final Root<Profile> q = cq.from(Profile.class);
	    
	    Predicate p = cb.conjunction();
	    
	    cq.where(p);
		return executeQuery(cq, filter);
	}	
	
}
