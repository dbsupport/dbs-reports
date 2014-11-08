/**
 * 
 */
package pl.com.dbs.reports.report.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.Report;
import pl.com.dbs.reports.report.domain.Report.ReportStatus;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;
import pl.com.dbs.reports.report.domain.Report_;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.db.dao.AFilter;

import com.google.inject.internal.Lists;

/**
 * Report filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
public class ReportFilter extends AFilter<Report> {
	private static final long serialVersionUID = -6561674834966022844L;
	
	private static final int DEFAULT_PAGER_SIZE = 10;
	private List<Long> id;
	private String name;
	private Long profileId;
	private List<String> accesses = new ArrayList<String>();
	private List<ReportPhaseStatus> phases = new ArrayList<ReportPhaseStatus>();
	private List<ReportStatus> statuses = new ArrayList<ReportStatus>();
	
	public ReportFilter(Integer limit) {
		if (limit != null) getPager().setDataSize(limit);
	}
	
	public ReportFilter() {
		Profile profile = SessionContext.getProfile();
		Validate.notNull(profile, "Profile is no more!");
		this.profileId = profile.getId();
		this.accesses = new ArrayList<String>();
		for (Access access : profile.getAccesses())
			this.accesses.add(access.getName());
		
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add(Report_.generationDate.getName(), false);
		getSorter().add(Report_.name.getName(), true);
		getSorter().add(Report_.format.getName(), true);
	}
	
	public ReportFilter unarchived() {
		this.phases = new ArrayList<ReportPhaseStatus>();
		this.phases.add(ReportPhaseStatus.READY);
		this.phases.add(ReportPhaseStatus.TRANSIENT);
		return this;
	}

	public ReportFilter archived() {
		this.phases = new ArrayList<ReportPhaseStatus>();
		this.phases.add(ReportPhaseStatus.PERSIST);
		return this;
	}
	
	public ReportFilter fine() {
		this.statuses = new ArrayList<ReportStatus>();
		this.statuses.add(ReportStatus.OK);
		return this;
	}
	
	public ReportFilter failed() {
		this.statuses = new ArrayList<ReportStatus>();
		this.statuses.add(ReportStatus.FAILED);
		return this;
	}
	
	public ReportFilter withName(String name) {
		this.name = name;
		return this;
	}
	
	public ReportFilter onlyFor(long id) {
		this.id = Lists.newArrayList(id);
		return this;
	}
	
	public ReportFilter onlyFor(Long[] ids) {
		this.id = Lists.newArrayList(ids);
		return this;
	}	
	
	public ReportFilter forAnyone() {
		this.profileId = null;
		return this;
	}
	
	public ReportFilter onlyFor(Profile profile) {
		this.profileId = profile.getId();
		return this;
	}
	
	
	public ReportFilter inStatuses(List<ReportStatus> statuses) {
		this.statuses = statuses;
		return this;
	}
	
	public ReportFilter inPhases(List<ReportPhaseStatus> phases) {
		this.phases = phases;
		return this;
	}
	
	
	
	public List<String> getAccesses() {
		return accesses;
	}

	public List<Long> getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getProfileId() {
		return profileId;
	}

	public List<ReportPhaseStatus> getPhases() {
		return phases;
	}

	public List<ReportStatus> getStatuses() {
		return statuses;
	}
}
