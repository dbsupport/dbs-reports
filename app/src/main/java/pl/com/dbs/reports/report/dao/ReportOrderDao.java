/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.domain.ReportOrder_;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

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
	 * ..find order with given report if all reports are ready...
	 */
	public ReportOrder findAllReady(Report report) {
		IContextDao<ReportOrder> c = new ContextDao<ReportOrder>(em, ReportOrder.class);
		CriteriaBuilder b = c.getBuilder();
		CriteriaQuery<ReportOrder> cq = c.getCriteria();
		Predicate p = b.conjunction();
		Root<ReportOrder> r = c.getRoot();
		
		/**
		 * find order with given report..
		 * 	
		 	select o.id
			from
			tre_order o,
			tre_order_report ro
			where
			ro.order_id = o.id
			and ro.report_id = 292
		 */
		
		p = b.and(p, r.get(ReportOrder_.reports).in(report));
	    cq.where(p);
		
	    ReportOrder order = executeQuerySingle(c);

	    /**
	     * ..all reports are READY?
	     */
	    for (Report rep : order.getReports())
	    	if (!ReportPhaseStatus.READY.equals(rep.getPhase().getStatus())) return null;
	    
	    return order;

	}
	
}
