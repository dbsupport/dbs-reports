/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.List;

import pl.com.dbs.reports.api.report.ReportLog;


/**
 * Interface blocks builder for given type of transfromate.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
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

	/**
	 * Results: logs
	 */
	List<ReportLog> getLogs();

}
