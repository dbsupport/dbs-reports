/**
 * 
 */
package pl.com.dbs.reports.report.pattern.application;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.inner.report.pattern.Pattern;
import pl.com.dbs.reports.api.inner.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.domain.ReportBuilder;
import pl.com.dbs.reports.report.domain.ReportNameTemplate;


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
	private static final java.util.regex.Pattern ROLE_PATTERN = java.util.regex.Pattern.compile("^[a-zA-z0-9-]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	/**
	 * Validate manifest file.
	 */
	public void validate(Manifest manifest) throws PatternValidationException {
		if (manifest==null) throw new PatternValidationException(Pattern.ATTRIBUTE_SECTION);
		
		final Attributes attrs = manifest.getAttributes(Pattern.ATTRIBUTE_SECTION);
		if (attrs==null) throw new PatternValidationException(Pattern.ATTRIBUTE_SECTION);
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_NAME))||!NAME_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_NAME)).find())
			throw new PatternValidationException(Pattern.ATTRIBUTE_PATTERN_NAME);
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_VERSION))||!VERSION_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_VERSION)).find())
			throw new PatternValidationException(Pattern.ATTRIBUTE_PATTERN_VERSION);
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_AUTHOR))||!AUTHOR_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_AUTHOR)).find())
			throw new PatternValidationException(Pattern.ATTRIBUTE_PATTERN_AUTHOR);
		if (!StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_FACTORY))&&!FACTORY_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_PATTERN_FACTORY)).find())
			throw new PatternValidationException(Pattern.ATTRIBUTE_PATTERN_FACTORY);
		

		/**
		 * Name template
		 */
		validateName(attrs);		
		
		/**
		 * Validate roles
		 */
		validateRoles(attrs);
	}
	
	private void validateRoles(final Attributes attrs) throws PatternValidationException {
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_ROLES))) throw new PatternValidationException(Pattern.ATTRIBUTE_ROLES, "---");
		List<String> roles = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(attrs.getValue(Pattern.ATTRIBUTE_ROLES), ";");
		while (st.hasMoreTokens()) {
			final String token = StringUtils.trim(st.nextToken());
			Matcher m = ROLE_PATTERN.matcher(token);
			if (!m.matches()) throw new PatternValidationException(Pattern.ATTRIBUTE_ROLES, token);
			if (!StringUtils.isBlank(token)) roles.add(token);
		}
		if (roles.isEmpty()) throw new PatternValidationException(Pattern.ATTRIBUTE_ROLES, "---");
	}
	
	private void validateName(final Attributes attrs) throws PatternValidationException {
		if (StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_NAME_TEMPLATE))) return;

		Matcher m = ReportBuilder.VARIABLE_PATTERN.matcher(attrs.getValue(Pattern.ATTRIBUTE_NAME_TEMPLATE));
	    while (m.find()) {
	    	/**
	    	 * Check if variables are fine...
	    	 */
	    	final String variable = StringUtils.trim(m.group(1));
	    	if (ReportNameTemplate.of(variable)==null)
	    		throw new PatternValidationException(Pattern.ATTRIBUTE_NAME_TEMPLATE, variable);
	    }
	}		
	
}
