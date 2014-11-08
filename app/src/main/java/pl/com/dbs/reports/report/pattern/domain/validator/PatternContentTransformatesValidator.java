/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.util.Arrays;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;


/**
 * Validate if tehere is everything ok with transformates.
 * There is allowed only one transformate with given extension.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Order(8)
@Component
public class PatternContentTransformatesValidator implements PatternValidator {

	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		if (pattern.getTransformates().isEmpty()) throw new PatternValidationException("report.pattern.import.no.transformates");
		
		for (PatternTransformate transformate1 : pattern.getTransformates()) {
			for (PatternTransformate transformate2 : pattern.getTransformates()) {
				if (transformate1!=transformate2&&
						transformate1.getFormat().getReportExtension().equalsIgnoreCase(transformate2.getFormat().getReportExtension()))
					throw new PatternValidationException("report.pattern.import.transformates.same.format", Arrays.asList(new String[] {transformate1.getFormat().getReportExtension(), transformate1.getName(), transformate2.getName()}));
			}
		}
				
	}
	
}
