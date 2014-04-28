/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternManifest;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;


/**
 * Validate manifest section required fields.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Order(1)
@Component
public class PatternManifestSectionValidator implements PatternValidator {
	private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[\\w\\.\\- ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern VERSION_PATTERN = java.util.regex.Pattern.compile("^[\\w\\.\\-]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern AUTHOR_PATTERN = java.util.regex.Pattern.compile("^[\\w\\.\\-]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern FACTORY_PATTERN = java.util.regex.Pattern.compile("^[a-zA-z0-9\\.]*$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	/**
	 * Validate manifest file.
	 */
	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		PatternManifest manifest = pattern.getManifest();
		if (manifest==null) throw new PatternValidationException("report.pattern.import.manifest.read.error");
		
		if (manifest.getPatternAttributes()==null) throw new PatternValidationException("report.pattern.import.manifest.field.validation.error", ReportPatternManifest.ATTRIBUTE_SECTION);
		
		if (StringUtils.isBlank(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_NAME))||!NAME_PATTERN.matcher(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_NAME)).find())
			throw new PatternValidationException("report.pattern.import.manifest.field.validation.error", ReportPatternManifest.ATTRIBUTE_PATTERN_NAME);
		if (StringUtils.isBlank(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_VERSION))||!VERSION_PATTERN.matcher(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_VERSION)).find())
			throw new PatternValidationException("report.pattern.import.manifest.field.validation.error", ReportPatternManifest.ATTRIBUTE_PATTERN_VERSION);
		if (StringUtils.isBlank(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_AUTHOR))||!AUTHOR_PATTERN.matcher(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_AUTHOR)).find())
			throw new PatternValidationException("report.pattern.import.manifest.field.validation.error", ReportPatternManifest.ATTRIBUTE_PATTERN_AUTHOR);
		if (!StringUtils.isBlank(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_FACTORY))&&!FACTORY_PATTERN.matcher(manifest.getPatternAttribute(ReportPatternManifest.ATTRIBUTE_PATTERN_FACTORY)).find())
			throw new PatternValidationException("report.pattern.import.manifest.field.validation.error", ReportPatternManifest.ATTRIBUTE_PATTERN_FACTORY);
	}
	
}
