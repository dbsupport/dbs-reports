/**
 * 
 */
package pl.com.dbs.reports.report.pattern.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern_;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.db.dao.AFilter;

/**
 * Report pattern filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class PatternFilter extends AFilter<ReportPattern> {
	private static final long serialVersionUID = 4271784642529890380L;
	
	private static final int DEFAULT_PAGER_SIZE = 10;
	private Long id;
	private String name;
	private String version;
	private Long profileId;
	
	private String factory;
	private List<String> accesses = new ArrayList<String>();

	public PatternFilter() {
		resetAccesses();
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add(ReportPattern_.name.getName(), true);
		//getSorter().add(ReportPattern_.uploadDate.getName(), false);
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
	
	public PatternFilter forAccess(String access) {
		if (!StringUtils.isBlank(access)) {
			Profile profile = SessionContext.getProfile();
			if (profile.hasAccess(access)) {
				this.accesses = new ArrayList<String>();
				this.accesses.add(access);
			}
		} else resetAccesses();
		return this;
	}
	

	public PatternFilter forName(String name) {
		this.name = name;
		return this;
	}
	
	public PatternFilter forProfile(Long id) {
		this.profileId = id;
		return this;
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

	public Long getProfileId() {
		return profileId;
	}
	
	private void resetAccesses() {
		Profile profile = SessionContext.getProfile();
		Validate.notNull(profile, "Profile is no more!");
		
		this.accesses = new ArrayList<String>();
		/**
		 * ..all profile accesses..
		 */
		for (Access access : profile.getAccesses())
			this.accesses.add(access.getName());		
	}
	
}
