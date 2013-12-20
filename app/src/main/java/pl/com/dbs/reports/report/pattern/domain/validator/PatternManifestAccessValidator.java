/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service.validator;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;


/**
 * Validate manifest accesses.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternManifestAccessValidator extends PatternValidator {
	private static final java.util.regex.Pattern ACCESS_PATTERN = java.util.regex.Pattern.compile("^[a-zA-z0-9-]+$",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		String accesses = pattern.getManifest().getPatternAttribute(ReportPatternManifest.ATTRIBUTE_ACCESSES);
		if (StringUtils.isBlank(accesses)) return;
		
		StringTokenizer st = new StringTokenizer(accesses, ";");
		while (st.hasMoreTokens()) {
			final String token = StringUtils.trim(st.nextToken());
			Matcher m = ACCESS_PATTERN.matcher(token);
			if (!m.matches()) throw new PatternValidationException("report.import.manifest.accesses.incorrect", Arrays.asList(new String[]{token}));
		}
	}

	@Override
	public int getOrder() {
		return 9000;
	}
	
}
