/**
 * 
 */
package pl.com.dbs.reports.report.domain.pattern;

import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.report.api.Pattern;
import pl.com.dbs.reports.report.api.PatternManifestValidationException;

/**
 * Validate manifest file according to default rules.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.manifest.validator.default")
public class PatternManifestValidatorDefault {
	private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[\\w\\.\\- ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern VERSION_PATTERN = java.util.regex.Pattern.compile("^[\\w\\.\\-]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern AUTHOR_PATTERN = java.util.regex.Pattern.compile("^[\\w\\.\\-]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern FACTORY_PATTERN = java.util.regex.Pattern.compile("^[a-zA-z0-9\\.]*$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	/**
	 * Validate manifest file.
	 */
	public void validate(Manifest manifest) throws PatternManifestValidationException {
		Attributes attrs = manifest.getAttributes(Pattern.ATTRIBUTE_SECTION);
		if (attrs==null) throw new PatternManifestValidationException(Pattern.ATTRIBUTE_SECTION);
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_NAME))||!NAME_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_NAME)).find())
			throw new PatternManifestValidationException(Pattern.ATTRIBUTE_NAME);
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_VERSION))||!VERSION_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_VERSION)).find())
			throw new PatternManifestValidationException(Pattern.ATTRIBUTE_VERSION);
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_AUTHOR))||!AUTHOR_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_AUTHOR)).find())
			throw new PatternManifestValidationException(Pattern.ATTRIBUTE_AUTHOR);
		if (!StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_FACTORY))&&!FACTORY_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_FACTORY)).find())
			throw new PatternManifestValidationException(Pattern.ATTRIBUTE_FACTORY);		
	}
}
