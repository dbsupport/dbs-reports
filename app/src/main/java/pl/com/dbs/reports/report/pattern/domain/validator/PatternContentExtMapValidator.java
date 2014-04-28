/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


/**
 * Validate extensions map.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Order(7)
@Component
public class PatternContentExtMapValidator implements PatternValidator {

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
			    if (!m.matches()) throw new PatternValidationException("report.pattern.import.manifest.field.validation.error", ReportPatternManifest.ATTRIBUTE_EXTENSION_MAP);
			    m.reset();
			    while (m.find()) {
			    	/**
			    	 * Confirm if file exist with given filename...
			    	 */
			    	final String file = StringUtils.trim(m.group(1));
			    	final String engext = StringUtils.trim(m.group(2));
			    	PatternTransformate transformate = Iterables.find(transformates, new Predicate<PatternTransformate>() {
			    		@Override
			    		public boolean apply(PatternTransformate input) {
			    			return file.equalsIgnoreCase(input.getName());
			    		}
			    	}, null);
			    	if (transformate==null) throw new PatternValidationException("report.pattern.import.content.validation.error", Arrays.asList(new String[] {file}));
			    	
				    Matcher mext = ReportPatternManifest.EXTENSION_ENGINE_PATTERN.matcher(engext);
				    mext.reset();
				    while (mext.find()) {
				    	/**
				    	 * i.e. 
				    	 * doc|rtf = format doc, extension rtf
				    	 * |xml = format txt (default), extension xml
				    	 * pdf| = format pdf, pdf (default ext of format)
				    	 */
				    	String eng = StringUtils.trim(mext.group(1));
				    	final ReportType type = StringUtils.isBlank(eng)?ReportType.TXT:ReportType.of(eng);
				    	if (type==null) throw new PatternValidationException("report.pattern.import.content.validation.error", Arrays.asList(new String[] {eng}));
				    }			    	
			    	
			    }
			}
		}		
	}

}
