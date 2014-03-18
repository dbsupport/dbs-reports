/**
 * 
 */
package pl.com.dbs.reports.report.domain.inflation.rules;

import java.util.LinkedList;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.report.domain.ReportBlock;

/**
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportBlockSwitchRule implements ReportBlockRule {
	private static final Logger logger = Logger.getLogger(ReportBlockSwitchRule.class);
	
	private static final java.util.regex.Pattern SWITCH_PATTERN = java.util.regex.Pattern.compile("SWITCH[\\s\\S]*\\(\\^\\$([\\w\\d_]+)\\^\\)([\\s\\S]*)ENDSWITCH",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern CASES_PATTERN = java.util.regex.Pattern.compile("(CASE\\s*\\(([\\d\\$]*)\\)(?:[\\s\\S])*?(?=CASE\\s*\\(([\\d\\$]*)\\)|ENDSWITCH))",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern CASE_PATTERN = java.util.regex.Pattern.compile("CASE\\s*\\(([\\d\\$]*)\\)",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public String apply(final String content, final String key, final String value) {
		Matcher switchm = SWITCH_PATTERN.matcher(content);
		while (switchm.find()) {
			String variable = switchm.group(1);
			String cases = switchm.group(2);
			if (!StringUtils.isBlank(variable)&&variable.equalsIgnoreCase(key)&&!StringUtils.isBlank(cases)) {
				ReportBlockRuleSwitch sw = new ReportBlockRuleSwitch(value);
				Matcher casesm = CASES_PATTERN.matcher(cases);
				
				while (casesm.find()) {
					String acase = casesm.group(1);
					sw.add(new ReportBlockRuleCase(acase));
				}
				
				String casee = sw.resolve();
				logger.debug(sw);
			}
		}
		
		return content;
	}
	
	private class ReportBlockRuleSwitch {
		private LinkedList<ReportBlockRuleCase> cases = new LinkedList<ReportBlockRuleCase>();
		private ReportBlockRuleCase result;
		private String option;
		
		private ReportBlockRuleSwitch(String option) {
			this.option = option;
		}
		
		private void add(ReportBlockRuleCase casee) {
			this.cases.add(casee);
			if (casee.isDefault()) {
				if (result!=null) throw new IllegalStateException("Too many default statements!");
				result = casee;
			}
		}
		
		private String resolve() {
			for (ReportBlockRuleCase casee : cases) {
				if (casee.isEqual(option)) return casee.content;
			}
			return result!=null?result.content:"";
		}
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			for (ReportBlockRuleCase casee : cases) {
				sb.append(casee).append("\n");
			}
			return sb.toString();
		}
	}
	
	private class ReportBlockRuleCase {
		private String option = "";
		private String content = "";
		
		ReportBlockRuleCase(String content) {
			if (!StringUtils.isBlank(content)) {
				String[] splits = content.split("CASE\\s*\\(([\\d\\$]*)\\)");
				if (splits.length>1) {
					Matcher optionm = CASE_PATTERN.matcher(content);
					if (optionm.find()) this.option = optionm.group(1);
					this.content = splits[1];
				}
			}
		}
		
		private boolean isEqual(String option) {
			return this.option.equalsIgnoreCase(option);
		}
		
		private boolean isDefault() {
			return option.equalsIgnoreCase("$");
		}
		
		@Override
		public String toString() {
			return option+":"+content;
		}		
		
	}

}
