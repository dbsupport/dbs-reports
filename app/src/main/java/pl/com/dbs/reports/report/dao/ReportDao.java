/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.support.db.dao.ADao;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class ReportDao extends ADao<Report, Long> {

	@PersistenceContext
	private EntityManager entityManager;	
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

}
