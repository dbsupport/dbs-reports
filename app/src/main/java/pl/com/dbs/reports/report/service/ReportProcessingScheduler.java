/**
 * 
 */
package pl.com.dbs.reports.report.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.dao.ReportDao;

/**
 * Reports generation scheduler.
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
	
	/**
	 * ..process reports from INIT state...
	 */
	@Scheduled(fixedRate=20000)// (cron="0 */1 * * * ?")
	public void generate() {
		/**
		 * ..find oldest generation to process..
		 */
		
		Long id = reportDao.findAwaiting();
		if (id != null) {
			process(id);

		}
	}
	
	
	
	//@Scheduled(cron="0 */2 * * * ?")
	/**
	 * Try to clean START'ed reports that stucked...
	 */
	public void regenerate() {
		/**
		 * ..find oldest generation to process..
		 */
		
		Long id = reportDao.findLost();
		if (id != null) {
			reprocess(id);
		}
	}	
	
	/**
	 * ..make some notification if reports are ready..
	 */
	//@Scheduled(cron="0 */1 * * * ?")
	public void notification() {
		reportOrderService.ready();
	}		

	
	/**
	 * ..cleanup orders..
	 */
	@Scheduled(fixedRate=30000)
	public void orders() {
		reportOrderService.cleanupConfirmed();
	}
	
	
	
	
	
	
	
	private void reprocess(final long id) {
		logger.debug("Report re-processing: "+id);
		reportProcessingService.restart(id);
		reportProcessingService.generate(id);
		reportOrderService.ready(id);
	}
	
	private void process(final long id) {
		logger.debug("Report processing: "+id);
		reportProcessingService.start(id);
		reportProcessingService.generate(id);
		reportOrderService.ready(id);
		
	}
	
}
