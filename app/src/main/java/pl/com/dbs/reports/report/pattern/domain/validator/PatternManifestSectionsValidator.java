/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;


/**
 * Validate if there is any not supported section.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Order(0)
@Component
public class PatternManifestSectionsValidator implements PatternValidator {
	private static final List<String> SECTIONS = Arrays.asList( ReportPatternManifest.ATTRIBUTE_PATTERN_NAME,
																ReportPatternManifest.ATTRIBUTE_PATTERN_VERSION,
																ReportPatternManifest.ATTRIBUTE_PATTERN_FACTORY,
																ReportPatternManifest.ATTRIBUTE_PATTERN_AUTHOR,
																ReportPatternManifest.ATTRIBUTE_ACCESSES,
																ReportPatternManifest.ATTRIBUTE_INIT_SQL,
																ReportPatternManifest.ATTRIBUTE_EXTENSION_MAP,
																ReportPatternManifest.ATTRIBUTE_NAME_TEMPLATE,
																ReportPatternManifest.ATTRIBUTE_FORM_FILENAME);
	
	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		Attributes attributes = pattern.getManifest().getPatternAttributes();

		for (Object attr : attributes.keySet()) {
			String section = ((Attributes.Name)attr).toString();
			if (!SECTIONS.contains(section)) throw new PatternValidationException("report.pattern.import.manifest.section.unsupported", Arrays.asList(new String[] {section}));
		}
	}

}
