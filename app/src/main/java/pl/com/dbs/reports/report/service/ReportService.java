/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportGenerationContext;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestResolver;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Reports management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class ReportService {
	private static final Logger logger = Logger.getLogger(ReportService.class);
	public static final int MAX_UNARCHIVED = 100;
	@Autowired private ReportDao reportDao;
	@Autowired private ReportOrderDao reportOrderDao;
	@Autowired private PatternDao patternDao;
	@Autowired private ProfileDao profileDao;
	@Autowired private PatternManifestResolver manifestResolver;


	/**
	 * Persist report of given id..
	 */
	@Transactional
	public Report archive(long id) {
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		report.archive();
		return report;
	}
	
	/**
	 * Erease report
	 */
	@Transactional
	public void delete(final long id) {
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		reportDao.erase(report);
		/**
		 * ..erease order if is empty..
		 */
		
		/**
		 * erease notifiaction..
		 */
		
		
	}	
	
	/**
	 * Erease report
	 */
	@Transactional
	public Report confirm(final long id) {
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		report.confirm();
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
	
	
	
	/**
	 * Makes report generation within proper reports seeds.
	 * ReprtScheduler will process them later.
	 */
	@Transactional
	public ReportOrder order(final ReportGenerationContext context) {
		Validate.notNull(context.getPattern(), "Pattern is no more!");
		Validate.notNull(context.getFormat(), "Format is no more!");
		Validate.notNull(context.getFormat().getReportType(), "Report type is no more!");

		/**
		 * ..create generation (group)..
		 */
		ReportOrder order = new ReportOrder(new Date());
		reportOrderDao.create(order);
		
		/**
		 * ..create generations... multiselect splits..
		 */
		ReportBuilder reportbuilder = new ReportBuilder(context);
		Report report = reportbuilder.build().getReport();
		reportDao.create(report);		

		
		/**
		 * ..add to order..
		 */
		order.add(report);

		return order;
	}	
		
	
	
	/**
	 * Report builder.
	 *
	 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
	 * @coptyright (c) 2013
	 */
	public final class ReportBuilder {
		private ReportPattern pattern;
		private PatternFormat format;
		private String name;
		private Map<String, String> inparams;
		private Profile profile;
		private Report report;
		
		public ReportBuilder(ReportGenerationContext context) {
			ReportPattern pattern = patternDao.find(context.getPattern());
			Profile profile = profileDao.find(SessionContext.getProfile().getId());
			
			PatternFormat format = context.getFormat();
			String name = context.getName();			
			
			this.pattern = pattern;
			this.format = format;
			this.name = name;
			//..store only input params ..
			this.inparams = new HashMap<String, String>();
			this.inparams.putAll(context.getParameters());
			//..add parameters: profile login..
			this.inparams.put(Profile.PARAMETER_USER, profile.getLogin());
			//..add profile parameter (HR authorities)..
			this.inparams.put(Profile.PARAMETER_PROFILE, SessionContext.getProfile().getClientAuthorityMetaData());
			this.profile = profile;
		}
		
		public ReportBuilder build() {
			final String fullname = name+"."+format.getReportExtension();
			
			this.report = new Report(pattern, fullname, profile)
					.format(format.getReportType())
					.parameters(inparams);
					
			logger.info("Report with name:"+fullname+" is build!");
			return this;
		}
		
		public Report getReport() {
			return report;
		}
	}	
}
