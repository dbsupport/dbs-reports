/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import pl.com.dbs.reports.report.domain.rules.ReportBlockReplaceRule;
import pl.com.dbs.reports.report.domain.rules.ReportBlockRule;
import pl.com.dbs.reports.report.domain.rules.ReportBlockSwitchRule;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

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
	 * Is content builded already?
	 */
	private boolean processed;
	
	/**
	 * Block is expected to be inflated with data named like those.
	 */
	private Set<String> parameters = new HashSet<String>();
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
	ReportTextBlock(ReportTextBlock parent, String content) {
		this.parent = parent;
		this.content = content;
	}

	/**
	 * Named block.
	 */
	ReportTextBlock(ReportTextBlock parent, String label, String content) {
		this(parent, content);
		this.label = label;
	}
	
//	ReportTextBlock addRules(LinkedList<ReportBlockRule> rules) {
//		Validate.notNull(rules, "Rules cant be null");
//		this.rules = rules;
//		return this;
//	}


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
	ReportTextBlock build(final Map<String, String> params) throws ReportBlockException {
		if (processed) return this;
		if (params.isEmpty()&&isInflateable()) throw new IllegalStateException("Block ("+label+") requires input parameters but you try to build it without any!");
		
		if (content==null||params.isEmpty()) {
			processed = true;
			return this;
		}
		
		StringBuffer result = new StringBuffer(content);
		for (ReportBlockRule rule : rules) result = rule.apply(result, params);

		content = result.toString();
		processed = true;
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

	void addBlock(ReportTextBlock block) {
		this.blocks.add(block);
	}
	
	/**
	 * Add required param name.
	 */
	void addParameter(String param) {
		if (!parameters.contains(param))
			this.parameters.add(param);
	}

	/**
	 * Select inflater connected with that block (by label).
	 * If block is not inflatable returns null.
	 */
	ReportBlockInflation findInflater(final List<ReportBlockInflation> inflaters) {
		if (!isLabeled()) return null;
		
		final ReportTextBlock block = this;
		ReportBlockInflation inflater = Iterables.find(inflaters, new Predicate<ReportBlockInflation>() {
				@Override
				public boolean apply(ReportBlockInflation input) {
					return input.isApplicable(block);
				}
			}, null);
		
		if (inflater==null&&!parameters.isEmpty()) throw new IllegalStateException("Block("+label+") requires inflater but cant find one!");
		return inflater;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isBlank(label)) 
			sb.append("label:").append(getLabel());
		if (!StringUtils.isEmpty(content)) {
			int max = 25;
			sb.append("content:").append(content.substring(0, content.length()>max?max:content.length())).append("...");
		}
		return sb.toString();
	}
	
	/**
	 * Print structure.
	 */
	void print() {
		print(0);
	}
	
	private void print(int level) {
		String l = "";
		for (int i=0; i<level; i++) l += "-";
		logger.debug(l+" "+this);
		for (ReportTextBlock block : getBlocks()) block.print(level+1);
	}
	
	
	/**
	 * Is this block labeled?
	 */
	private boolean isLabeled() {
		return !StringUtils.isBlank(label);
	}	
	
}
