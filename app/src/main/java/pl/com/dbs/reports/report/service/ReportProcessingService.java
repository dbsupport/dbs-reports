/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportProduceContextDefault;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestResolver;

/**
 * Reports management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class ReportProcessingService {
	private static final Logger logger = Logger.getLogger(ReportProcessingService.class);
	@Autowired private ReportDao reportDao;
	@Autowired private PatternManifestResolver manifestResolver;

	/**
	 * Build report...
	 */
	@Transactional(timeout=10)
	public Report generate(final long id) {
		logger.debug("Report processing: "+id);
		final Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		
		try {
			ReportFactory factory = manifestResolver.resolveFactory(report.getPattern().getFactory()).getReportFactory();
			Validate.notNull(factory, "Report factory is no more!");

			ReportProduceContext context = new ReportProduceContextDefault() {
					@Override
					public Pattern getPattern() {
						return report.getPattern();
					}
					@Override
					public ReportType getFormat() {
						return report.getFormat();
					}
					@Override
					public Map<String, String> getParameters() {
						return report.getParameters();
					}
				};
			
			report.ready(factory.produce(context));
		} catch (Exception e) {
			report.failure(e);
		}
		
		return report;
	}	
	
	
	/**
	 * Build report...
	 */
	@Transactional(timeout=5)
	public Report start(final long id) {
		logger.debug("Report starting: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		report.start();
		return report;
	}
	
	@Transactional(timeout=5)
	public Report restart(final long id) {
		logger.debug("Report restarting: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		report.restart();
		return report;
	}
	
}
