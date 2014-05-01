/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.util.Map;

import org.springframework.stereotype.Component;

import pl.com.dbs.reports.report.domain.ReportBlockException;

/**
 * Inflates blocks.
 * Takes root block (tree root) and goes deep inside and inflates blocks (if they are inflateables) with given params.
 * No DB access required.
 * All parameters should be provided!
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Component
public class ReportTextBlockInflaterDefault implements ReportTextBlockInflater {
	/**
	 * Iterates through blocks and chenges its content inflating them from sql-inflations.
	 */
	@Override
	public void inflate(final ReportTextBlock root, final Map<String, String> params, final StringBuilder sb) throws ReportBlockException {
		for (ReportTextBlock block : root.getBlocks()) {
			inflateBlock(block, params, sb);
		}
	}
	
	/**
	 * Takes block and corelated inflation (if any).
	 */
	private void inflateBlock(final ReportTextBlock block, final Map<String, String> parameters, final StringBuilder sb) throws ReportBlockException { 
		if (block.hasContent()) {
			//..no more block inside.. generate content..
			block.build(parameters, sb);
			return;
		}

		//..go deeper and inflate children..
		for (final ReportTextBlock b : block.getBlocks()) {
			inflateBlock(b, parameters, sb);
		}		
	}
}
