/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.Date;
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
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.Profile_;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.Report.ReportStatus;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;
import pl.com.dbs.reports.report.domain.ReportPhase_;
import pl.com.dbs.reports.report.domain.Report_;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

import com.google.common.collect.Lists;

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

	/**
	 * Counts ALL pattern reports.
	 */
	public long countByPattern(ReportPattern pattern) {
		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Report> cq = cb.createQuery(Report.class);
	    final Root<Report> q = cq.from(Report.class);
	    
	    Predicate p = cb.equal(q.get(Report_.pattern).get(ReportPattern_.id), pattern.getId());
	    cq.where(p);
		
		return count(cq);
	}
	
	public Report findSingle(ReportFilter filter) {
		List<Report> reports = find(filter);
		return reports!=null&&!reports.isEmpty()?reports.get(0):null;
	}
	
	public List<Report> find(final ReportFilter filter) {
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class, filter);
		
	    Predicate p = c.getBuilder().conjunction();
	    
	    if (!filter.getPhases().isEmpty()) {
	    	p = c.getBuilder().and(p, c.getRoot().get(Report_.phase).get(ReportPhase_.status).in(filter.getPhases()));
	    }
	    if (!filter.getStatuses().isEmpty()) {
	    	p = c.getBuilder().and(p, c.getRoot().get(Report_.status).in(filter.getStatuses()));
	    }
	    
	    if (!filter.getAccesses().isEmpty()) {
	    	p = c.getBuilder().and(p, c.getRoot().get(Report_.pattern).get(ReportPattern_.accesses).in(filter.getAccesses()));
	    } else {
	    	p = c.getBuilder().and(p, c.getRoot().get(Report_.pattern).get(ReportPattern_.accesses).isNull());
	    }
	    if (!StringUtils.isBlank(filter.getName())) {
	    	Predicate ex1 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Report_.name)), "%"+filter.getName().toUpperCase()+"%");
	    	Predicate ex2 = c.getBuilder().like(c.getBuilder().upper(c.getRoot().<String>get(Report_.format.getName())), "%"+filter.getName().toUpperCase()+"%");
	    	
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
	    if (filter.getId()!=null&&!filter.getId().isEmpty()) {
	    	p = c.getBuilder().and(p, c.getRoot().get(Report_.id).in(filter.getId()));
	    }
	    if (filter.getProfileId()!=null) {
	    	p = c.getBuilder().and(p, c.getBuilder().equal(c.getRoot().get(Report_.creator).get(Profile_.id), filter.getProfileId()));
	    }
	    
	    c.getCriteria().where(p);
		
		return executeQuery(c);
	}
	
	/**
	 * find limit awaiting (generationfate) with INIT phase
	 */
	public List<Report> findAwaiting(Integer limit) {
		ReportFilter filter = new ReportFilter(limit);
		filter.getSorter().add(Report_.generationDate.getName(), true);
		
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class, filter);
		
		List<ReportPhaseStatus> phases = Lists.newArrayList(ReportPhaseStatus.INIT);
		List<ReportStatus> statuses = Lists.newArrayList(ReportStatus.OK);
		
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.phase).get(ReportPhase_.status).in(phases));
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.status).in(statuses));

	    
//		List<Order> orders = Lists.newArrayList();
//		orders.add(c.getBuilder().asc(c.getRoot().get(Report_.generationDate)));
//		c.getCriteria().orderBy(orders);
		
	    c.getCriteria().where(p);
		
	    return (List<Report>)executeQuery(c);
	}	

	/**
	 * ..find oldest with START/OK phase more than 24h.. 
	 */
	public List<Report> findLost(Integer limit) {
		ReportFilter filter = new ReportFilter(limit);
		filter.getSorter().add(Report_.generationDate.getName(), true);
		
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class, filter);
		
		List<ReportPhaseStatus> phases = Lists.newArrayList(ReportPhaseStatus.START);
		List<ReportStatus> statuses = Lists.newArrayList(ReportStatus.OK);
		Date date = DateTime.now().minusSeconds(60*60*24).toDate();
		
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.phase).get(ReportPhase_.status).in(phases));
	    p = c.getBuilder().and(p, c.getBuilder().lessThan(c.getRoot().get(Report_.phase).get(ReportPhase_.date), date));
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.status).in(statuses));

//		List<Order> orders = Lists.newArrayList();
//		orders.add(c.getBuilder().asc(c.getRoot().get(Report_.generationDate)));
//		c.getCriteria().orderBy(orders);
	    
	    c.getCriteria().where(p);
		
	    return (List<Report>)executeQuery(c);
	}
	
	/**
	 * find oldest with START/OK phase more than 48h.. 
	 */
	public List<Report> findBroken(Integer limit) {
		ReportFilter filter = new ReportFilter(limit);
		filter.getSorter().add(Report_.generationDate.getName(), true);
		
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class);
		
		List<ReportPhaseStatus> phases = Lists.newArrayList(ReportPhaseStatus.START);
		List<ReportStatus> statuses = Lists.newArrayList(ReportStatus.OK);
		Date date = DateTime.now().minusSeconds(60*60*24*2).toDate();
		
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.phase).get(ReportPhase_.status).in(phases));
	    p = c.getBuilder().and(p, c.getBuilder().greaterThan(c.getRoot().get(Report_.phase).get(ReportPhase_.date), date));
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.status).in(statuses));

//		List<Order> orders = Lists.newArrayList();
//		orders.add(c.getBuilder().desc(c.getRoot().get(Report_.generationDate)));
//		c.getCriteria().orderBy(orders);
	    
	    c.getCriteria().where(p);
		
	    return (List<Report>)executeQuery(c);
	}

}
