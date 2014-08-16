/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.concurrent.Callable;

import pl.com.dbs.reports.api.report.ReportProduceResult;
import pl.com.dbs.reports.report.domain.Report;

/**
 * Report generation task.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportTask implements Callable<Report> {
	private long report;
	private ReportProcessingService reportProcessingService;
	
	ReportTask(ReportProcessingService reportProcessingService, Report report) {
		this.report = report.getId();
		this.reportProcessingService = reportProcessingService;
	}
	
	@Override
	public Report call() throws Exception {
		ReportProduceResult result = reportProcessingService.generate(report);
		if (result!=null) reportProcessingService.save(result, report);
		return reportProcessingService.ready(report);
	}

}
