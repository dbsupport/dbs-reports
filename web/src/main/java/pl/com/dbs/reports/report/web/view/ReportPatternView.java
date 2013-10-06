/**
 * 
 */
package pl.com.dbs.reports.report.web.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.api.inner.report.pattern.Pattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class ReportPatternView {
	private String name;
	private String version;
	private String factory;
	private String author;
	private String roles;
	private String manifest;
	private Pattern pattern;
	
	public static List<ReportPatternView> build(List<ReportPattern> patterns) {
		List<ReportPatternView> result = new ArrayList<ReportPatternView>();
		for (ReportPattern pattern : patterns) result.add(new ReportPatternView(pattern));
		return result;
	}
	
	public ReportPatternView(Pattern pattern) {
		this.name = pattern.getAttribute(Pattern.ATTRIBUTE_PATTERN_NAME);
		this.version = pattern.getAttribute(Pattern.ATTRIBUTE_PATTERN_VERSION);
		this.author = pattern.getAttribute(Pattern.ATTRIBUTE_PATTERN_AUTHOR);
		this.roles = pattern.getAttribute(Pattern.ATTRIBUTE_ROLES);
		
		this.factory = pattern.getAttribute(Pattern.ATTRIBUTE_PATTERN_FACTORY);
		if (StringUtils.isBlank(this.factory)) this.factory = "report.pattern.factory.default";
		
		try {
			OutputStream os = new ByteArrayOutputStream();
			pattern.getManifest().write(os);
			os.close();
			this.manifest = os.toString();
		} catch (IOException e) {}		
		
		this.pattern = pattern;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getFactory() {
		return factory;
	}

	public String getAuthor() {
		return author;
	}

	public String getRoles() {
		return roles;
	}

	public String getManifest() {
		return manifest;
	}

	public Date getUploadDate() {
		return pattern.getUploadDate();
	}
	
	public long getId() {
		return pattern.getId();
	}	
}
