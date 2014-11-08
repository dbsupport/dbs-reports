/**
 * 
 */
package pl.com.dbs.reports.support.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

/**
 * For any query.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Repository
public class Dao {

	@PersistenceContext
	private EntityManager em;	
	
	public EntityManager getEntityManager() {
		return em;
	}
	
	public List<?> executeQuery(String sql) {
		return getEntityManager().createNativeQuery(sql).getResultList();
	}
}
