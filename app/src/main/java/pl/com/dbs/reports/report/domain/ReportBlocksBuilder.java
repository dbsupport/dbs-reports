/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.LinkedList;

import pl.com.dbs.reports.report.domain.rules.ReportBlockReplaceRule;
import pl.com.dbs.reports.report.domain.rules.ReportBlockRule;
import pl.com.dbs.reports.report.domain.rules.ReportBlockSwitchRule;




/**
 * Abstract blocks builder for given type of transfromate.
 * Holds branch of blocks.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public abstract class ReportBlocksBuilder {
	protected byte[] content;
	protected LinkedList<ReportBlockRule> rules = new LinkedList<ReportBlockRule>();
	protected ReportBlock block;
	
	ReportBlocksBuilder(byte[] content) {
		this.content = content;
		rules.add(new ReportBlockSwitchRule());
		rules.add(new ReportBlockReplaceRule());
	}
	
	abstract ReportBlocksBuilder build();
	
	ReportBlock getBlock() {
		return block;
	}
}
