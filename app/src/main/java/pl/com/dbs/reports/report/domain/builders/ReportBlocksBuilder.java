/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;



/**
 * Interface blocks builder for given type of transfromate.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public interface ReportBlocksBuilder {
	/**
	 * Deconstructs content into blocks tree.
	 * Process blocks.
	 */
	ReportBlocksBuilder build();
	
	ReportBlocksBuilder addParameter(final String key, final String value);

	/**
	 * Results: content
	 */
	byte[] getContent();
}
