package pl.com.dbs.reports.support.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds a string replacing key by value...
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2017
 */
public class KeyValueReplacer {
	public static StringBuffer replace(StringBuffer sb, String key, String value) {
		Matcher m = Pattern.compile("\\^\\$"+key+"\\^", Pattern.CASE_INSENSITIVE).matcher(sb);
		if (m.find()) {
			//String buf = result.substring(m.start(), m.end()).toUpperCase();
			return replace(sb.replace(m.start(), m.end(), value), key, value);
		}
		return sb;
	}
}
