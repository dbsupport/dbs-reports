/**
 * 
 */
package pl.com.dbs.reports.report.service;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.dbs.reports.logging.service.LoggingService;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportGenerationContext;
import pl.com.dbs.reports.report.domain.ReportOrder;

import java.util.List;

/**
 * Reports management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Service
public class ReportService {
	//private static final Logger logger = Logger.getLogger(ReportService.class);
	public static final int MAX_UNARCHIVED = 100;
	@Autowired private ReportDao reportDao;
	@Autowired private ReportOrderDao reportOrderDao;
	@Autowired private ReportOrderService reportOrderService;
	@Autowired private ReportProcessingService reportProcessingService;
	@Autowired private ReportProcessingAsynchService reportProcessingAsynchService;
	@Autowired private LoggingService loggingService;

	
	/**
	 * Create reports generation order and start ASYNCHRONOUSLY processing..
	 */
	public ReportOrder order(final ReportGenerationContext context) {
		ReportOrder order = reportOrderService.order(context);
		
		for (Report report : order.getReports()) {
			reportProcessingService.start(report.getId());
		}		
		
		/**
		 * run generation ASYNCHRONOUSLY
		 */
		reportProcessingAsynchService.generate(order.getReports());
		return order;
	}
	
	/**
	 * Persist report of given id..
	 */
	@Transactional
	public Report archive(long id) {
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		report.archive();
		/**
		 * ..remove order if all reports are TRANSIENT/PERSIST...
		 */
		reportOrderService.cleanupConfirmed(report);

		/**
		 * remove all logs for given raport.
		 */
		loggingService.delog(id);
		
		return report;
	}
	
	/**
	 * Erease report
	 */
	@Transactional
	public void delete(final long id) {
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		
		ReportOrder order = reportOrderDao.find(report);
		if (order!=null) {
			order = reportOrderDao.find(order.getId());
			/**
			 * remove report from order..
			 */
			order.remove(report);
			/**
			 * ..erease order if is empty or all reports PERSIST ..
			 */
			if (order.isEmpty()||order.isConfirmed())
				reportOrderDao.erase(order);
		}
		reportDao.erase(report);
		
		/**
		 * remove all logs for given raport.
		 */
		loggingService.delog(id);
	}	
	
	/**
	 * Make report TRANSIENT
	 */
	@Transactional
	public Report confirm(final long id) {
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		report.confirm();
		/**
		 * ..remove order if all reports are TRANSIENT/PERSIST...
		 */
		reportOrderService.cleanupConfirmed(report);		
		return report;
	}		
	
	
	/**
	 * Find reports by filter.
	 */
	public List<Report> find(ReportFilter filter) {
		return reportDao.find(filter);
	}

	/**
	 * Find by filter
	 */
	public Report findSingle(ReportFilter filter) {
		return reportDao.findSingle(filter);
	}	
	
	
	/**
	 * Is there maximum temporary reports for this profile reached? 
	 * Counts if there is more generations (not sinngle reports) than MAX_TEMPORARY_REPORTS.
	 */
	public int countUnarchived() {
		ReportFilter filter = new ReportFilter().unarchived().fine();
		reportDao.find(filter);
		return filter.getPager().getDataSize();
	}
	
}
