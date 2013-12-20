/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportBuilder;
import pl.com.dbs.reports.report.domain.ReportGeneration;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.service")
public class ReportService {
	@Autowired private PatternDao patternDao;
	@Autowired private ReportDao reportDao;
	
	/**
	 * Build Report object...
	 */
	@Transactional
	public Report generate(ReportGeneration form) {
		ReportPattern pattern = patternDao.find(form.getPatternId());
		Validate.notNull(pattern, "Pattern is no more!");
		
		ReportBuilder builder = new ReportBuilder(form);
		Report report = builder.construct().getReport();
		reportDao.create(report);
		return report;
	}
	
	/**
	 * Find by filter.
	 */
	
	public List<Report> find(ReportFilter filter) {
		return reportDao.find(filter);
	}
	
}
