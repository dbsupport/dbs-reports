/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.ProfileAccess;
import pl.com.dbs.reports.support.db.dao.ADao;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class ProfileAccessDao extends ADao<ProfileAccess, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public ProfileAccess find(String name) {
		Validate.notEmpty(name, "name is no more!");
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<ProfileAccess> cq = cb.createQuery(ProfileAccess.class);
	    final Root<ProfileAccess> q = cq.from(ProfileAccess.class);
	    
	    cq.where(cb.equal(q.get("name"), name));
		return executeSingleQuery(getEntityManager().createQuery(cq));
	}
	
	
	
}
