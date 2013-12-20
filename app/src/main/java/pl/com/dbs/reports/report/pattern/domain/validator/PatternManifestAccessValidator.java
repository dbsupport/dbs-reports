/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;
import pl.com.dbs.reports.security.domain.SessionContext;


/**
 * Validate manifest accesses.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternManifestAccessValidator extends PatternValidator {

	
	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		String accesses = pattern.getManifest().getPatternAttribute(ReportPatternManifest.ATTRIBUTE_ACCESSES);
		if (StringUtils.isBlank(accesses)) return;
		
		StringTokenizer st = new StringTokenizer(accesses, ";");
		while (st.hasMoreTokens()) {
			final String token = StringUtils.trim(st.nextToken());
			Matcher m = Access.NAME_PATTERN.matcher(token);
			if (!m.matches()) throw new PatternValidationException("report.pattern.import.manifest.accesses.incorrect", Arrays.asList(new String[]{token}));
			if (!SessionContext.getProfile().hasAccess(token)) throw new PatternValidationException("report.pattern.import.manifest.current.no.accesses");
		}
	}

	@Override
	public int getOrder() {
		return 9000;
	}
	
}
