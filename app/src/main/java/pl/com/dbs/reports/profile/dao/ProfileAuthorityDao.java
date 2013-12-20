/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class ProfileAuthorityDao extends ADao<Authority, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public List<Authority> find() {
		return executeQuery(new ContextDao<Authority>(em, Authority.class));
	}
}
