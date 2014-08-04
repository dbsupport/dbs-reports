/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders.inflaters.functions;

import java.util.Map.Entry;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import pl.com.dbs.reports.api.report.ReportLogType;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildTerminationException;


/**
 * Terminates generation if parameters result is 1.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Component
public class ReportBlockInflaterTerminateFunction implements ReportBlockInflaterFunction {
	private static final String NAME = "OUT_TERMINATE";
	private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("^result:([0-9]{1})\\|msg:(.*)$",  java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.DOTALL);
	
	@Override
	public boolean supports(final Entry<String, String> param) {
		if (StringUtils.isBlank(param.getKey())) return false;
		
		String key = param.getKey().trim();
		return NAME.equalsIgnoreCase(key);
	}
	
	public void apply(final ReportBlocksBuildContext context, final Entry<String, String> param) {
		if (isStop(param)) {
			String msg = getMsg(param);
			context.addLog(ReportLogType.INFO, msg);
			context.getContent().delete(0, context.getContent().length());
			throw new ReportBlocksBuildTerminationException(msg);
		}
	}

	private String getMsg(final Entry<String, String> param) {
		Matcher matcher = PATTERN.matcher(param.getValue());
		String msg = "";
		if (matcher.find()) {
			if (!StringUtils.isBlank(matcher.group(2))) msg = matcher.group(2);
		}			
		return msg;
	}
	
	private boolean isStop(final Entry<String, String> param) {
		Matcher matcher = PATTERN.matcher(param.getValue());
		if (matcher.find()) {
			try {
				Integer result = Integer.valueOf(matcher.group(1));
				return (result!=null&&result==1);
			} catch (NumberFormatException e) {}
		}			
		return false;
	}

}
