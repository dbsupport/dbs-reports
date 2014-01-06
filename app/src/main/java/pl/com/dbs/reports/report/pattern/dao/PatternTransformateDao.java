/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.report.pattern.domain.ReportPatternTransformate;
import pl.com.dbs.reports.support.db.dao.ADao;


/**
 * Report pattern transformates CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class PatternTransformateDao extends ADao<ReportPatternTransformate, Long> {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}
}
