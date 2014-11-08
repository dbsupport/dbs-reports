/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.dao.ReportDao;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.dao.PatternFilter;
import pl.com.dbs.reports.report.pattern.dao.PatternFormDao;
import pl.com.dbs.reports.report.pattern.dao.PatternInflaterDao;
import pl.com.dbs.reports.report.pattern.dao.PatternTransformateDao;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestResolver;
import pl.com.dbs.reports.report.pattern.domain.PatternProduceContextDefault;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternForm;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternInflater;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternTransformate;

/**
 * Report patterns services.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Service("report.pattern.service")
public class PatternService {
	private static final Logger logger = LoggerFactory.getLogger(PatternService.class);
	@Autowired private PatternManifestResolver manifestResolver;
	@Autowired private PatternDao patternDao;
	@Autowired private ReportDao reportDao;
	@Autowired private PatternTransformateDao patternTransformateDao;
	@Autowired private PatternInflaterDao patternInflaterDao;
	@Autowired private PatternFormDao patternFormDao;
	
	/**
	 * Read manifest and validate...
	 */
	public ReportPattern read(final File file) throws PatternValidationException, PatternFactoryNotFoundException, IOException {
		logger.info("Checking pattern file.");
		/**
		 * Find factory by manifest file ..
		 */
		PatternFactory factory = manifestResolver.resolveFactory(file);
		/**
		 * ..create context (source is file)..
		 */
		PatternProduceContextDefault context = new PatternProduceContextDefault() {
			@Override
			public File getFile() {
				return file;
			}
		};
		return (ReportPattern)factory.produce(context);
	}
	
	
	/**
	 * Import package.
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public ReportPattern upload(final File file) throws PatternValidationException, PatternFactoryNotFoundException, IOException {
		logger.info("Uploading pattern file.");

		ReportPattern pattern = read(file);
		for (ReportPatternTransformate t : (List<ReportPatternTransformate>)pattern.getTransformates()) {
			for (ReportPatternInflater i : (List<ReportPatternInflater>)t.getInflaters()) {
				patternInflaterDao.create(i);
			}
			patternTransformateDao.create(t);
		}
		for (ReportPatternForm f : (List<ReportPatternForm>)pattern.getForms()) {
			patternFormDao.create(f);
		}		
		patternDao.create(pattern);
		
		return pattern;
	}

	/**
	 * Deactivate pattern.
	 * If pattern has NO archives - erase it for good.
	 */
	@Transactional
	public void delete(long id) {
		ReportPattern pattern = patternDao.find(id);
		if (reportDao.countByPattern(pattern)>0) pattern.deactivate();
		else  patternDao.erase(pattern);
	}
	
	
	/**
	 * Find by filter.
	 */
	public List<ReportPattern> find(PatternFilter filter) {
		return patternDao.find(filter);
	}
	
	/**
	 * Find by id.
	 */
	public ReportPattern find(long id) {
		PatternFilter filter = new PatternFilter(id);
		return patternDao.findSingle(filter);
	}
}
