/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service.validator;

import java.util.Arrays;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.domain.ReportBuilder;
import pl.com.dbs.reports.report.domain.ReportNameTemplate;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;


/**
 * Validate report template name variables.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternManifestNameTemplateValidator extends PatternValidator {
	
	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		String nametemplate = pattern.getManifest().getPatternAttribute(ReportPatternManifest.ATTRIBUTE_NAME_TEMPLATE);
		if (StringUtils.isBlank(nametemplate)) return;

		Matcher m = ReportBuilder.VARIABLE_PATTERN.matcher(nametemplate);
	    while (m.find()) {
	    	/**
	    	 * Check if variables are fine...
	    	 */
	    	final String variable = StringUtils.trim(m.group(1));
	    	if (ReportNameTemplate.of(variable)==null)
	    		throw new PatternValidationException("report.import.manifest.field.validation.detailed.error", Arrays.asList(new String[] {ReportPatternManifest.ATTRIBUTE_NAME_TEMPLATE, variable}));
	    }
	}

	@Override
	public int getOrder() {
		return 10000;
	}		
	
}
