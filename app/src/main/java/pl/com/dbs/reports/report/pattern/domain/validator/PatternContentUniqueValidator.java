/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.pattern.dao.PatternDao;
import pl.com.dbs.reports.report.pattern.dao.PatternFilter;


/**
 * Is there already active pattern with that name/version/factory values? 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Order(4)
@Component
public class PatternContentUniqueValidator implements PatternValidator {
	@Autowired private PatternDao patternDao;
	
	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		PatternFilter filter = new PatternFilter(pattern.getName(), pattern.getVersion(), pattern.getFactory());
		if (!patternDao.findExactMatch(filter).isEmpty()) 
			throw new PatternValidationException("report.pattern.import.manifest.not.unique", Arrays.asList(new String[] {pattern.getName(), pattern.getVersion()}));
	}

}
