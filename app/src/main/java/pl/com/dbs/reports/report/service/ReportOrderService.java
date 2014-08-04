/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.Iterator;
import java.util.LinkedHashMap;
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
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportGenerationContext;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Reports orders management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Service
public class ReportOrderService {
	private static final Logger logger = Logger.getLogger(ReportOrderService.class);
	@Autowired private ReportDao reportDao;
	@Autowired private ReportOrderDao reportOrderDao;
	@Autowired private ProfileDao profileDao;
	@Autowired private PatternDao patternDao;
	
	/**
	 * ..find orders that have ALL reports PERSISTED and remove them..
	 */
	@Transactional(timeout=10)
	public void cleanupConfirmed() {
		logger.debug("Reports orders checking for confirmed..");
		List<ReportOrder> orders = reportOrderDao.findWithAllReportsConfirmed();
		if (!orders.isEmpty()) 
			reportOrderDao.erase(orders);
	}
	
	/**
	 * ..find order that have ALL reports TRANSIENT/PERSISTED and remove them..
	 */
	@Transactional(timeout=10)
	public void cleanupConfirmed(Report report) {
		logger.debug("Reports order checking for confirmed:"+report.getId());
		ReportOrder order = reportOrderDao.findWithAllReportsConfirmed(report);
		if (order!=null) 
			reportOrderDao.erase(order);
	}	
	
	/**
	 * ..find orders that have ALL reports READY and change its state..
	 */
	@Transactional(timeout=10)
	public void notifi(Profile profile) {
		logger.debug("User was notified about his orders..");
		/**
		 * ..find order for report that has ALL reports READY..
		 */
		List<ReportOrder> orders = reportOrderDao.findUnnotified(profile);
		for (ReportOrder order : orders) {
			order.notified();
		}
	}	

	/**
	 * ..get current user ready orders..
	 */
	public List<ReportOrder> findReady(Profile profile) {
		return reportOrderDao.findReady(profile);
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
		 * ..create reports... multiselect splits..
		 */
		for (Map<String, String> parameters : context.getParameters()) {
			ReportBuilder reportbuilder = new ReportBuilder(context, profile);
			reportbuilder.parameters(parameters);
			reportbuilder.suffix(context.getParameters().size());
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
		private static final int MAX_SUFFIX_LENGTH = 10;
		private static final int MAX_NAME_LENGTH = 20;
		private static final int MAX_EXT_LENGTH = 10;
		
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
			this.inparams = new LinkedHashMap<String, String>();
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
		
		public ReportBuilder suffix(int all) {
			if (all>1&&this.inparams.size()>2) {
				Iterator<Map.Entry<String,String>> iter = this.inparams.entrySet().iterator();
				Map.Entry<String,String> entry = null;
				while (iter.hasNext()) { entry = iter.next(); }
				if (entry!=null) {
					this.suffix = abbreviate(entry.getValue().replaceAll("[^A-Za-z0-9 ]", ""), MAX_SUFFIX_LENGTH); 
				}
			}
			return this;
		}
		
		public ReportBuilder build() {
			for (Map.Entry<String, String> p : this.inparams.entrySet()) 
				logger.debug(p.getKey()+":"+p.getValue());
			
			String fullname = abbreviate(name, MAX_NAME_LENGTH);
			if (!StringUtils.isBlank(this.suffix)) {
				fullname += "-"+this.suffix;
			}
			fullname += "."+abbreviate(format.getReportExtension(), MAX_EXT_LENGTH);
			
			this.report = new Report(pattern, fullname, profile)
					.format(format.getReportType())
					.parameters(inparams);
					
			return this;
		}
		
		public Report getReport() {
			return report;
		}
		
		private String abbreviate(String value, int length) {
			if (value==null) return null;
			value = value.trim();
			if (value.length()<length) return value;
			return value.substring(0, length);
		}

	}	

}
