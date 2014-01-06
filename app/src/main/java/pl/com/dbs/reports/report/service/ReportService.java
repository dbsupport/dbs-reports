/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportGeneration;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestResolver;
import pl.com.dbs.reports.security.domain.SessionContext;

import com.google.common.collect.Lists;

/**
 * Reports management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.service")
public class ReportService {
	//private static final Logger logger = Logger.getLogger(ReportService.class);
	public static final int MAX_TEMPORARY_REPORTS = 10;
	@Autowired private ReportDao reportDao;
	@Autowired private ProfileDao profileDao;
	@Autowired private PatternManifestResolver manifestResolver;


	/**
	 * Is there maximum temporary reports for this profile reached? 
	 */
	public boolean exceededsTemporaryReports() {
		return reportDao.findTemporary(SessionContext.getProfile()).size()>=MAX_TEMPORARY_REPORTS;
	}
	
	/**
	 * Remove older temporary reports until number reach MAXIMUM.
	 */
	@Transactional
	public void cleanTemporaryReports() {
		List<Report> reports = Lists.reverse(reportDao.findTemporary(SessionContext.getProfile()));
		Iterator<Report> ir = reports.iterator();
		while (ir.hasNext()&&reports.size()>MAX_TEMPORARY_REPORTS) {
			Report report = ir.next();
			reportDao.erase(report);
			ir.remove();
		}
	}
	
	/**
	 * Build report...
	 */
	@Transactional
	public Report generate(final ReportGeneration context) throws PatternFactoryNotFoundException, ReportValidationException, DataAccessException {
		Validate.notNull(context.getPattern(), "Pattern is no more!");
		
		ReportFactory factory = manifestResolver.resolveFactory(context.getPattern().getFactory()).getReportFactory();
		Validate.notNull(factory, "Report factory is no more!");

		//..create as temporary..
		Report report = (Report)factory.produce(context);
		reportDao.create(report);
		
		//..delete temporary if more than X..
		cleanTemporaryReports();
		
		return report;
	}

	/**
	 * Persist report..
	 */
	@Transactional
	public Report archive(final Long id) {
		Validate.notNull(id, "Report is no more!");
		Report report = reportDao.findTemporaryById(SessionContext.getProfile(), id);
		Validate.notNull(report, "Report is no more!");
		report.archive();
		return report;
	}
	
	/**
	 * Erease report..
	 */
	@Transactional
	public void delete(final long id) {
		Report report = findNoMatterWhat(id);
		reportDao.erase(report);
	}
	
	
	/**
	 * Find ARCHIVED by filter.
	 */
	public List<? extends Report> find(ReportFilter filter) {
		return reportDao.find(filter);
	}

	/**
	 * Find ARCHIVED by id
	 */
	public Report find(long id) {
		ReportFilter filter = new ReportFilter(id);
		return reportDao.findSingle(filter);
	}
	
	/**
	 * Find ARCHIVED or NOT
	 */
	public Report findNoMatterWhat(long id) {
		Report report = find(id);
		if (report==null) report = findTemporary(id);
		return report;
	}	
	
	

	/**
	 * Get temporary archives for this profile..
	 */
	public List<? extends Report> findTemporary() {
		return reportDao.findTemporary(SessionContext.getProfile());
	}

	/**
	 * Get temporary by id for this profile
	 */
	public Report findTemporary(Long id) {
		return reportDao.findTemporaryById(SessionContext.getProfile(), id);
	}		
	
}
