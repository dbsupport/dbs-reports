/**
 * 
 */
package pl.com.dbs.reports.profile.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.ProfileNote;
import pl.com.dbs.reports.support.db.dao.ADao;

/**
 * Note CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class ProfileNoteDao extends ADao<ProfileNote, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
}
