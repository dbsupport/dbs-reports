/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.Profile_;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.domain.ReportOrder.ReportOrderStatus;
import pl.com.dbs.reports.report.domain.ReportOrder_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Reports orders CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Repository
public class ReportOrderDao extends ADao<ReportOrder, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	/**
	 * ..return orders if all reports are confirmed (TRANSIEN/PERSIST)...
	 */
	public List<ReportOrder> findWithAllReportsConfirmed() {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cr = c.getCriteria();
		Predicate p = b.conjunction();
		
	    cr.where(p);

	    /**
	     * check if all reports are >= READY
	     */
	    return Lists.newArrayList(Iterables.filter(executeQuery(c), new com.google.common.base.Predicate<ReportOrder>() {
			@Override
			public boolean apply(ReportOrder input) {
				return input.isConfirmed();
			}
	    }));
	}	
	
	
	/**
	 * ..find order with given report if all reports are confirmed (TRANSIEN/PERSIST)...
	 */
	public ReportOrder findWithAllReportsConfirmed(Report report) {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrder> r = c.getRoot();
		
		List<Report> reports = Lists.newArrayList(report);
		p = b.and(p, r.get(ReportOrder_.reports).in(reports));
	    cr.where(p);
	    
	    ReportOrder order = executeQuerySingle(c);
	    return order!=null&&order.isConfirmed()?order:null;
	}
	
	
	/**
	 * ..find orders if all theirs reports are ready...
	 */
	public List<ReportOrder> findWithAllReportsFinished() {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrder> r = c.getRoot();
		
		List<ReportOrderStatus> statuses = Lists.newArrayList(ReportOrderStatus.NOTREADY);
		p = b.and(p, r.get(ReportOrder_.status).in(statuses));
		
		List<Order> orders = Lists.newArrayList();
		orders.add(b.desc(r.get(ReportOrder_.date)));
		cr.orderBy(orders);
	    cr.where(p);

	    /**
	     * check if all reports are >= READY
	     */
	    return Lists.newArrayList(Iterables.filter(executeQuery(c), new com.google.common.base.Predicate<ReportOrder>() {
			@Override
			public boolean apply(ReportOrder input) {
				return input.isFinished();
			}
	    }));
	}
	
	/**
	 * ..find order with of given report if all reports are ready...
	 */
	public ReportOrder findWithAllReportsFinished(Report report) {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrder> r = c.getRoot();
		
		List<ReportOrderStatus> statuses = Lists.newArrayList(ReportOrderStatus.NOTREADY);
		p = b.and(p, r.get(ReportOrder_.status).in(statuses));
		List<Report> reports = Lists.newArrayList(report);
		p = b.and(p, r.get(ReportOrder_.reports).in(reports));		
		
	    cr.where(p);

	    ReportOrder order = executeQuerySingle(c);
	    return order!=null&&order.isFinished()?order:null;
	}	
	
	/**
	 * ..find UNNOTIFIED/NOTIFIED for given user 
	 * ordered from newest to oldest..
	 */
	public List<ReportOrder> findReady(Profile profile) {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrder> r = c.getRoot();

		
		p = b.and(p, b.equal(r.get(ReportOrder_.creator).get(Profile_.id), profile.getId()));
		
		List<ReportOrderStatus> statuses = Lists.newArrayList(ReportOrderStatus.UNNOTIFIED, ReportOrderStatus.NOTIFIED);
		p = b.and(p, r.get(ReportOrder_.status).in(statuses));
		
		List<Order> orders = Lists.newArrayList();
		orders.add(b.desc(r.get(ReportOrder_.date)));
		cr.orderBy(orders);
		
	    cr.where(p);
		
	    return executeQuery(c);
	}		
	
	/**
	 * ..find UNNOTIFIED for given user.. 
	 */
	public List<ReportOrder> findUnnotified(Profile profile) {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrder> r = c.getRoot();

		p = b.and(p, b.equal(r.get(ReportOrder_.creator).get(Profile_.id), profile.getId()));
		
		List<ReportOrderStatus> statuses = Lists.newArrayList(ReportOrderStatus.UNNOTIFIED);
		p = b.and(p, r.get(ReportOrder_.status).in(statuses));
		
	    cr.where(p);
		
	    return executeQuery(c);
	}
	
	/**
	 * ..find order by report..
	 */
	public ReportOrder find(Report report) {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrder> r = c.getRoot();
		
		List<Report> reports = Lists.newArrayList(report);
		p = b.and(p, r.get(ReportOrder_.reports).in(reports));
	    cr.where(p);
	    
	    return executeQuerySingle(c);
	}
	
	
}
