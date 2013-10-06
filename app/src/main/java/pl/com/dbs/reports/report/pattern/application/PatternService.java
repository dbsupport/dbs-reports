/**
 * 
 */
package pl.com.dbs.reports.report.pattern.application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.api.inner.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.inner.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.application.ReportService;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.dao.PatternFilter;
import pl.com.dbs.reports.report.pattern.domain.PatternFactoryNotFoundException;
import pl.com.dbs.reports.report.pattern.domain.PatternFactoryProduceException;
import pl.com.dbs.reports.report.pattern.domain.PatternManifestNotFoundException;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.service")
public class PatternService {
	private static final Logger logger = Logger.getLogger(PatternService.class);
	@Autowired private PatternManifestResolver manifestResolver;
	@Autowired private PatternDao patternDao;
	
	@Autowired private ReportService reportService;
	
	
	/**
	 * Read manifest and validate...
	 */
	public ReportPattern read(File file) throws IOException, PatternValidationException, PatternManifestNotFoundException, PatternFactoryNotFoundException, PatternFactoryProduceException {
		logger.info("Checking pattern file.");
		/**
		 * Find factory by manifest file ..
		 */
		Manifest manifest = manifestResolver.findManifest(file);
		PatternFactory factory = manifestResolver.resolveFactory(manifest);
		/**
		 * ..create entity..
		 */
		
		ReportPattern pattern = (ReportPattern)factory.produce(file);
		if (pattern==null) throw new PatternFactoryProduceException(factory);
		
		
		//TEST
		Report rep = (Report)reportService.generate(pattern);
		
		return pattern;
	}
	
	
	/**
	 * Import package.
	 */
	@Transactional
	public ReportPattern upload(File file) throws IOException, PatternValidationException, PatternManifestNotFoundException, PatternFactoryNotFoundException, PatternFactoryProduceException {
		logger.info("Uploading pattern file.");
		
		ReportPattern pattern = read(file);
		patternDao.create(pattern);
		
		return pattern;
	}
	
	
	/**
	 * Znajdz paczki wg podanego filtra.
	 */
	public List<ReportPattern> find(PatternFilter filter) {
		return patternDao.find(filter);
	}
}
