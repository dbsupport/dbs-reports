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

import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.domain.ReportOrderNotification;
import pl.com.dbs.reports.report.domain.ReportOrderNotification.PreReportOrderNotificationStatus;
import pl.com.dbs.reports.report.domain.ReportOrderNotification_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

import com.google.common.collect.Lists;

/**
 * notifications CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Repository
public class ReportOrderNotificationDao extends ADao<ReportOrderNotification, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	/**
	 * find order with report...
	 */
	public ReportOrderNotification find(ReportOrder order) {
		IContextDao<ReportOrderNotification> c = new ContextDao<ReportOrderNotification>(em, ReportOrderNotification.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrderNotification> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrderNotification> r = c.getRoot();
		
		
		p = b.and(p, b.equal(r.get(ReportOrderNotification_.order), order));
		
	    cr.where(p);
		
	    return executeQuerySingle(c);
	}

	/**
	 * ..find all notification ordered from newest to oldest..
	 */
	public List<ReportOrderNotification> find() {
		IContextDao<ReportOrderNotification> c = new ContextDao<ReportOrderNotification>(em, ReportOrderNotification.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrderNotification> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrderNotification> r = c.getRoot();

		List<Order> orders = Lists.newArrayList();
		orders.add(b.desc(r.get(ReportOrderNotification_.date)));
		orders.add(b.asc(r.get(ReportOrderNotification_.status)));
		cr.orderBy(orders);
		
	    cr.where(p);
		
	    return executeQuery(c);
	}	

	/**
	 * ..find all UNNOTIFIED ordered from newest to oldest..
	 */
	public List<ReportOrderNotification> findAllUnnotified() {
		IContextDao<ReportOrderNotification> c = new ContextDao<ReportOrderNotification>(em, ReportOrderNotification.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrderNotification> cr = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrderNotification> r = c.getRoot();

		List<PreReportOrderNotificationStatus> statuses = Lists.newArrayList(PreReportOrderNotificationStatus.UNNOTIFIED);
		p = b.and(p, r.get(ReportOrderNotification_.status).in(statuses));
		
		List<Order> orders = Lists.newArrayList();
		orders.add(b.asc(r.get(ReportOrderNotification_.date)));
		cr.orderBy(orders);
		
	    cr.where(p);
		
	    return executeQuery(c);
	}		

}
