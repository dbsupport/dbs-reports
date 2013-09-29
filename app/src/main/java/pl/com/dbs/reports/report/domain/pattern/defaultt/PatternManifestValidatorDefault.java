/**
 * 
 */
package pl.com.dbs.reports.report.domain.pattern.defaultt;

import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.api.PatternManifest;
import pl.com.dbs.reports.report.api.PatternManifestValidationException;

/**
 * Validate manifest file according to default rules.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.manifest.validator.default")
public class PatternManifestValidatorDefault {
	private static final Pattern NAME_PATTERN = Pattern.compile("^[\\w\\.\\- ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+$",  Pattern.CASE_INSENSITIVE);
	private static final Pattern VERSION_PATTERN = Pattern.compile("^[\\w\\.\\-]+$",  Pattern.CASE_INSENSITIVE);
	private static final Pattern FACTORY_PATTERN = Pattern.compile("^[a-zA-z0-9\\.]*$",  Pattern.CASE_INSENSITIVE);
	
	/**
	 * Validate manifest file.
	 */
	public void validate(Manifest manifest) throws PatternManifestValidationException {
		Attributes attrs = manifest.getAttributes(PatternManifest.SECTION);
		if (attrs==null) throw new PatternManifestValidationException(PatternManifest.SECTION);
		if (StringUtils.isBlank(attrs.getValue(PatternManifest.NAME))||!NAME_PATTERN.matcher(attrs.getValue(PatternManifest.NAME)).find())
			throw new PatternManifestValidationException(PatternManifest.NAME);
		if (StringUtils.isBlank(attrs.getValue(PatternManifest.VERSION))||!VERSION_PATTERN.matcher(attrs.getValue(PatternManifest.VERSION)).find())
			throw new PatternManifestValidationException(PatternManifest.VERSION);
		if (!StringUtils.isBlank(attrs.getValue(PatternManifest.FACTORY))&&!FACTORY_PATTERN.matcher(attrs.getValue(PatternManifest.FACTORY)).find())
			throw new PatternManifestValidationException(PatternManifest.FACTORY);		
	}
}
