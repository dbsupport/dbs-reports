/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.ReportGeneration;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.web.form.DForm;


/**
 * Report generation form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
public class ReportGenerationForm extends DForm implements ReportGeneration {
	public static final String KEY = "reportGenerationForm";
	private ReportPattern pattern;
	private String name;
	private PatternFormat format;
	private Report report;
	
	public ReportGenerationForm() {
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

	public void setExtension(String value) {
		for (PatternFormat format : getFormats()) {
			if (format.getReportExtension().equalsIgnoreCase(value)) {
				this.format = format;
				return;
			}
		}
		this.format = null;
	}
	
	public String getExtension() {
		return this.format!=null?format.getReportExtension():"";
	}

	public void addReport(Report report) {
		this.report = report;
	}
	
	public Report getReport() {
		return report;
	}
	
	public List<PatternFormat> getFormats() {
		return pattern!=null?pattern.getFormats():new ArrayList<PatternFormat>();
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
		return name+"."+format.getReportExtension();
	}	
}
