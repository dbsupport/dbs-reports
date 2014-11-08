/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.domain.Report;

/**
 * Reports processing management.
 * Set -Ddbs.reports.processing.threads.max=... to set threads quantity.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Service
public class ReportProcessingAsynchService {
	private static final Logger logger = LoggerFactory.getLogger(ReportProcessingAsynchService.class);
	private static final int MAX_THREADS_DEFAULT = 10;
	private ExecutorService executors;
	
	@Autowired private ReportProcessingService reportProcessingService;

	public ReportProcessingAsynchService() {
		Integer max = null;
		try {
			max = Integer.valueOf(System.getProperties().getProperty("dbs.reports.processing.threads.max"));
		} catch (Exception e) {
			max = MAX_THREADS_DEFAULT;
		}
		executors = Executors.newFixedThreadPool(max);
	}	
	
	@Async
	public void generate(List<Report> reports) {
		if (reports==null||reports.isEmpty()) return;
		
		/**
		 * Make tasks to execute.
		 */
		List<Callable<Report>> tasks = new ArrayList<Callable<Report>>();
		
		
		for (Report report : reports) {
			tasks.add(new ReportTask(reportProcessingService, report));
		}		
		
        List<Future<Report>> response;
        List<Report> results = new ArrayList<Report>();
        try {
            response = executors.invokeAll(tasks);
            for (Future<Report> report : response) {
                results.add(report.get());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
	}	
}
