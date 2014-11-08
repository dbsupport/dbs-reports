/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.report.domain.ReportGenerationContext;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.web.form.DForm;


/**
 * Report generation form.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "form", namespace = "http://www.dbs.com.pl/reports/1.0/form")
public class ReportGenerationForm extends DForm implements ReportGenerationContext {
	private static final long serialVersionUID = -5420902467886273804L;
	public static final String KEY = "reportGenerationForm";
	private Long pattern;
	private String name;
	private PatternFormat format;
	/**
	 * ...to satisfy interface..
	 */
	private List<PatternFormat> formats;
	
	public ReportGenerationForm() {
		super();
	}
	
	public void reset(ReportPattern pattern) {
		super.reset();
		this.pattern = pattern.hasId()?pattern.getId():null;
		this.name = pattern.getManifest().getNameTemplate();
		this.formats = pattern.getFormats();
		if (this.formats.size()==1) this.format = formats.get(0);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setExtension(String value) {
		for (PatternFormat format : formats) {
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

	@Override
	public long getPattern() {
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
