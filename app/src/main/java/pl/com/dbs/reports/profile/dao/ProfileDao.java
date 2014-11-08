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
import pl.com.dbs.reports.profile.domain.ProfileAddress_;
import pl.com.dbs.reports.profile.domain.Profile_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

/**
 * Profile CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Repository
public class ProfileDao extends ADao<Profile, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public Profile find(Long id) {
		Validate.notNull(id, "Id is no more!");
		IContextDao<Profile> c = new ContextDao<Profile>(em, Profile.class);
	    
		Predicate p = c.getBuilder().conjunction();
	    
	    p = c.getBuilder().and(c.getBuilder().equal(c.getRoot().get(Profile_.active), 1));
    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Profile_.id), id));
	    
	    c.getCriteria().where(p);
	    List<Profile> profiles = executeQuery(c);
	    if (profiles.size()>1) throw new IllegalStateException("Too many profiles with ID:"+id);
	    
	    return profiles.isEmpty()?null:profiles.get(0);
	}
	
	public Profile findByIdNoMatterWhat(Long id) {
		Validate.notNull(id, "Id is no more!");
		return getEntityManager().find(clazz, id);
	}	
	
	public Profile findByUuid(String uuid) {
		Validate.notEmpty(uuid, "login is no more!");
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Profile> cq = cb.createQuery(Profile.class);
	    final Root<Profile> q = cq.from(Profile.class);
	    
	    Predicate p = cb.conjunction();
	    p = cb.and(p, cb.equal(q.get(Profile_.uuid), uuid));
	    
	    cq.where(p);
		return executeSingleQuery(getEntityManager().createQuery(cq));
	}	
	
	public List<Profile> find(ProfileFilter filter) {
		Validate.notNull(filter, "Filter is no more!");
		Validate.notEmpty(filter.getLogin(), "Login is no more!");
		IContextDao<Profile> c = new ContextDao<Profile>(em, Profile.class, filter);
	    
		Predicate p = c.getBuilder().conjunction();
	    
	    p = c.getBuilder().and(c.getBuilder().equal(c.getRoot().get(Profile_.active), 1));

	    if (filter.getAccepted()!=null) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Profile_.accepted), filter.getAccepted()?1:0));
	    }
	    if (!StringUtils.isBlank(filter.getLogin())) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Profile_.login), filter.getLogin()));
	    }
	    if (!StringUtils.isBlank(filter.getPasswd())) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Profile_.passwd), filter.getPasswd()));
	    }
	    
	    c.getCriteria().where(p);
	    return executeQuery(c);
	}
	
	public List<Profile> find(final ProfilesFilter filter) {
		IContextDao<Profile> c = new ContextDao<Profile>(em, Profile.class, filter);
	    
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Profile_.active), 1));
	    
	    if (!StringUtils.isBlank(filter.getName())) {
	    	Predicate ex1 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Profile_.firstname)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex2 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Profile_.lastname)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex3 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Profile_.description)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex4 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Profile_.login)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex5 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Profile_.email)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex6 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Profile_.phone)), "%"+filter.getName().toUpperCase()+"%");
	    	
	    	Join<Profile, ProfileAddress> a = c.getRoot().join(Profile_.address, JoinType.LEFT);
	    	Predicate ex10 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(ProfileAddress_.street)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex11 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(ProfileAddress_.city)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex12 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(ProfileAddress_.state)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex13 = c.getBuilder().like(c.getBuilder().upper(a.<String>get(ProfileAddress_.zipcode)), "%"+filter.getName().toUpperCase()+"%");
	    	p = c.getBuilder().or(ex1, ex2, ex3, ex4, ex5, ex6, ex10, ex11, ex12, ex13);
	    }
    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Profile_.accepted), filter.isAccepted()?1:0));
	    
	    c.getCriteria().where(p);
	    
	    return executeQuery(c);
	}	
	
}
