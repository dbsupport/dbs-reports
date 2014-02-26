/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.ArrayList;
import java.util.List;

import pl.com.dbs.reports.api.report.pattern.PatternInflater;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.support.encoding.EncodingContext;

/**
 * Inflationing context.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
class ReportInflationContext {
	private List<ReportBlockInflation> inflations;
	private ReportBuilder builder;
	private EncodingContext encodingContext;  
	
	ReportInflationContext(final ReportBuilder builder, final PatternTransformate transformate, final EncodingContext encodingContext) {
		this.builder = builder;
		this.encodingContext = encodingContext;
		inflations = resolveInflations(transformate);		
	}
	
	List<ReportBlockInflation> getInflations() {
		return inflations;
	}
	
	ReportBuilder getBuilder() {
		return builder;
	}
	
	EncodingContext getEncodingContext() {
		return encodingContext;
	}
	
	/**
	 * Iterate thorough all .sql files and obtain all inflaters from them..
	 */
	private List<ReportBlockInflation> resolveInflations(PatternTransformate transformate) {
		List<ReportBlockInflation> inflaters = new ArrayList<ReportBlockInflation>();
		for (PatternInflater inflater : transformate.getInflaters()) {
			inflaters.addAll(new ReportBlockInflationsBuilder(inflater.getContent()).build().getInflations());
		}
		return inflaters;
	}	
}
