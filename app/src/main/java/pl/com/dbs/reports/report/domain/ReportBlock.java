/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * A block of paterrn data.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportBlock {
	private static final Logger logger = Logger.getLogger(ReportBlock.class);
	/**
	 * Parent block
	 */
	private ReportBlock parent;
	/**
	 * If this is named block contains label, otherwise null.
	 */
	private String label;
	/**
	 * Content itself as pattern or simple data.
	 */
	private String content;
	/**
	 * Block is expected to be inflated with data named like those.
	 */
	private Set<String> parameters = new HashSet<String>();
	/**
	 * Nested blocks.
	 */
	private LinkedList<ReportBlock> blocks = new LinkedList<ReportBlock>();
	
	/**
	 * Totally empty block (root).
	 */
	ReportBlock() {}
	
	/**
	 * Simple (not-named) block.
	 */
	ReportBlock(ReportBlock parent, String content) {
		this.parent = parent;
		this.content = content;
	}

	/**
	 * Named block.
	 */
	ReportBlock(ReportBlock parent, String label, String content) {
		this(parent, content);
		this.label = label;
	}


	/**
	 * Requires to be inflated with some data?
	 */
	boolean isInflateable() {
		return !parameters.isEmpty();
	}

	/**
	 * Produce block data inflating with params.
	 * Only for inflateable blocks!
	 * Can produce no data if content is empty.
	 */
	String build(final Map<String, String> params) {
		if (params.isEmpty()&&isInflateable()) throw new IllegalStateException("Block ("+label+") requires input parameters but you try to build it without any!");
		
		if (content==null) return null;
		if (params.isEmpty()) return content;
		
		String result = new String(content);
		for (Entry<String, String> param: params.entrySet()) {
			result = result.replaceAll("\\^\\$"+param.getKey()+"\\^", !StringUtils.isBlank(param.getValue())?param.getValue():"");
		}
		return result;
	}

	String getLabel() {
		return label;
	}
	
	ReportBlock getParent() {
		return parent;
	}

	LinkedList<ReportBlock> getBlocks() {
		return blocks;
	}
	
	boolean hasContent() {
		return !StringUtils.isBlank(content);
	}

	void addBlock(ReportBlock block) {
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
		
		final ReportBlock block = this;
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
		for (ReportBlock block : getBlocks()) block.print(level+1);
	}
	
	
	/**
	 * Is this block labeled?
	 */
	private boolean isLabeled() {
		return !StringUtils.isBlank(label);
	}	
	
}
