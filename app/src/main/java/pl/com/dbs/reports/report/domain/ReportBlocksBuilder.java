/**
 * 
 */
package pl.com.dbs.reports.report.domain;




/**
 * Abstract blocks builder for given type of transfromate.
 * Holds branch of blocks.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public abstract class ReportBlocksBuilder {
	protected byte[] content;
	protected ReportBlock block;
	
	ReportBlocksBuilder(byte[] content) {
		this.content = content;
	}
	
	abstract ReportBlocksBuilder build();
	
	ReportBlock getBlock() {
		return block;
	}
}
