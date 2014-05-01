/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import pl.com.dbs.reports.report.domain.ReportBlockException;






/**
 * Abstract blocks builder for given type of transfromate.
 * Holds branch of blocks.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public interface ReportBlocksBuilder {
	/**
	 * Deconstructs content into blocks tree.
	 * Process blocks.
	 */
	ReportBlocksBuilder build() throws ReportBlockException;
	
	ReportBlocksBuilder addParameter(final String key, final String value);

	/**
	 * Results
	 */
	byte[] getContent();	

}
