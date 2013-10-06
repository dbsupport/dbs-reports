/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.db.dao.ADao;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Repository
public class PatternDao extends ADao<ReportPattern, Long> {
	//private static String find = "SELECT trp FROM ReportPattern trp WHERE trp.id = :id";
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public List<ReportPattern> find(PatternFilter filter) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<ReportPattern> cq = cb.createQuery(ReportPattern.class);
	    final Root<ReportPattern> q = cq.from(ReportPattern.class);
	    //final TypedQuery<ReportPattern> tq = em.createQuery(cq);
	    
	    if (!filter.getRoles().isEmpty()) {
		    //cq.select(q).where(q.get("roles").in(filter.getRoles()));
		    cq.where(q.get("roles").in(filter.getRoles()));
	    }
	    if (filter.getId()!=null) {
	    	cq.where(cb.equal(q.get("id"), filter.getId()));
//		    ParameterExpression<Long> p = cb.parameter(Long.class);
//		    tq.setParameter(p, filter.getId());
//		    cq.select(q).where(cb.equal(q.get("id"), p));
	    }
		
		return executeQuery(cq, filter);
	}
}
