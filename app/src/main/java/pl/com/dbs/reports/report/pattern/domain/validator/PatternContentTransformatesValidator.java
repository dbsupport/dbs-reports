/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;


/**
 * Validate if tehere is everything ok with transformates.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternContentTransformatesValidator extends PatternValidator {

	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		if (pattern.getTransformates().isEmpty()) throw new PatternValidationException("report.pattern.import.no.transformates");
		
		for (PatternTransformate transformate1 : pattern.getTransformates()) {
			for (PatternTransformate transformate2 : pattern.getTransformates()) {
				if (transformate1.getFormat().getFormat().equals(transformate2.getFormat().getFormat()))
					throw new PatternValidationException("report.pattern.import.transformates.same.format", Arrays.asList(new String[] {transformate1.getName(), transformate2.getName()}));
			}
		}
				
	}

	@Override
	public int getOrder() {
		return 8;
	}	
}
