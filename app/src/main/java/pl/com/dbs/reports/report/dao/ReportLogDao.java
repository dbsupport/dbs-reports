/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.report.domain.ReportLog;
import pl.com.dbs.reports.support.db.dao.ADao;

/**
 * Reports log CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Repository
public class ReportLogDao extends ADao<ReportLog, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

}
