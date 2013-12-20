/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.pattern.PatternFactory;
import pl.com.dbs.reports.api.report.pattern.PatternFactoryNotFoundException;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Bootstrap logic for Manifest.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component
public class PatternManifestResolver {
	private static final Log logger = LogFactory.getLog(PatternManifestResolver.class);
	@Autowired private List<PatternFactory> factories;

	/**
	 * Get manifest from zip file and according 
	 * to its properties return proper pattern factory.
	 */
	public PatternFactory resolveFactory(final File file) throws PatternFactoryNotFoundException, PatternValidationException, IOException {
		PatternManifestBuilder builder = new PatternManifestBuilder(file);
		builder.build();
		if (builder.getManifest()==null) throw new PatternValidationException("report.pattern.import.manifest.error");
		
		return resolveFactory(builder.getFactory());
	}
	
	/**
	 * Find factory by name.
	 */
	public PatternFactory resolveFactory(final String factory) throws PatternFactoryNotFoundException {
		PatternFactory result = Iterables.find(factories, new Predicate<PatternFactory>() {  
				public boolean apply(PatternFactory f) {  
					return f.getName().equals(factory);
				}  
			}, null);  
		
		if (result==null) {
			logger.warn("Factory NOT found for: "+factory);
			throw new PatternFactoryNotFoundException(factory);
		}
		return result;
	}	
	
}
