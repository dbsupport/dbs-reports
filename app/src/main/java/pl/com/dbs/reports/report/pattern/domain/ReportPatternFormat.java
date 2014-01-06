/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import pl.com.dbs.reports.api.report.ReportFormat;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;

/**
 * What type of transformation it is?
 * What extensfion file shuld have?
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Embeddable
public class ReportPatternFormat implements PatternFormat {
	@Column(name = "format")
	@Enumerated(EnumType.STRING)
	private ReportFormat format;
	
	@Column(name = "ext")
	private String ext;
	
	public ReportPatternFormat() {}
	
	public ReportPatternFormat(ReportFormat format, String ext) {
		this.format = format;
		this.ext = ext;
	}
	
	public ReportPatternFormat(ReportFormat format) {
		this.format = format;
		this.ext = format.getDefaultExt();
	}

	@Override
	public ReportFormat getFormat() {
		return format;
	}

	@Override
	public String getExt() {
		return ext;
	}
	
	@Override
	public String toString() {
		return format+"|"+ext;
	}
}
