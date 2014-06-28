/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
	@Autowired private ReportOrderService reportOrderService;


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
		 * ..create order ..
		 */
		Profile profile = profileDao.find(SessionContext.getProfile().getId());
		
		ReportOrder order = new ReportOrder(context.getName(), profile);
		reportOrderDao.create(order);
		
		/**
		 * find multiselect variable..
		 */
		
		/**
		 * ..create reports... multiselect splits..
		 */
		for (int counter=0; counter<context.getParameters().size(); counter++) {
			ReportBuilder reportbuilder = new ReportBuilder(context, profile);
			reportbuilder.parameters(context.getParameters().get(counter));
			if (context.getParameters().size()>1) reportbuilder.suffix("fixme"+String.valueOf(counter));
			Report report = reportbuilder.build().getReport();
			reportDao.create(report);		
			order.add(report);
		}

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
		private String suffix;
		private Map<String, String> inparams;
		private Profile profile;
		private Report report;
		
		public ReportBuilder(ReportGenerationContext context, Profile profile) {
			ReportPattern pattern = patternDao.find(context.getPattern());
			PatternFormat format = context.getFormat();
			String name = context.getName();			
			
			this.pattern = pattern;
			this.format = format;
			this.name = name;
			//..store only input params ..
			this.inparams = new HashMap<String, String>();
			//..add parameters: profile login..
			this.inparams.put(Profile.PARAMETER_USER, profile.getLogin());
			//..add profile parameter (HR authorities)..
			this.inparams.put(Profile.PARAMETER_PROFILE, SessionContext.getProfile().getClientAuthorityMetaData());
			this.profile = profile;
		}
		
		public ReportBuilder parameters(Map<String, String> parameters) {
			this.inparams.putAll(parameters);
			return this;
		}
		
		public ReportBuilder suffix(String suffix) {
			this.suffix = suffix;
			return this;
		}
		
		public ReportBuilder build() {
			for (Map.Entry<String, String> p : this.inparams.entrySet()) 
				logger.debug(p.getKey()+":"+p.getValue());
			
			String fullname = name;
			if (!StringUtils.isBlank(this.suffix))
				fullname += "-"+this.suffix;
			fullname += "."+format.getReportExtension();
			
			this.report = new Report(pattern, fullname, profile)
					.format(format.getReportType())
					.parameters(inparams);
					
			return this;
		}
		
		public Report getReport() {
			return report;
		}
	}	
}
