/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service.validator;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


/**
 * Validate ext to files.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternContentExtMapValidator extends PatternValidator {

	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		String map = pattern.getManifest().getPatternAttribute(ReportPatternManifest.ATTRIBUTE_EXTENSION_MAP);
		
		if (!StringUtils.isBlank(map)) {
			final List<? extends PatternTransformate> transformates = pattern.getTransformates();
			
			StringTokenizer st = new StringTokenizer(map, ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				/**
				 * Find pairs filename=ext
				 */
			    Matcher m = ReportPatternManifest.EXTENSION_PATTERN.matcher(token);
			    if (!m.matches()) throw new PatternValidationException("report.import.manifest.field.validation.error", ReportPatternManifest.ATTRIBUTE_EXTENSION_MAP);
			    m.reset();
			    while (m.find()) {
			    	/**
			    	 * Confirm if file exist with given filename...
			    	 */
			    	final String file = StringUtils.trim(m.group(1));
			    	final String ext = StringUtils.trim(m.group(2));
			    	PatternTransformate transformate = Iterables.find(transformates, new Predicate<PatternTransformate>() {
			    		@Override
			    		public boolean apply(PatternTransformate input) {
			    			return file.equalsIgnoreCase(input.getName());
			    		}
			    	}, null);
			    	if (transformate==null) throw new PatternValidationException("report.import.content.validation.error", Arrays.asList(new String[] {file}));
			    	if (ReportType.of(ext)==null) throw new PatternValidationException("report.import.content.validation.error", Arrays.asList(new String[] {token}));
			    }
			}
		}		
	}

	@Override
	public int getOrder() {
		return 7000;
	}	
}
