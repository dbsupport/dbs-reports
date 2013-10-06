/**
 * 
 */
package pl.com.dbs.reports.report.pattern.application;

import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.dbs.reports.api.outer.report.ReportExecuteException;
import org.dbs.reports.api.outer.report.ReportExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.api.inner.report.pattern.Pattern;
import pl.com.dbs.reports.api.inner.report.pattern.PatternValidationException;
import pl.com.dbs.reports.report.domain.ReportBuilder;
import pl.com.dbs.reports.report.pattern.domain.PatternAsset;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;


/**
 * Validate files in package.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("report.pattern.content.validator.default")
public class PatternContentValidatorDefault {
	@Autowired private ReportExecutor executor;
	
	/**
	 * Validate manifest file.
	 */
	public void validate(Manifest manifest, List<PatternAsset> assets) throws PatternValidationException {
		final Attributes attrs = manifest.getAttributes(Pattern.ATTRIBUTE_SECTION);
		
//		Validate.notNull(assets, "Assets is no more!");
//		if (Iterables.find(assets, new Predicate<Asset>() { @Override public boolean apply(Asset input) { return asset.getPath().equals(input.getPath()); } }, null)!=null)
//			throw new IllegalStateException("Asset "+asset.getPath()+" already exists!");		
		
		/**
		 * Init SQL
		 */
		validateInitSql(attrs, assets);
		
		/**
		 * Extensions 
		 */
		validateExtensionMap(attrs, assets);

	}
	
	private void validateInitSql(final Attributes attrs, List<PatternAsset> assets) throws PatternValidationException {
		if (!StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_INIT_SQL))) {
			StringTokenizer st = new StringTokenizer(attrs.getValue(Pattern.ATTRIBUTE_INIT_SQL), ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				PatternAsset asset = Iterables.find(assets, new Predicate<PatternAsset>() {
					@Override
					public boolean apply(PatternAsset input) {
						return token.equalsIgnoreCase(input.getPath());
					}
				}, null);
				if (asset==null) throw new PatternValidationException(Pattern.ATTRIBUTE_INIT_SQL, token);
				/**
				 * Try execute safetly...
				 */
				try {
					executor.execute(asset.getContentAsString());
				} catch (ReportExecuteException e) {
					throw new PatternValidationException(e, Pattern.ATTRIBUTE_INIT_SQL, token);
				}
					
			}
		}		
	}
	
	private void validateExtensionMap(final Attributes attrs, List<PatternAsset> assets) throws PatternValidationException {
		if (!StringUtils.isBlank(attrs.getValue(Pattern.ATTRIBUTE_EXTENSION_MAP))) {
			StringTokenizer st = new StringTokenizer(attrs.getValue(Pattern.ATTRIBUTE_EXTENSION_MAP), ";");
			while (st.hasMoreTokens()) {
				final String token = StringUtils.trim(st.nextToken());
				/**
				 * Find pairs filename=ext
				 */
			    Matcher m = ReportBuilder.EXTENSION_PATTERN.matcher(token);
			    if (!m.matches()) throw new PatternValidationException(Pattern.ATTRIBUTE_EXTENSION_MAP, token);
			    m.reset();
			    while (m.find()) {
			    	/**
			    	 * Confirm if file exist with given filename...
			    	 */
			    	final String file = StringUtils.trim(m.group(1));
			    	PatternAsset asset = Iterables.find(assets, new Predicate<PatternAsset>() {
			    		@Override
			    		public boolean apply(PatternAsset input) {
			    			return file.equalsIgnoreCase(input.getPath());
			    		}
			    	}, null);
			    	if (asset==null) throw new PatternValidationException(Pattern.ATTRIBUTE_EXTENSION_MAP, file);
			    }
			}
		}		
	}	
}
