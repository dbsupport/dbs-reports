/**
 * 
 */
package pl.com.dbs.reports.report.domain.rules;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.report.domain.builders.ReportBlockException;

/**
 * Simple replace rule.
 * Replaces variables with their values.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportBlockReplaceRule implements ReportBlockRule {
	private static int VALVE = 100;
	
	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

	@Override
	public StringBuilder apply(final StringBuilder content, final Map<String, String> params) throws ReportBlockException {
		StringBuilder result = new StringBuilder(content);
		for (Map.Entry<String, String> param : params.entrySet()) {
			result = apply(result, param.getKey(), !StringUtils.isBlank(param.getValue())?param.getValue():"", 0);
		}
		return result;
	}
	
	private StringBuilder apply(final StringBuilder content, final String key, final String value, int valve) throws ReportBlockException {
		if (valve++ > VALVE) throw new ReportBlockException("Too many iteration! "+valve+">"+VALVE);
		
		Matcher m = Pattern.compile("\\^\\$"+key+"\\^", Pattern.CASE_INSENSITIVE).matcher(content);
		if (m.find()) {
			return apply(content.replace(m.start(), m.end(), value), key, value, valve++);
		}					
		return content;
	}
	
}
