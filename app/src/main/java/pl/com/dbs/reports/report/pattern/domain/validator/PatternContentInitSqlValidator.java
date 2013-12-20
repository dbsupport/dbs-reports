/**
 * 
 */
package pl.com.dbs.reports.report.pattern.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternValidationException;
import pl.com.dbs.reports.api.report.pattern.PatternValidator;
import pl.com.dbs.reports.api.support.db.SqlExecutor;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternManifest;


/**
 * Validate init SQL.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service
public class PatternContentInitSqlValidator extends PatternValidator {
	@Autowired private SqlExecutor executor;

	@Override
	public void validate(Pattern pattern) throws PatternValidationException {
		String sql = pattern.getManifest().getPatternAttribute(ReportPatternManifest.ATTRIBUTE_INIT_SQL);
		
//		if (!StringUtils.isBlank(sql)) {
//			StringTokenizer st = new StringTokenizer(sql, ";");
//			while (st.hasMoreTokens()) {
//				final String token = StringUtils.trim(st.nextToken());
//				Asset asset = Iterables.find(pattern.getTransformates(), new Predicate<Asset>() {
//					@Override
//					public boolean apply(Asset input) {
//						return token.equalsIgnoreCase(input.getPath());
//					}
//				}, null);
//				if (asset==null) throw new PatternValidationException("report.import.content.validation.error", Arrays.asList(new String[] {token}));
//				
//				/**
//				 * Try execute safetly...
//				 */
//				try {
//					executor.execute(((PatternAsset)asset).getContentAsString());
//				} catch (ClassNotFoundException e) {
//					throw new PatternValidationException(e, "report.import.content.validation.detailed.error", Arrays.asList(new String[] {token, e.getMessage()}));
//				} catch (SQLException e) {
//					throw new PatternValidationException(e,"report.import.content.validation.detailed.error", Arrays.asList(new String[] {token, e.getMessage()}));
//				} catch (SqlExecuteException e) {
//					throw new PatternValidationException(e, "report.import.content.validation.detailed.error", Arrays.asList(new String[] {token, e.getMessage()}));
//				}
//			}
//		}		
	}

	@Override
	public int getOrder() {
		return 6000;
	}	
}
