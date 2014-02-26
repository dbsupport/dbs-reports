/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.api.report.ReportType;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;

/**
 * What type of transformation it is (engine)?
 * What extension file should have?
 * What extension pattern it is?
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Embeddable
public class ReportPatternFormat implements PatternFormat {
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ReportType type;
	
	@Column(name = "report_extension")
	private String reportExtension;
	
	@Column(name = "pattern_extension")
	private String patternExtension;
	
	public ReportPatternFormat() {}
	
	public ReportPatternFormat(String filename, ReportType type, String rext) {
		this.type = type;
		this.reportExtension = rext;
		this.patternExtension = resolveExt(filename);
	}
	
	/**
	 * Default is TXT
	 */
	public ReportPatternFormat(String filename) {
		this.type = ReportType.TXT;
		this.reportExtension = ReportType.TXT.getDefaultExt();
		this.patternExtension = resolveExt(filename);
	}
	
	@Override
	public ReportType getReportType() {
		return type;
	}

	@Override
	public String getReportExtension() {
		return reportExtension;
	}
	
	@Override
	public String getPatternExtension() {
		return patternExtension;
	}
	
	@Override
	public String toString() {
		return type+"|"+reportExtension+"|"+patternExtension;
	}
	
	private static String resolveExt(String value) {
		String ext = value.lastIndexOf(".")>=0&&value.lastIndexOf(".")<value.length()?value.substring(value.lastIndexOf(".")+1):null;
		return StringUtils.isBlank(ext)?null:ext;		
	}
	
}
