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

import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportGeneration;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestResolver;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.service")
public class ReportService {
	private static final Logger logger = Logger.getLogger(ReportService.class);
	@Autowired private ReportDao reportDao;
	@Autowired private PatternManifestResolver manifestResolver;

	/**
	 * Build report...
	 */
	public Report generate(final ReportGeneration context) throws PatternFactoryNotFoundException, ReportValidationException {
		Validate.notNull(context.getPattern(), "Pattern is no more!");
		
		ReportFactory factory = manifestResolver.resolveFactory(context.getPattern().getFactory()).getReportFactory();
		Validate.notNull(factory, "Report factory is no more!");

		return (Report)factory.produce(context);
	}

	/**
	 * Save report..
	 */
	@Transactional
	public Report archive(final Report report) {
		Validate.notNull(report, "Report is no more!");
		logger.info("Uploading report file.");
		reportDao.create(report);
		return report;
	}
	
	@Transactional
	public void delete(final long id) {
		ReportFilter filter = new ReportFilter(id);
		Report report = reportDao.findSingle(filter);
		reportDao.erase(report);
	}
	
	
	/**
	 * Find by filter.
	 */
	
	public List<? extends Report> find(ReportFilter filter) {
		return reportDao.find(filter);
	}

	/**
	 * Find by id
	 */
	public Report find(long id) {
		ReportFilter filter = new ReportFilter(id);
		return reportDao.findSingle(filter);
	}	
	
}
