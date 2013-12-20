/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.dao.PatternFilter;
import pl.com.dbs.reports.report.pattern.dao.PatternInflaterDao;
import pl.com.dbs.reports.report.pattern.dao.PatternTransformateDao;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternInflater;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternTransformate;
import pl.com.dbs.reports.report.service.ReportService;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.service")
public class PatternService {
	private static final Logger logger = Logger.getLogger(PatternService.class);
	@Autowired private ManifestResolver manifestResolver;
	@Autowired private PatternDao patternDao;
	@Autowired private PatternTransformateDao patternTransformateDao;
	@Autowired private PatternInflaterDao patternInflaterDao;
	
	@Autowired private ReportService reportService;
	
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
		PatternFactoryContextDefault context = new PatternFactoryContextDefault() {
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
		patternDao.create(pattern);
		
		return pattern;
	}
	
	
	/**
	 * Find by filter.
	 */
	public List<ReportPattern> find(PatternFilter filter) {
		filter.setCurrentAccesses();
		return patternDao.find(filter);
	}
	
	/**
	 * Find by id.
	 */
	public ReportPattern find(long id) {
		return patternDao.find(id);
	}	
	
	
}
