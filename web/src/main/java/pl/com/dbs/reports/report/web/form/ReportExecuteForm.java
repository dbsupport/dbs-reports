/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportGeneration;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.web.form.DForm;

import com.google.common.collect.Lists;


/**
 * Report generation form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
public class ReportExecuteForm extends DForm implements ReportGeneration {
	public static final String KEY = "reportGenerationForm";
	private ReportPattern pattern;
	private String name;
	private PatternFormat format;
	private Report report;
	
	public ReportExecuteForm() {
		super();
	}
	
	public void reset(ReportPattern pattern) {
		super.reset();
		this.pattern = pattern;
		this.name = pattern.getManifest().getNameTemplate();
		List<PatternFormat> formats = getFormats();
		if (formats.size()==1) this.format = formats.get(0);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFormat(String value) {
		for (PatternFormat format : getFormats()) {
			if (format.getExt().equalsIgnoreCase(value)) {
				this.format = format;
				return;
			}
		}
		this.format = null;
	}

	public void addReport(Report report) {
		this.report = report;
	}
	
	public Report getReport() {
		return report;
	}
	
	public List<PatternFormat> getFormats() {
		Set<PatternFormat> result = new HashSet<PatternFormat>();
		if (pattern!=null) {
			for (PatternTransformate transformate : pattern.getTransformates())
				result.add(transformate.getFormat());
		}
		return Lists.newArrayList(result);
	}
	
	@Override
	public ReportPattern getPattern() {
		return pattern;
	}

	@Override
	public PatternFormat getFormat() {
		return format;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public String getFullname() {
		return name+"."+format.getExt();
	}	
}
