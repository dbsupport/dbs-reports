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

import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.dao.ReportOrderNotificationDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.domain.ReportOrderNotification;

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
	@Autowired private ReportOrderNotificationDao reportOrderNotificationDao;
	


	
	/**
	 * Build notification if ALL reports are READY...
	 */
	@Transactional(timeout=10)
	public void generate(final long id) {
		logger.debug("Report order processing: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		/**
		 * ..find order for report that has ALL reports READY..
		 */
		ReportOrder order = reportOrderDao.findAllReady(report);
		if (order!=null) {
			ReportOrderNotification notification = reportOrderNotificationDao.find(order);
			if (notification==null) {
				notification = new ReportOrderNotification(order);
				reportOrderNotificationDao.create(notification);
			}
		}
	}
	
	/**
	 * ..make notify ALL unnotified..
	 */
	@Transactional(timeout=30)
	public void notiftyAll() {
		for (ReportOrderNotification notification : reportOrderNotificationDao.findAllUnnotified()) {
			notification.notified();
		}
	}
	

	/**
	 * ..get current notifications..
	 */
	public List<ReportOrderNotification> get() {
		return reportOrderNotificationDao.find();
	}
	
}
