/**
 * 
 */
package pl.com.dbs.reports.report.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.report.ReportFactory;
import pl.com.dbs.reports.api.report.ReportLogType;
import pl.com.dbs.reports.api.report.ReportProduceContext;
import pl.com.dbs.reports.api.report.ReportProduceResult;
import pl.com.dbs.reports.api.report.ReportProduceStatus;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.dao.ReportLogDao;
import pl.com.dbs.reports.report.dao.ReportOrderDao;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportLog;
import pl.com.dbs.reports.report.domain.ReportOrder;
import pl.com.dbs.reports.report.domain.ReportProduceContextDefault;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestResolver;

/**
 * Reports processing management.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class ReportProcessingService {
	private static final Logger logger = Logger.getLogger(ReportProcessingService.class);
	
	@Autowired private ReportDao reportDao;
	@Autowired private ReportLogDao reportLogDao;
	@Autowired private ReportOrderDao reportOrderDao;
	@Autowired private PatternManifestResolver manifestResolver;
	
	/**
	 * Build report...
	 */
	public ReportProduceResult generate(final long id) {
		logger.debug("Report processing: "+id);
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
		} catch (DataAccessException e) {
			logger.warn("Database access error! Report: "+id+" postponed!");
		} catch (Exception e) {
			final List<pl.com.dbs.reports.api.report.ReportLog> logs = new ArrayList<pl.com.dbs.reports.api.report.ReportLog>();
			logs.add(new ReportLog(e.toString()+": "+e.getMessage(), ReportLogType.ERROR));
			
			result = new ReportProduceResult() {
				@Override
				public byte[] getContent() {
					return null;
				}

				@Override
				public List<pl.com.dbs.reports.api.report.ReportLog> getLogs() {
					return logs;
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
		logger.debug("Report saving: "+id);
		final Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");
		
		//..generation logs..
		List<ReportLog> logs = new LinkedList<ReportLog>();
		for (pl.com.dbs.reports.api.report.ReportLog log : result.getLogs()) {
			ReportLog rlog = new ReportLog(log.getMsg(), log.getType());
			logs.add(rlog);
			reportLogDao.create(rlog);
		}		
		
		if (ReportProduceStatus.FAILURE.equals(result.getStatus())) {
			//..report failed...
			report.failure(logs);
		} else {
			//..report done...
			report.ready(result.getContent(), logs);		
		}
		
		return report;
	}	
	
	
	/**
	 * Mark report as stared (if is in propper state)
	 */
	@Transactional(timeout=10)
	public Report start(final long id) {
		logger.debug("Report starting: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		report.start();
		return report;
	}
	
	@Transactional(timeout=10)
	public Report restart(final long id) {
		logger.debug("Report restarting: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		report.restart();
		return report;
	}
	
	@Transactional(timeout=10)
	public Report timeout(final long id) {
		logger.debug("Report timing out: "+id);
		Report report = reportDao.find(id);
		Validate.notNull(report, "Report is no more!");

		ReportLog log = new ReportLog("Raport przerwany ze wzgledu na zbyt dluga generacje!", ReportLogType.ERROR);
		reportLogDao.create(log);
		
		report.timeout(log);
		return report;
	}
	
	/**
	 * check if all order's reports are READY.
	 * If so - switch to proper state.. 
	 */
	@Transactional(timeout=10)
	public void ready() {
		logger.debug("Reports orders checking for ready..");
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
		logger.debug("Reports orders checking for ready fo report:"+id);
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
