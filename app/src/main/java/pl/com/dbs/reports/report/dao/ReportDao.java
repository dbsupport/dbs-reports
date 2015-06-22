/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import com.google.common.collect.Lists;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

/**
 * Reports CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
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
        IContextDao<Report> context = new ContextDao<Report>(em, Report.class);

		//final CriteriaBuilder cb = em.getCriteriaBuilder();
		//final CriteriaQuery<Report> cq = cb.createQuery(Report.class);
	    //final Root<Report> q = cq.from(Report.class);
        final CriteriaBuilder builder = context.getBuilder();
        final Root<Report> root = context.getRoot();
        final CriteriaQuery<Report> criteria = context.getCriteria();
	    Predicate p = builder.equal(root.get(Report_.pattern).get(ReportPattern_.id), pattern.getId());
        criteria.where(p);
		
		return count(context);
	}
	
	public Report findSingle(ReportFilter filter) {
        IContextDao<Report> context = filter2Context(filter);
		return executeQuerySingle(context);
	}
	
	public List<Report> find(final ReportFilter filter) {
		IContextDao<Report> context = filter2Context(filter);

		return executeQuery(context);
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
	 * ..find reports with START/OK phase older than 6h.. 
	 */
	public List<Report> findLost(Integer limit) {
		ReportFilter filter = new ReportFilter(limit);
		filter.getSorter().add(Report_.generationDate.getName(), true);
		
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class, filter);
		
		List<ReportPhaseStatus> phases = Lists.newArrayList(ReportPhaseStatus.START);
		List<ReportStatus> statuses = Lists.newArrayList(ReportStatus.OK);
		Date date = DateTime.now().minusSeconds(60*60*6).toDate();
		
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
	 * find reports with START/OK phase older than 24h.. 
	 */
	public List<Report> findBroken(Integer limit) {
		ReportFilter filter = new ReportFilter(limit);
		filter.getSorter().add(Report_.generationDate.getName(), true);
		
		IContextDao<Report> c = new ContextDao<Report>(em, Report.class);
		
		List<ReportPhaseStatus> phases = Lists.newArrayList(ReportPhaseStatus.START);
		List<ReportStatus> statuses = Lists.newArrayList(ReportStatus.OK);
		Date date = DateTime.now().minusSeconds(60*60*24).toDate();
		
	    Predicate p = c.getBuilder().conjunction();
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.phase).get(ReportPhase_.status).in(phases));
	    p = c.getBuilder().and(p, c.getBuilder().lessThan(c.getRoot().get(Report_.phase).get(ReportPhase_.date), date));
	    p = c.getBuilder().and(p, c.getRoot().get(Report_.status).in(statuses));

//		List<Order> orders = Lists.newArrayList();
//		orders.add(c.getBuilder().desc(c.getRoot().get(Report_.generationDate)));
//		c.getCriteria().orderBy(orders);
	    
	    c.getCriteria().where(p);
		
	    return (List<Report>)executeQuery(c);
	}

    private IContextDao<Report> filter2Context(final ReportFilter filter) {
        IContextDao<Report> context = new ContextDao<Report>(em, Report.class, filter);
        CriteriaBuilder builder = context.getBuilder();
        Root<Report> root = context.getRoot();
        Predicate p = builder.conjunction();

        if (!filter.getPhases().isEmpty()) {
            p = builder.and(p, root.get(Report_.phase).get(ReportPhase_.status).in(filter.getPhases()));
        }
        if (!filter.getStatuses().isEmpty()) {
            p = builder.and(p, root.get(Report_.status).in(filter.getStatuses()));
        }

        if (!filter.getAccesses().isEmpty()) {
            p = builder.and(p, root.get(Report_.pattern).get(ReportPattern_.accesses).in(filter.getAccesses()));
        } else {
            p = builder.and(p, root.get(Report_.pattern).get(ReportPattern_.accesses).isNull());
        }
        if (!StringUtils.isBlank(filter.getName())) {
            Predicate ex1 = builder.like(builder.upper(root.<String>get(Report_.name)), "%" + filter.getName().toUpperCase()+"%");
            Predicate ex2 = builder.like(builder.upper(root.<String>get(Report_.format.getName())), "%" + filter.getName().toUpperCase()+"%");

            Join<Report, Profile> a1 = root.join(Report_.creator, JoinType.LEFT);
            Predicate ex3 = builder.like(builder.upper(a1.<String>get(Profile_.firstname)), "%" + filter.getName().toUpperCase()+"%");
            Predicate ex4 = builder.like(builder.upper(a1.<String>get(Profile_.lastname)), "%" + filter.getName().toUpperCase()+"%");
            Predicate ex5 = builder.like(builder.upper(a1.<String>get(Profile_.description)), "%" + filter.getName().toUpperCase()+"%");
            Predicate ex6 = builder.like(builder.upper(a1.<String>get(Profile_.login)), "%" + filter.getName().toUpperCase()+"%");

            Join<Report, ReportPattern> a2 = root.join(Report_.pattern, JoinType.LEFT);
            Predicate ex7 = builder.like(builder.upper(a2.<String>get(ReportPattern_.name)), "%" + filter.getName().toUpperCase()+"%");
            Predicate ex8 = builder.like(builder.upper(a2.<String>get(ReportPattern_.version)), "%" + filter.getName().toUpperCase()+"%");

            p = builder.or(ex1, ex2, ex3, ex4, ex5, ex6, ex7, ex8);
        }
        if (filter.getId()!=null&&!filter.getId().isEmpty()) {
            p = builder.and(p, root.get(Report_.id).in(filter.getId()));
        }
        if (filter.getProfileId()!=null) {
            p = builder.and(p, builder.equal(root.get(Report_.creator).get(Profile_.id), filter.getProfileId()));
        }

        context.getCriteria().where(p);

        return context;
    }

}
