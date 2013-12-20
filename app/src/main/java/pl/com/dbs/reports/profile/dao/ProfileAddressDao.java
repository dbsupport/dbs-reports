/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.ProfileAddress;
import pl.com.dbs.reports.support.db.dao.ADao;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class ProfileAddressDao extends ADao<ProfileAddress, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
}
