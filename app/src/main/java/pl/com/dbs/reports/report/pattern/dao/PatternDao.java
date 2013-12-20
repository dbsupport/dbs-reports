/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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
	    
	    Predicate p = cb.conjunction();
	    if (!filter.getAccesses().isEmpty()) {
	    	p = cb.and(p, q.get("accesses").in(filter.getAccesses()));
	    } else {
	    	p = cb.and(p, q.get("accesses").isNull());
	    }
	    if (filter.getName()!=null) {
	    	p = cb.and(p, cb.equal(q.get("name"), filter.getName()));
	    }
	    if (filter.getVersion()!=null) {
	    	p = cb.and(p, cb.equal(q.get("version"), filter.getVersion()));
	    }
	    if (filter.getFactory()!=null) {
	    	p = cb.and(p, cb.equal(q.get("factory"), filter.getFactory()));
	    }
	    if (filter.getId()!=null) {
	    	p = cb.and(p, cb.equal(q.get("id"), filter.getId()));
	    }
	    
	    cq.where(p);
		return executeQuery(cq, filter);
	}
}
