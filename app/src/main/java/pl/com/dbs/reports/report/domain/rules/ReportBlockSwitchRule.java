/**
 * 
 */
package pl.com.dbs.reports.report.domain.rules;

import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.report.domain.ReportBlockException;

/**
 * SWITCH rule.
 * Searches fo SWITCH...blocks and resolves them using parameters.
 * 
 * PS. Option value cant have: ()
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportBlockSwitchRule implements ReportBlockRule {
	private static int VALVE = 100;
	private static final java.util.regex.Pattern SWITCH_PATTERN = java.util.regex.Pattern.compile("\\[SWITCH[\\s]*?\\([\\s]*?\\^\\$([\\w\\d_]+)\\^[\\s]*?\\)([\\s\\S]*?)SWITCH\\]",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern CASES_PATTERN = java.util.regex.Pattern.compile("#CASE\\(([\\s\\S]*?)\\)(((?!#CASE)[\\s\\S])*)",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public StringBuilder apply(final StringBuilder content, final Map<String, String> params) throws ReportBlockException {
		ReportBlockRuleSwitch aswitch = null;
		int valve = 0;
		StringBuilder buffer = new StringBuilder(content);
		while ((aswitch = resolveSwitch(buffer))!=null) {
			if (++valve > VALVE) throw new ReportBlockException("Too many iteration! "+valve+">"+VALVE);
			buffer = buffer.replace(aswitch.start, aswitch.end, aswitch.resolve(params));
		}
		
		return buffer;
	}
	
	/**
	 * Searches till finds first occurance.
	 */
	private ReportBlockRuleSwitch resolveSwitch(final StringBuilder content) {
		Matcher switchMatcher = SWITCH_PATTERN.matcher(content);
		while (switchMatcher.find()) {
			String switchOption = switchMatcher.group(1);
			String switchCases = switchMatcher.group(2);

			if (!StringUtils.isBlank(switchOption)&&!StringUtils.isBlank(switchCases)) {
				return new ReportBlockRuleSwitch(switchOption, switchMatcher.start(), switchMatcher.end())
							.add(resolveCases(switchCases));
			}
		}		
		return null;
	}
	
	private LinkedList<ReportBlockRuleCase> resolveCases(final String content) {
		LinkedList<ReportBlockRuleCase> result = new LinkedList<ReportBlockRuleCase>(); 
		Matcher caseMatcher = CASES_PATTERN.matcher(content);
		while (caseMatcher.find()) {
			String caseOption = caseMatcher.group(1);
			String caseContent = caseMatcher.group(2);
			if (!StringUtils.isBlank(caseContent)) {
				result.add(new ReportBlockRuleCase(caseOption, caseContent));
			}
		}
		return result;
	}
	
	private class ReportBlockRuleSwitch {
		private LinkedList<ReportBlockRuleCase> cases = new LinkedList<ReportBlockRuleCase>();
		private ReportBlockRuleCase result;
		private String option;
		private int start;
		private int end;
		
		private ReportBlockRuleSwitch(String option, int start, int end) {
			this.option = option.toUpperCase();
			this.start = start;
			this.end = end;
		}
		
		private ReportBlockRuleSwitch add(ReportBlockRuleCase acase) {
			this.cases.add(acase);
			if (acase.isDefault()) {
				if (result!=null) throw new IllegalStateException("Too many default statements!");
				result = acase;
			}
			return this;
		}
		
		private ReportBlockRuleSwitch add(LinkedList<ReportBlockRuleCase> cases) {
			for (ReportBlockRuleCase acase : cases)
				this.add(acase);
			return this;
		}		
		
		private String resolve(Map<String, String> params) throws ReportBlockException {
			String value = resolveOption(params);
			
			for (ReportBlockRuleCase acase : cases) {
				if (acase.isEqual(value)) return acase.content;
			}
			return result!=null?result.content:"";
		}
		
		private String resolveOption(final Map<String, String> params) throws ReportBlockException {
			String value = params.get(option);
			if (value==null) throw new ReportBlockException("ReportBlockRuleSwitch requires value for parameter: "+option+" but cant find one!");
			return value;
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
		
		ReportBlockRuleCase(String option, String content) {
			this.option = !StringUtils.isBlank(option)?option.trim():"";
			this.content = content;
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
