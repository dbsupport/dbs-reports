/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;

/**
 * Report generation context implementation.
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportGenerationContextDefault implements ReportGenerationContext, Serializable {
	private static final long serialVersionUID = -6908861084907040852L;
	
	private String name;
	
	private PatternFormat format;	
	
    private List<Map<String, String>> parameters = new ArrayList<Map<String, String>>(); 
	
	private long pattern;
	
	public ReportGenerationContextDefault() {}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public PatternFormat getFormat() {
		return format;
	}

	@Override
	public List<Map<String, String>> getParameters() {
		return parameters; 
	}

	@Override
	public long getPattern() {
		return pattern;
	}
}
