/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.report.domain.ReportPattern;
import pl.com.dbs.reports.support.db.dao.ADao;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class PatternDao extends ADao<ReportPattern, Long> {
	private static String find = "SELECT trp FROM ReportPattern trp WHERE trp.id = :id";
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<ReportPattern> find(PatternFilter filter) {
		Query query = entityManager.createQuery(find);
		query.setParameter("id", filter.getId());
		
		try {
			return query.getResultList();
		} catch (NoResultException e) {}
		return new ArrayList<ReportPattern>(); 		
	}
}
