/**
 * 
 */
package pl.com.dbs.reports.report.pattern.domain;

import java.io.Serializable;

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
 * @copyright (c) 2014
 */
@Embeddable
public class ReportPatternFormat implements PatternFormat, Serializable {
	private static final long serialVersionUID = 1L;

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
	
	public ReportPatternFormat(ReportType type, String rext, String pext) {
		this.type = type;
		this.reportExtension = rext;
		this.patternExtension = pext;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ReportPatternFormat format = (ReportPatternFormat) obj;
		
		boolean pext = false;
		if (StringUtils.isBlank(this.patternExtension)&&StringUtils.isBlank(format.getPatternExtension())) pext = true;
		if (!StringUtils.isBlank(this.patternExtension)&&this.patternExtension.equalsIgnoreCase(format.getPatternExtension())) pext = true;
		
		boolean rext = false;
		if (StringUtils.isBlank(this.reportExtension)&&StringUtils.isBlank(format.getReportExtension())) rext = true;
		if (!StringUtils.isBlank(this.reportExtension)&&this.reportExtension.equalsIgnoreCase(format.getReportExtension())) rext = true;
		
		boolean type = false;
		if (this.type==null&&format.getReportType()==null) type = true;
		if (this.type!=null&&this.type.equals(format.getReportType())) type = true;
		
		return pext&&rext&&type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reportExtension == null) ? 0 : reportExtension.hashCode());
		result = prime * result + ((patternExtension == null) ? 0 : patternExtension.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
}
