/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
final class ReportTextBlocksBuilder extends ReportBlocksBuilder {
	private static final Logger logger = Logger.getLogger(ReportTextBlocksBuilder.class);
	private static final String BLOCK_START_PATTERN = "[BLOCK";
	private static final String BLOCK_END_PATTERN = "BLOCK]";
	private static final java.util.regex.Pattern BLOCK_LABEL_PATTERN = java.util.regex.Pattern.compile("^\\[BLOCK\\(([\\w\\d_]+)\\)[\\s\\S]*",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern BLOCK_CONTENT_PATTERN = java.util.regex.Pattern.compile("\\[BLOCK\\([\\w\\d_]+\\)([\\s\\S]*)",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern BLOCK_REST_PATTERN = java.util.regex.Pattern.compile("^BLOCK\\]([\\s\\S]*)",  java.util.regex.Pattern.CASE_INSENSITIVE);
	private static final java.util.regex.Pattern IN_VARIABLE_PATTERN = java.util.regex.Pattern.compile("\\^\\$([\\w\\d_]+)\\^",  java.util.regex.Pattern.CASE_INSENSITIVE);

	ReportTextBlocksBuilder(byte[] content) {
		super(content);
	}
	
	ReportTextBlocksBuilder build() {
		ReportBlock root = new ReportBlock();
		this.block = root;
		parse(new String(content));
        this.block = root;
        return this;
	}
	
	
	private String parse(String content) {
		if (StringUtils.isBlank(content)) return null;
		
		Content data = new Content(content);
		String rest = null;
		
		if (data.isSimple()) {
			//..add to current block..
			ReportBlock block = new ReportBlock(this.block, data.getText());
			//resolve invariables..
			resolveInput(data.getText());
			//..put to parent..
			this.block.addBlock(block);
			rest = data.getContent();
		} else if (data.isBlockStart()) {
			String label = data.getBlockLabel();
			rest = data.getBlockContent();
			
			ReportBlock block = new ReportBlock(this.block, label, null);
			this.block.addBlock(block);
			this.block = block;
		} else if (data.isBlockEnd()) {
			rest = data.getBlockRest();
			this.block = this.block.getParent();
		} else {
			throw new IllegalStateException("Unknown TextBlock build phase!");
		}
		
		return parse(rest);
	}
	
	private void resolveInput(String content) {
		logger.info("Resolving input variables..");
		Matcher variables = IN_VARIABLE_PATTERN.matcher(content);
	    while (variables.find()) {
	    	String variable = StringUtils.trim(variables.group(1));
	    	logger.info("Input variable found: "+variable);
	    	block.addParameter(variable);
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
