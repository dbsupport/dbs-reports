/**
 * 
 */
package pl.com.dbs.reports.report.service;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.dao.ReportParameterDao;
import pl.com.dbs.reports.report.domain.*;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;

import java.util.List;

/**
 * Reports orders management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Service
public class ReportOrderService {
	private static final Logger logger = LoggerFactory.getLogger(ReportOrderService.class);
	@Autowired private ReportDao reportDao;
	@Autowired private ReportParameterDao parameterDao;
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
		ReportPattern pattern = patternDao.find(context.getPattern());
		PatternFormat format = context.getFormat();

		for (List<ReportParameter> parameters : context.getParameters()) {
			ReportParameterBuilder pbuilder = ReportParameterBuilder.builder()
					.parameters(parameters);

			Report report = ReportBuilder.builder()
					.format(format)
					.name(context.getName())
					.pattern(pattern)
					.profile(profile)
					.suffix(pbuilder.getSuffix())
					.parameters(pbuilder.build())
					.build();

			parameterDao.create(report.getParametersAsList());
			reportDao.create(report);

			order.add(report);
		}

		return order;
	}	
	
	
	
	




}
