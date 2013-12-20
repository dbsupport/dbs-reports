/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class PatternFilter extends AFilter<ReportPattern> {
	private static final int DEFAULT_PAGER_SIZE = 10;
	private Long id;
	private String name;
	private String version;
	private Date uploadDate;
	private String author;
	private String creator;
	
	private String factory;
	private List<String> accesses = new ArrayList<String>();

	public PatternFilter() {
		Profile profile = SessionContext.getProfile();
		Validate.notNull(profile, "Profile is no more!");
		this.accesses = new ArrayList<String>();
		for (Access access : profile.getAccesses())
			this.accesses.add(access.getName());
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add("uploadDate", false);
		getSorter().add("name", true);
	}
	
	public PatternFilter(long id) {
		this();
		this.id = id;
	}
	
	public PatternFilter(String name, String version, String factory) {
		this();
		this.name = name;
		this.version = version;
		this.factory = factory;
	}
	
	public PatternFilter(Access access) {
		this.accesses.add(access.getName());
	}

	public void putName(String name) {
		this.name = name;
	}
	
	public void putVersion(String version) {
		this.version = version;
	}
	
	public void putUploadDate(Date date) {
		this.uploadDate = date;
	}
	
	public void putAuthor(String author) {
		this.author = author;
	}
	
	public void putCreator(String creator) {
		this.creator = creator;
	}
	
	
	
	public List<String> getAccesses() {
		return accesses;
	}
	
	public Long getId() {
		return id;
	}


	public String getVersion() {
		return version;
	}


	public String getName() {
		return name;
	}


	public String getFactory() {
		return factory;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public String getAuthor() {
		return author;
	}

	public String getCreator() {
		return creator;
	}
}
