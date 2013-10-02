/**
 * 
 */
package pl.com.dbs.reports.report.web.view;

import java.util.jar.Attributes;

import pl.com.dbs.reports.report.api.Pattern;

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
	
	public ReportPatternView(Pattern pattern) {
		Attributes attrs = pattern.getManifest().getAttributes(Pattern.ATTRIBUTE_SECTION);
		this.name = attrs.getValue(Pattern.ATTRIBUTE_NAME);
		this.version = attrs.getValue(Pattern.ATTRIBUTE_VERSION);
		this.factory = attrs.getValue(Pattern.ATTRIBUTE_FACTORY);
		this.author = attrs.getValue(Pattern.ATTRIBUTE_AUTHOR);
		this.roles = attrs.getValue(Pattern.ATTRIBUTE_ROLES);
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
}
