/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders.inflaters;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import pl.com.dbs.reports.report.domain.builders.ReportBlockException;
import pl.com.dbs.reports.report.domain.builders.ReportBlocksBuildContext;
import pl.com.dbs.reports.report.domain.builders.inflaters.functions.ReportBlockInflaterFunction;


/**
 * Inflates blocks with params.
 * Result is appened to content.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public abstract class ReportTextBlockInflater {
	@Autowired private List<ReportBlockInflaterFunction> functions;
	
	public abstract void inflate(final ReportBlocksBuildContext context) throws ReportBlockException;
	
	ReportBlockInflaterFunction resolveFunction(Entry<String, String> param) {
		for (ReportBlockInflaterFunction function : functions) {
			if (function.supports(param))
				return function;
		}
		return null;
	}
	
	
}
