/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.domain.Report;

/**
 * Reports generation scheduler.
 * Generates if sth stucked.
 * 
 * http://docs.spring.io/spring/docs/3.2.x/spring-framework-reference/html/scheduling.html
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Service
public class ReportProcessingScheduler {
	private static final Logger logger = Logger.getLogger(ReportProcessingScheduler.class);
	@Autowired private ReportDao reportDao;
	@Autowired private ReportProcessingService reportProcessingService;
	@Autowired private ReportOrderService reportOrderService;
	@Autowired private ReportProcessingAsynchService reportProcessingAsynchService;

	
	/**
	 * ..process reports from INIT state...
	 */
	@Scheduled(cron="* */15 * * * ?")//(fixedRate=20000)
	public void generate() {
		List<Report> awaiting = reportDao.findAwaiting(null);
		reportProcessingAsynchService.generate(awaiting);
	}
	
	/**
	 * Try to clean START'ed reports that stucked...
	 */
	@Scheduled(cron="* */20 * * * ?")
	public void regenerate() {
		List<Report> lost = reportDao.findLost(null);
		for (Report report : lost) {
			logger.debug("Report re-processing:"+report.getId());
			reportProcessingService.restart(report.getId());
		}
	}	
	
	/**
	 * find broken ones.. 
	 */
	@Scheduled(cron="0 */25 * * * ?")
	public void cleanup() {
		List<Report> broken = reportDao.findBroken(null);
		for (Report report : broken) {
			logger.debug("Report timeouted:"+report.getId());
			reportProcessingService.timeout(report.getId());
		}
	}		
	
	/**
	 * ..make some notification if reports are ready..
	 * Normally it is done on demand (see public void ready(long id))
	 * but this cron cleans failed tries.. 
	 */
	@Scheduled(cron="0 */5 * * * ?")
	public void notification() {
		reportProcessingService.ready();
	}		
	
	/**
	 * ..cleanup orders that have all reports done.
	 * Normally it is done on demand (see public void cleanupConfirmed(Report report))
	 * but this cron cleans failed tries..
	 * public void cleanupConfirmed(Report report) {
	 */
	//@Scheduled(fixedRate=30000)
	@Scheduled(cron="0 */10 * * * ?")
	public void orders() {
		reportOrderService.cleanupConfirmed();
	}
	
	
}
