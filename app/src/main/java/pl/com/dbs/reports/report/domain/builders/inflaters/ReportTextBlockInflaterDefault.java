/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders.inflaters;

import java.util.Map;

import org.springframework.stereotype.Component;

import pl.com.dbs.reports.report.domain.builders.ReportBlockException;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;
import pl.com.dbs.reports.report.domain.builders.ReportTextBlock;

/**
 * Inflates blocks.
 * Takes root block (tree root) and goes deep inside and inflates blocks (if they are inflateables) with given params.
 * No DB access required.
 * All parameters should be provided!
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Component("report.block.inflater.default")
public class ReportTextBlockInflaterDefault extends ReportTextBlockInflater {
	/**
	 * Iterates through blocks and chenges its content inflating them from sql-inflations.
	 */
	@Override
	public void inflate(final ReportBlocksBuildContext context) throws ReportBlockException {
		for (ReportTextBlock block : context.getBlocks()) {
			inflateBlock(block, context.getParams(), context.getContent());
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
