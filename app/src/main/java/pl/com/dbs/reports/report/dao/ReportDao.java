/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.Profile_;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.Report_;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

/**
 * Reports CRUD.
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
	    
	    Predicate p = cb.equal(q.get(Report_.pattern).get(ReportPattern_.id), pattern.getId());
	    p = cb.and(p, cb.equal(q.get(Report_.temporary), 0));
	    cq.where(p);
		
		return count(cq);
	}
	
	public Report findSingle(ReportFilter filter) {
		List<Report> reports = find(filter);
		return reports!=null&&!reports.isEmpty()?reports.get(0):null;
	}
	
	/**
	 * Returns newest first.
	 */
	public List<Report> findTemporary(final Profile profile) {
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class);
		
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.temporary), 1));
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.creator).get(Profile_.id), profile.getId()));
	    
		List<Order> orders = new ArrayList<Order>();
		orders.add(c.getBuilder().desc(c.getRoot().get(Report_.generationDate)));
		c.getCriteria().orderBy(orders);
	    
	    c.getCriteria().where(p);
		
		return executeQuery(c);
	}
	
	public Report findTemporaryById(final Profile profile, final long id) {
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class);
		
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.id), id));
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.temporary), 1));
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.creator).get(Profile_.id), profile.getId()));
	    
	    c.getCriteria().where(p);
		
		return executeQuerySingle(c);
	}	
	
	public List<Report> find(final ReportFilter filter) {
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class, filter);
		
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.temporary), 0));
	    
	    if (!filter.getAccesses().isEmpty()) {
	    	p = c.getBuilder().and(p, c.getRoot().get(Report_.pattern).get(ReportPattern_.accesses).in(filter.getAccesses()));
	    } else {
	    	p = c.getBuilder().and(p, c.getRoot().get(Report_.pattern).get(ReportPattern_.accesses).isNull());
	    }
	    if (!StringUtils.isBlank(filter.getName())) {
	    	Predicate ex1 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Report_.name)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex2 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Report_.format.toString())), "%"+filter.getName().toUpperCase()+"%");
	    	
	    	Join<Report, Profile> a1 = c.getRoot().join(Report_.creator, JoinType.LEFT);
	    	Predicate ex3 = c.getBuilder().like(c.getBuilder().upper(a1.<String>get(Profile_.firstname)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex4 = c.getBuilder().like(c.getBuilder().upper(a1.<String>get(Profile_.lastname)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex5 = c.getBuilder().like(c.getBuilder().upper(a1.<String>get(Profile_.description)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex6 = c.getBuilder().like(c.getBuilder().upper(a1.<String>get(Profile_.login)), "%"+filter.getName().toUpperCase()+"%");
	    	
	    	Join<Report, ReportPattern> a2 = c.getRoot().join(Report_.pattern, JoinType.LEFT);
	    	Predicate ex7 = c.getBuilder().like(c.getBuilder().upper(a2.<String>get(ReportPattern_.name)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex8 = c.getBuilder().like(c.getBuilder().upper(a2.<String>get(ReportPattern_.version)), "%"+filter.getName().toUpperCase()+"%");

	    	p = c.getBuilder().or(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8);	    	
	    }	    
	    if (filter.getId()!=null) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.id), filter.getId()));
	    }
	    
	    c.getCriteria().where(p);
		
		return executeQuery(c);
	}
}
