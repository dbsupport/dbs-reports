/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.report.domain.ReportBlockException;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Report blocks builder for text formats.
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportTextBlocksBuilder implements ReportBlocksBuilder {
	private static final Logger logger = Logger.getLogger(ReportTextBlocksBuilder.class);
	private static final String BLOCK_START_PATTERN = "[BLOCK";
	private static final String BLOCK_END_PATTERN = "BLOCK]";
	private static final java.util.regex.Pattern BLOCK_LABEL_PATTERN = java.util.regex.Pattern.compile("^\\[BLOCK\\(([\\w\\d_]+)\\)[\\s\\S]*",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern BLOCK_CONTENT_PATTERN = java.util.regex.Pattern.compile("\\[BLOCK\\([\\w\\d_]+\\)([\\s\\S]*)",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern BLOCK_REST_PATTERN = java.util.regex.Pattern.compile("^BLOCK\\]([\\s\\S]*)",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern IN_VARIABLE_PATTERN = java.util.regex.Pattern.compile("\\^\\$([\\w\\d_]+)\\^",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern INFLATER_PATTERN = java.util.regex.Pattern.compile("(\\w+):\\s*(SELECT\\s+.+?FROM\\s+.+?);",  java.util.regex.Pattern.CASE_INSENSITIVE|java.util.regex.Pattern.DOTALL);
	
	protected byte[] content;
	protected ReportTextBlock root;
	private ReportTextBlockInflater inflater;
	private List<ReportBlockInflation> inflations;
	private Map<String, String> parameters;		
	
	public ReportTextBlocksBuilder(final PatternTransformate transformate, ReportTextBlockInflater inflater, final Map<String, String> params) {
		content = transformate.getContent();
		parameters = new HashMap<String, String>();
		resolveInflations(transformate);
		resolveInput();
		this.inflater = inflater;
		addParameters(params);
	}
	
	private ReportTextBlocksBuilder addParameters(Map<String, String> parameters) {
		if (parameters!=null) {
			for (Map.Entry<String, String> parameter : parameters.entrySet()) 
				this.parameters.put(parameter.getKey(), parameter.getValue());
		}
		return this;
	}	
	
	@Override
	public ReportTextBlocksBuilder addParameter(final String key, final String value) {
		if (parameters!=null&&!StringUtils.isBlank(key)) {
			this.parameters.put(key, value);
		}
		return this;
	}	
	
	/**
	 * Content. Either before as well as after construction.
	 */
	@Override
	public byte[] getContent() {
		return content;
	}

	ReportTextBlock getRootBlock() {
		return root;
	}	
	
	/**
	 * Iterate thorough all .sql files and obtain all inflaters (labeled block) from them..
	 */
	private void resolveInflations(final PatternTransformate transformate) {
		this.inflations = new ArrayList<ReportBlockInflation>();
		
		for (PatternInflater inflater : transformate.getInflaters()) {
			resolveInflations(new String(inflater.getContent()));
		}
	}	
	
	/**
	 * Find all inflation blocks in provided content, like: 
	 * INIT: SELECT INTO OUT_VARIABLE1, OUT_VARIABLE2 FROM TABLE WHERE ID=^$IN_VARIABLE1^ AND DATE<^$IN_VARIABLE2^;
	 * -- label: INIT
	 * -- sql: SELECT INTO OUT_VARIABLE1, OUT_VARIABLE2 FROM TABLE WHERE ID=^$IN_VARIABLE1^ AND DATE<^$IN_VARIABLE2^;
	 */
	private void resolveInflations(String content) {
		logger.debug("Resolving inflations..");
		Matcher inflations = INFLATER_PATTERN.matcher(content);
	    while (inflations.find()) {
	    	String label = StringUtils.trim(inflations.group(1));
	    	String sql = StringUtils.trim(inflations.group(2));
	    	ReportBlockInflation inflation = new ReportBlockInflation(label, sql);
	    	logger.debug("Inflation found: "+inflation);
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
			logger.debug("Resolving input variables of "+inflater.getLabel());
			Matcher variables = IN_VARIABLE_PATTERN.matcher(inflater.getSql());
		    while (variables.find()) {
		    	String variable = StringUtils.trim(variables.group(1));
		    	logger.debug("Input variable found: "+variable);
		    	inflater.addInput(variable);
		    }
		}
	}	
	

	
	@Override
	public ReportTextBlocksBuilder build() throws ReportBlockException {
		//..deconstruct first...
		deconstruct();
		
		//..inflate...
		if (inflater!=null) {
			StringBuilder sb = new StringBuilder();
			inflater.inflate(root, parameters, sb);
			content = sb.toString().getBytes();
		}
		
		return this;
	}

	private void deconstruct() {
		ReportTextBlock root = new ReportTextBlock();
		this.root = root;
		parse(new String(content));
		this.root = root;
		this.root.print();
	}
	
	String getContentAsString() {
		return new String(content);
	}

	/**
	 * Breakes content into blocks.
	 * All in memory - sic!
	 */
	private String parse(String content) {
		if (StringUtils.isBlank(content)) return null;
		
		Content data = new Content(content);
		String rest = null;
		
		if (data.isSimple()) {
			//..add to current block..
			ReportTextBlock block = new ReportTextBlock(this.root).addContent(data.getText());
			//resolve invariables..
			resolveInput(data.getText());
			//..put to parent..
			this.root.addBlock(block);
			rest = data.getContent();
		} else if (data.isBlockStart()) {
			String label = data.getBlockLabel();
			rest = data.getBlockContent();
			
			ReportTextBlock block = new ReportTextBlock(this.root, label);
			resolveInflation(block);
			this.root.addBlock(block);
			this.root = block;
		} else if (data.isBlockEnd()) {
			rest = data.getBlockRest();
			this.root = this.root.getParent();
		} else {
			throw new IllegalStateException("Unknown TextBlock build phase!");
		}
		
		return parse(rest);
	}
	
	/**
	 * Select inflater connected with root block (by label).
	 * If block is not inflatable (has no label) do nothing.
	 */
	private void resolveInflation(final ReportTextBlock block) {
		if (StringUtils.isBlank(block.getLabel())) return;
		
		block.addInflation(
			Iterables.find(inflations, new Predicate<ReportBlockInflation>() {
					@Override
					public boolean apply(ReportBlockInflation input) {
						return input.isApplicable(block.getLabel());
					}
			}, null));
	}	
	
	private void resolveInput(String content) {
		logger.debug("Resolving input variables..");
		Matcher variables = IN_VARIABLE_PATTERN.matcher(content);
	    while (variables.find()) {
	    	String variable = StringUtils.trim(variables.group(1));
	    	logger.debug("Input variable found: "+variable);
	    	root.addParameter(variable);
	    }
	}
	
	private class Content {
		private String text = null;
		private String content = null;
		
		Content(String txt) {
			int sdx = txt.indexOf(BLOCK_START_PATTERN);
			int edx = txt.indexOf(BLOCK_END_PATTERN);
			
			if (sdx<0&&edx<0) {
				text = txt;
				content = null;
			} else if (sdx==0||edx==0) {
				text = null;
				content = txt;
			} else {
				int idx = (sdx>=0&&edx>=0)? 
							(sdx<edx?sdx:edx):
								(sdx<0&&edx>=0)?edx:sdx;
				text = txt.substring(0, idx);
				content = txt.substring(idx);
			}
		}
		
		boolean isSimple() {
			return !StringUtils.isEmpty(text);
		}
		
		String getText() {
			return text;
		}
		
		String getContent() {
			return content;
		}
		
		boolean isBlockStart() {
			return content.startsWith(BLOCK_START_PATTERN);
		}
		
		boolean isBlockEnd() {
			return content.startsWith(BLOCK_END_PATTERN);
//			int edx = content.indexOf(BLOCK_END_PATTERN);
//			return edx>=0?content.substring(0, edx).indexOf(BLOCK_START_PATTERN)<0:false;
		}
		
		String getBlockLabel() {
			Matcher m = BLOCK_LABEL_PATTERN.matcher(content);
			return m.matches()?m.group(1):null;
		}
		
		String getBlockContent() {
			Matcher m = BLOCK_CONTENT_PATTERN.matcher(content);
			return m.matches()?m.group(1):null;
		}
		
		String getBlockRest() {
			Matcher m = BLOCK_REST_PATTERN.matcher(content);
			return m.matches()?m.group(1):null;
		}		
	}
}
