/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Inflates blocks.
 * Takes root block (tree root) and goes deep inside and inflates blocks (if they are inflateables) with given params.
 * No DB access required.
 * All parameters should be provided before!
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Component
public class ReportTextBlockInflater {
	/**
	 * Iterates through blocks and chenges its content inflating them from sql-inflations.
	 */
	public void inflate(final ReportTextBlock root, final Map<String, String> parameters) throws ReportBlockException {
		for (ReportTextBlock block : root.getBlocks()) {
			inflateBlock(block, parameters);
		}
	}
	
	/**
	 * Takes block and corelated inflation (if any).
	 */
	private void inflateBlock(final ReportTextBlock block, final Map<String, String> parameters) throws ReportBlockException { 
		if (block.hasContent()) {
			//..no more block inside.. generate content..
			block.build(parameters);
			return;
		}

		//..go deeper and inflate children..
		for (final ReportTextBlock b : block.getBlocks()) {
			inflateBlock(b, parameters);
		}		
	}
}
