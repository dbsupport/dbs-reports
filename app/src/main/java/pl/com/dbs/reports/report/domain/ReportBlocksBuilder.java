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
	protected ReportTextBlock root;
	
	ReportBlocksBuilder(byte[] content) {
		this.content = content;
	}
	
	/**
	 * Deconstructs content into blocks tree.
	 */
	abstract ReportBlocksBuilder deconstruct();
	
	/**
	 * Construct blocks into content. 
	 * Merge blocks (from three structure) back into content. 
	 */
	abstract ReportBlocksBuilder construct();
	
	/**
	 * Content. Either before as well as after construction.
	 */
	public byte[] getContent() {
		return content;
	}

	public ReportTextBlock getRootBlock() {
		return root;
	}
}
