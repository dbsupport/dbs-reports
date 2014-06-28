/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportOrder;

/**
 * Reports orders management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Service
public class ReportOrderService {
	private static final Logger logger = Logger.getLogger(ReportOrderService.class);
	@Autowired private ReportDao reportDao;
	@Autowired private ReportOrderDao reportOrderDao;

	
	/**
	 * check if all order's reports are READY.
	 * If so - switch to proper state.. 
	 */
	@Transactional(timeout=10)
	public void ready() {
		logger.debug("Reports orders checking for ready..");
		/**
		 * ..find order for report that has ALL reports READY..
		 */
		List<ReportOrder> orders = reportOrderDao.findWithAllReportsFinished();
		for (ReportOrder order : orders) {
			order.ready();
		}
	}
	
	/**
	 * check if all order's reports of given report are READY.
	 * If so - switch to proper state.. 
	 */
	@Transactional(timeout=10)
	public void ready(long id) {
		logger.debug("Reports orders checking for ready fo report:"+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		
		/**
		 * ..find order for report that has ALL reports finished..
		 */
		ReportOrder order = reportOrderDao.findWithAllReportsFinished(report);
		if (order!=null) {
			order.ready();
		}
	}	
	
	/**
	 * ..find orders that have ALL reports PERSISTED and remove them..
	 */
	@Transactional(timeout=10)
	public void cleanupConfirmed() {
		logger.debug("Reports orders checking for confirmed..");
		List<ReportOrder> orders = reportOrderDao.findWithAllReportsConfirmed();
		if (!orders.isEmpty()) 
			reportOrderDao.erase(orders);
	}
	
	/**
	 * ..find order that have ALL reports TRANSIENT/PERSISTED and remove them..
	 */
	@Transactional(timeout=10)
	public void cleanupConfirmed(Report report) {
		logger.debug("Reports order checking for confirmed:"+report.getId());
		ReportOrder order = reportOrderDao.findWithAllReportsConfirmed(report);
		if (order!=null) 
			reportOrderDao.erase(order);
	}	
	
	/**
	 * ..find orders that have ALL reports READY and change its state..
	 */
	@Transactional(timeout=10)
	public void notifi(Profile profile) {
		logger.debug("User was notified about his orders..");
		/**
		 * ..find order for report that has ALL reports READY..
		 */
		List<ReportOrder> orders = reportOrderDao.findUnnotified(profile);
		for (ReportOrder order : orders) {
			order.notified();
		}
	}	

	/**
	 * ..get current user ready orders..
	 */
	public List<ReportOrder> findReady(Profile profile) {
		return reportOrderDao.findReady(profile);
	}
	
}
