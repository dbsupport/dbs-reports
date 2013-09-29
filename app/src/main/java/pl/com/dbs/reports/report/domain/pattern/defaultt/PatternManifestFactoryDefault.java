/**
 * 
 */
package pl.com.dbs.reports.report.domain.pattern.defaultt;

import java.util.jar.Manifest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.api.PatternManifestValidationException;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.manifest.factory.default")
public class PatternManifestFactoryDefault {
	private static final Log logger = LogFactory.getLog(PatternManifestFactoryDefault.class);
	
	@Autowired private PatternManifestValidatorDefault manifestValidator;
	
	/**
	 * Validate file.
	 * Create object.
	 */
	public PatternManifestDefault create(Manifest manifest) throws PatternManifestValidationException {
		logger.debug("Creating PatternManifestDefault object.");
		manifestValidator.validate(manifest);
		return new PatternManifestDefault(manifest);
	}
}
