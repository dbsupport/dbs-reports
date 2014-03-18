/**
 * 
 */
package pl.com.dbs.reports.report.domain.inflation.rules;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportBlockReplaceRule implements ReportBlockRule {

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

	@Override
	public String apply(final String content, final String key, final String value) {
		return content.replaceAll("\\^\\$"+key+"\\^", !StringUtils.isBlank(value)?value:"");
	}

}
