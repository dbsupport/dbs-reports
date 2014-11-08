/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportProduceResult;
import pl.com.dbs.reports.api.report.ReportProduceStatus;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.domain.ReportProduceContextDefault;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestResolver;

/**
 * Reports processing management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Slf4j
@Service
public class ReportProcessingService {
	@Autowired private ReportDao reportDao;
	@Autowired private ReportOrderDao reportOrderDao;
	@Autowired private PatternManifestResolver manifestResolver;
	
	/**
	 * Build report...
	 */
	public ReportProduceResult generate(final long id) {
		log.debug("Report processing: "+id);
		final Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		
		ReportProduceResult result = null;
		try {
			ReportFactory factory = manifestResolver.resolveFactory(report.getPattern().getFactory()).getReportFactory();
			Validate.notNull(factory, "Report factory is no more!");

			ReportProduceContext context = new ReportProduceContextDefault() {
					@Override
					public Pattern getPattern() {
						return report.getPattern();
					}
					@Override
					public PatternFormat getFormat() {
						return report.getFormat();
					}
					@Override
					public Map<String, String> getParameters() {
						return report.getParameters();
					}
				};
			result = factory.produce(context);				
		} catch (Exception e) {
			log.error(e.getMessage());
			
			result = new ReportProduceResult() {
				@Override
				public byte[] getContent() {
					return null;
				}

				@Override
				public ReportProduceStatus getStatus() {
					return ReportProduceStatus.FAILURE;
				}
			};
		}
		
		return result;
	}

	/**
	 * 
	 * Save report to data base...
	 */
	@Transactional
	public Report save(final ReportProduceResult result, final long id) {
		log.debug("Report saving: "+id);
		final Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		
		if (ReportProduceStatus.FAILURE.equals(result.getStatus())) {
			//..report failed...
			report.failure();
		} else {
			//..report done...
			report.ready(result.getContent());		
		}
		
		return report;
	}	
	
	
	/**
	 * Mark report as stared (if is in propper state)
	 */
	@Transactional(timeout=10)
	public Report start(final long id) {
		log.debug("Report starting: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		report.start();
		return report;
	}
	
	@Transactional(timeout=10)
	public Report restart(final long id) {
		log.debug("Report restarting: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		report.restart();
		return report;
	}
	
	@Transactional(timeout=10)
	public Report timeout(final long id) {
		log.debug("Report timing out: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		log.error("Raport przerwany ze wzgledu na zbyt dluga generacje!");
		
		report.timeout();
		return report;
	}
	
	/**
	 * check if all order's reports are READY.
	 * If so - switch to proper state.. 
	 */
	@Transactional(timeout=10)
	public void ready() {
		log.debug("Reports orders checking for ready..");
		/**
		 * ..find order for report that has ALL reports READY..
		 */
		List<ReportOrder> orders = reportOrderDao.findWithAllReportsFinished();
		for (ReportOrder order : orders) {
			order.ready();
		}
	}
	
	/**
	 * check if all order's reports of given report are READY.
	 * If so - switch to proper state.. 
	 */
	@Transactional(timeout=10)
	public Report ready(long id) {
		log.debug("Reports orders checking for ready fo report:"+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		
		/**
		 * ..find order for report that has ALL reports finished..
		 */
		ReportOrder order = reportOrderDao.findWithAllReportsFinished(report);
		if (order!=null) {
			order.ready();
		}
		return report;
	}
	
}
