/**
 * 
 */
package pl.com.dbs.reports.report.application;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.report.api.PatternFactory;
import pl.com.dbs.reports.report.api.PatternManifestValidationException;
import pl.com.dbs.reports.report.dao.PatternDao;
import pl.com.dbs.reports.report.dao.PatternFilter;
import pl.com.dbs.reports.report.domain.ManifestNotFoundException;
import pl.com.dbs.reports.report.domain.ManifestResolver;
import pl.com.dbs.reports.report.domain.PatternFactoryNotFoundException;
import pl.com.dbs.reports.report.domain.ReportPattern;

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
	
	
	/**
	 * Read manifest and validate...
	 */
	public ReportPattern read(File file) throws IOException, PatternManifestValidationException, ManifestNotFoundException, PatternFactoryNotFoundException {
		logger.info("Checking pattern file.");
		/**
		 * Find factory by manifest file ..
		 */
		Manifest manifest = manifestResolver.findManifest(file);
		PatternFactory factory = manifestResolver.resolveFactory(manifest);
		/**
		 * ..create and save entity..
		 */
		return (ReportPattern)factory.produce(file);
	}
	
	
	/**
	 * Import package.
	 */
	@Transactional
	public ReportPattern upload(File file) throws IOException, PatternManifestValidationException, ManifestNotFoundException, PatternFactoryNotFoundException {
		logger.info("Uploading pattern file.");
		
		ReportPattern pattern = read(file);
		patternDao.create(pattern);
		
		return pattern;
	}
	
	
	/**
	 * Znajdz paczki wg podanego filtra.
	 */
	public List<ReportPattern> find(PatternFilter filter) {
		return null;
	}
}
