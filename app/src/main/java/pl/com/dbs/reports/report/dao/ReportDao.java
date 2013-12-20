/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	public List<Report> find(ReportFilter filter) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Report> cq = cb.createQuery(Report.class);
	    final Root<Report> q = cq.from(Report.class);
	    //final TypedQuery<ReportPattern> tq = em.createQuery(cq);
	    
	    if (!filter.getAuthorities().isEmpty()) {
		    //cq.where(q.get("authorities").in(filter.getAuthorities()));
	    }
	    if (filter.getId()!=null) {
	    	cq.where(cb.equal(q.get("id"), filter.getId()));
	    }
		
		return executeQuery(cq, filter);
	}
}
