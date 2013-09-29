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

import pl.com.dbs.reports.report.api.Pattern;
import pl.com.dbs.reports.report.api.PatternFactory;
import pl.com.dbs.reports.report.api.PatternManifestValidationException;
import pl.com.dbs.reports.report.domain.pattern.FactoryNotFoundException;
import pl.com.dbs.reports.report.domain.pattern.ManifestNotFoundException;
import pl.com.dbs.reports.report.domain.pattern.ManifestResolver;
import pl.com.dbs.reports.report.domain.pattern.ReportPattern;

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
	
	/**
	 * Import package.
	 */
	public Pattern upload(File file) throws IOException, PatternManifestValidationException, ManifestNotFoundException, FactoryNotFoundException {
		logger.info("Uploading pattern file.");
		/**
		 * Find factory by manifest file ..
		 */
		Manifest manifest = manifestResolver.findManifest(file);
		PatternFactory factory = manifestResolver.resolveFactory(manifest);
		/**
		 * ..create and save entity..
		 */
		Pattern pattern = factory.produce(file, manifest);
		
		return pattern;
	}
	
	
	
	
	/**
	 * Znajdz paczki wg podanego filtra.
	 */
	public List<ReportPattern> find(PatternFilter filter) {
		return null;
	}
}
