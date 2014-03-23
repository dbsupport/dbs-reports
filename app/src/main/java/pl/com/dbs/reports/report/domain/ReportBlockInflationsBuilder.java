/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Builder that inflates block with db data.
 * OUTPUT variables are NOT supported - we dont need them actually.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
final class ReportBlockInflationsBuilder {
	private static final Logger logger = Logger.getLogger(ReportBlockInflationsBuilder.class);
	//private static final java.util.regex.Pattern INFLATER_PATTERN = java.util.regex.Pattern.compile("((\\w+):\\B([\\w\\d\\s\\(\\)\\^\\$_,'/\\.\\@=\\-\\<\\>\\%\\*\\+\\|]+);\\B)+",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern INFLATER_PATTERN = java.util.regex.Pattern.compile("(\\w+):\\s*(SELECT\\s+.+?FROM\\s+.+?);",  java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.DOTALL);
	private static final java.util.regex.Pattern IN_VARIABLE_PATTERN = java.util.regex.Pattern.compile("\\^\\$([\\w\\d_]+)\\^",  java.util.regex.Pattern.CASE_INSENSITIVE);
	
//	private static final java.util.regex.Pattern OUT_VARIABLES_PATTERN = java.util.regex.Pattern.compile("INTO\\s+([\\w\\d_,\\s]+)\\bFROM",  java.util.regex.Pattern.CASE_INSENSITIVE);
//	private static final java.util.regex.Pattern OUT_VARIABLE_PATTERN = java.util.regex.Pattern.compile("([\\w\\d_]+),*",  java.util.regex.Pattern.CASE_INSENSITIVE);
//	
//	private static final java.util.regex.Pattern OUT_VARIABLES2_PATTERN = java.util.regex.Pattern.compile("SELECT\\s+([\\w\\d_,\\s'\\(\\)\\/\\-\\^\\$]+)\\bFROM",  java.util.regex.Pattern.CASE_INSENSITIVE);
//	private static final java.util.regex.Pattern OUT_VARIABLE2_PATTERN = java.util.regex.Pattern.compile("\\s+([\\w\\d_]+)\\s*,{0,1}\\s*",  java.util.regex.Pattern.CASE_INSENSITIVE);
	

	/**
	 * Processing data.
	 */
	private String content;

	/**
	 * All found inflations.
	 */
	private List<ReportBlockInflation> inflations = new ArrayList<ReportBlockInflation>();
	
	ReportBlockInflationsBuilder(byte[] content) {
		this.content = new String(content);
	}
	
	ReportBlockInflationsBuilder build() {
		resolveInflations();
		resolveInput();
		//resolveOutput();
		//resolveOutput2();
		return this;
	}
	
	/**
	 * Find all inflation blocks in provided content, like: 
	 * INIT: SELECT INTO OUT_VARIABLE1, OUT_VARIABLE2 FROM TABLE WHERE ID=^$IN_VARIABLE1^ AND DATE<^$IN_VARIABLE2^;
	 * -- label: INIT
	 * -- sql: SELECT INTO OUT_VARIABLE1, OUT_VARIABLE2 FROM TABLE WHERE ID=^$IN_VARIABLE1^ AND DATE<^$IN_VARIABLE2^;
	 */
	private void resolveInflations() {
		logger.info("Resolving inflations..");
		Matcher inflations = INFLATER_PATTERN.matcher(content);
	    while (inflations.find()) {
	    	String label = StringUtils.trim(inflations.group(1));
	    	String sql = StringUtils.trim(inflations.group(2));
	    	ReportBlockInflation inflation = new ReportBlockInflation(label, sql);
	    	logger.info("Inflation found: "+inflation);
	    	this.inflations.add(inflation);
	    }
	}	
	
	/**
	 * Find all INPUT PARAMETERS in all inflations, like:
	 * SELECT INTO OUT_VARIABLE1, OUT_VARIABLE2 FROM TABLE WHERE ID=^$IN_VARIABLE1^ AND DATE<^$IN_VARIABLE2^;
	 * -- [IN_VARIABLE1, IN_VARIABLE2]
	 */
	private void resolveInput() {
		for (ReportBlockInflation inflater : this.inflations) {
			logger.info("Resolving input variables of "+inflater.getLabel());
			Matcher variables = IN_VARIABLE_PATTERN.matcher(inflater.getSql());
		    while (variables.find()) {
		    	String variable = StringUtils.trim(variables.group(1));
		    	logger.info("Input variable found: "+variable);
		    	inflater.addInput(variable);
		    }
		}
	}
	
	/**
	 * Find all OUTPUT PARAMETERS in all inflations, like:
	 * SELECT INTO OUT_VARIABLE1, OUT_VARIABLE2 FROM TABLE WHERE ID=^$IN_VARIABLE1^ AND DATE<^$IN_VARIABLE2^;
	 * -- [OUT_VARIABLE1, OUT_VARIABLE2]
	 */
//	private void resolveOutput() {
//		logger.info("Resolving output variables..");
//		for (ReportBlockInflation inflater : this.inflations) {
//			Matcher variables = OUT_VARIABLES_PATTERN.matcher(inflater.getSql());
//		    while (variables.find()) {
//		    	Matcher variable = OUT_VARIABLE_PATTERN.matcher(StringUtils.trim(variables.group(1)));
//		    	while (variable.find()) {
//		    		String value = StringUtils.trim(variable.group(1));
//		    		logger.info("Output variable found: "+value);
//		    		inflater.addOutput(value);
//		    	}
//		    }
//		}
//	}
	
//	private void resolveOutput2() {
//		for (ReportBlockInflation inflater : this.inflations) {
//			logger.info("Resolving output variables of "+inflater.getLabel());
//			Matcher variables = OUT_VARIABLES2_PATTERN.matcher(inflater.getSql());
//		    while (variables.find()) {
//		    	Matcher variable = OUT_VARIABLE2_PATTERN.matcher(StringUtils.trim(variables.group(1)));
//		    	while (variable.find()) {
//		    		String value = StringUtils.trim(variable.group(1));
//		    		logger.info("Output variable found: "+value);
//		    		inflater.addOutput(value);
//		    	}
//		    }
//		}
//	}	
	
	List<ReportBlockInflation> getInflations() {
		return inflations;
	}
	
}
