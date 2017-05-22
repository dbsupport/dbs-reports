/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import org.springframework.stereotype.Repository;
import pl.com.dbs.reports.report.domain.ReportParameter;
import pl.com.dbs.reports.support.db.dao.ADao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Reports CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
@Repository
public class ReportParameterDao extends ADao<ReportParameter, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public void create(List<ReportParameter> params) {
		for (ReportParameter param : params) {
			create(param);
		}
	}
}
