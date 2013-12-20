/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

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

	public long countByPattern(ReportPattern pattern) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Report> cq = cb.createQuery(Report.class);
	    final Root<Report> q = cq.from(Report.class);
	    
	    cq.where(cb.equal(q.get("pattern").get("id"), pattern.getId()));
		
		return count(cq);
	}
	
	public Report findSingle(ReportFilter filter) {
		List<Report> reports = find(filter);
		return reports!=null&&!reports.isEmpty()?reports.get(0):null;
	}
	
	public List<Report> find(final ReportFilter filter) {
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class, filter);
		
	    Predicate p = c.getBuilder().conjunction();
	    
	    if (!filter.getAccesses().isEmpty()) {
	    	p = c.getBuilder().and(p, c.getRoot().get("pattern").get("accesses").in(filter.getAccesses()));
	    } else {
	    	p = c.getBuilder().and(p, c.getRoot().get("pattern").get("accesses").isNull());
	    }
	    if (!StringUtils.isBlank(filter.getName())) {
	    	Predicate ex1 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("name")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex2 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get("format")), "%"+filter.getName().toUpperCase()+"%");
	    	
	    	Join<Report, Profile> a1 = c.getRoot().join("creator", JoinType.LEFT);
	    	Predicate ex3 = c.getBuilder().like(c.getBuilder().upper(a1.<String>get("name")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex4 = c.getBuilder().like(c.getBuilder().upper(a1.<String>get("login")), "%"+filter.getName().toUpperCase()+"%");
	    	
	    	Join<Report, ReportPattern> a2 = c.getRoot().join("pattern", JoinType.LEFT);
	    	Predicate ex5 = c.getBuilder().like(c.getBuilder().upper(a2.<String>get("name")), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex6 = c.getBuilder().like(c.getBuilder().upper(a2.<String>get("version")), "%"+filter.getName().toUpperCase()+"%");

	    	p = c.getBuilder().or(ex1, ex2, ex3, ex4, ex5, ex6);	    	
	    }	    
	    if (filter.getId()!=null) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get("id"), filter.getId()));
	    }
	    
	    c.getCriteria().where(p);
		
		return executeQuery(c);
	}
}
