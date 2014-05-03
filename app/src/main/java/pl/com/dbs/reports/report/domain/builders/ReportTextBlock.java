/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.report.domain.ReportBlockException;
import pl.com.dbs.reports.report.domain.rules.ReportBlockReplaceRule;
import pl.com.dbs.reports.report.domain.rules.ReportBlockRule;
import pl.com.dbs.reports.report.domain.rules.ReportBlockSwitchRule;
import pl.com.dbs.reports.support.utils.separator.Separator;

/**
 * A block of paterrn data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportTextBlock {
	private static final Logger logger = Logger.getLogger(ReportTextBlock.class);
	/**
	 * Parent block
	 */
	private ReportTextBlock parent;
	/**
	 * If this is named block contains label, otherwise null.
	 */
	private String label;
	/**
	 * Content itself as pattern or simple data.
	 */
	private String content;
	/**
	 * Assigned inflation (sql file).
	 */
	private ReportBlockInflation inflation;
	/**
	 * Block is expected to be inflated with data named like those.
	 */
	Set<String> parameters = new HashSet<String>();
	/**
	 * Nested blocks.
	 */
	private LinkedList<ReportTextBlock> blocks = new LinkedList<ReportTextBlock>();

	/**
	 * Rules to process content.
	 */
	private static LinkedList<ReportBlockRule> rules = new LinkedList<ReportBlockRule>();
	static {
		rules.add(new ReportBlockSwitchRule());
		rules.add(new ReportBlockReplaceRule());
	}

	
	/**
	 * Totally empty block (root).
	 */
	ReportTextBlock() {}
	
	/**
	 * Simple (not-named) block.
	 */
	ReportTextBlock(ReportTextBlock parent) {
		this.parent = parent;
	}

	/**
	 * Named block.
	 */
	ReportTextBlock(ReportTextBlock parent, String label) {
		this(parent);
		this.label = label;
	}
	
	ReportTextBlock addContent(String content) {
		this.content = content;
		return this;
	}
	
	ReportTextBlock addInflation(ReportBlockInflation inflation) {
		this.inflation = inflation;
		return this;
	}
	

	/**
	 * Requires to be inflated with some data?
	 */
	boolean isInflateable() {
		return !parameters.isEmpty();
	}

	/**
	 * Replaces parameter variables with values (given in params).
	 * Applies rules (replacing strategy).
	 * Only for inflateable blocks!
	 * Can produce no data if content is empty.
	 */
	ReportTextBlock build(final Map<String, String> params, final StringBuilder sb) throws ReportBlockException {
		if (params.isEmpty()&&isInflateable()) throw new IllegalStateException("Block ("+label+") requires input parameters but you try to build it without any!");
		
		if (content==null) return this;
		if (params.isEmpty()) return this;
		
		StringBuilder result = new StringBuilder(content);
		for (ReportBlockRule rule : rules) result = rule.apply(result, params);

		sb.append(result);
		return this;
	}

	String getLabel() {
		return label;
	}
	
	ReportTextBlock getParent() {
		return parent;
	}

	LinkedList<ReportTextBlock> getBlocks() {
		return blocks;
	}
	
	boolean hasContent() {
		return !StringUtils.isBlank(content);
	}
	
	String getContent() {
		return content;
	}

	public ReportBlockInflation getInflation() {
		return inflation;
	}

	ReportTextBlock addBlock(ReportTextBlock block) {
		this.blocks.add(block);
		return this;
	}
	
	/**
	 * Add required param name.
	 */
	ReportTextBlock addParameter(String param) {
		if (!parameters.contains(param))
			this.parameters.add(param);
		return this;
	}

	@Override
	public String toString() {
		Separator separator = new Separator(";");
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isBlank(label)) 
			sb.append(separator).append("label:").append(getLabel());
		if (!StringUtils.isEmpty(content)) {
			int max = 25;
			sb.append(separator).append("content:").append(content.substring(0, content.length()>max?max:content.length())).append("...");
		}
		if (parameters!=null&&!parameters.isEmpty()) {
			sb.append(separator).append("parameters:[");
			Separator s = new Separator(", ");
			for (String parameter : parameters) {
				sb.append(s).append(parameter);
			}
			sb.append("]");
		}
		if (inflation!=null) {
			sb.append(separator).append("inflation:["+inflation+"]");
		}
		return sb.toString();
	}
	
	/**
	 * Print structure.
	 */
	ReportTextBlock print() {
		print(0);
		return this;
	}
	
	private void print(int level) {
		String l = "";
		for (int i=0; i<level; i++) l += "-";
		logger.debug(l+" "+this);
		for (ReportTextBlock block : getBlocks()) block.print(level+1);
	}
	
}
