/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import java.util.Map;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.report.domain.ReportGeneration;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.web.form.AForm;


/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportExecuteForm extends AForm implements ReportGeneration {
	public static final String KEY = "reportGenerationForm";
	private ReportPattern pattern;
	
	public ReportExecuteForm() {}
	
	public void reset(ReportPattern pattern) {
		super.reset();
		this.pattern = pattern;
	}

	public ReportPattern getPattern() {
		return pattern;
	}

	@Override
	public long getPatternId() {
		return pattern.getId();
	}

	@Override
	public Map<String, String> getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReportType getType() {
		//FIXME:
		return ReportType.RTF;
	}
}
