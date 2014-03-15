/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain.validator;

import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;



/**
 * Validate init SQL.
 * Try to execute its content against client-db.
 * 
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternContentInitSqlValidator extends PatternValidator {
	@Autowired private SqlExecutor<Map<String, String>> executor;

	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		String sql = pattern.getManifest().getPatternAttribute(ReportPatternManifest.ATTRIBUTE_INIT_SQL);
		
		//Cast?
		ReportPattern rpattern = (ReportPattern)pattern;
		
		if (!StringUtils.isBlank(sql)) {
			StringTokenizer st = new StringTokenizer(sql, ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				final byte[] content = rpattern.getInits().get(token);

				if (content==null) throw new PatternValidationException("report.pattern.import.content.validation.error", Arrays.asList(new String[] {token}));
				
				/**
				 * Try execute safetly...
				 */
				try {
					executor.execute(new String(content), null);
				} catch (Exception e) {
					throw new PatternValidationException(e, "report.pattern.import.content.validation.detailed.error", Arrays.asList(new String[] {token, e.getMessage()}));
				}
			}
		}		
	}

	@Override
	public int getOrder() {
		return 5;
	}	
}
