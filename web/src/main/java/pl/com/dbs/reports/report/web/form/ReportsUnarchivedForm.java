/**
 * 
 */
package pl.com.dbs.reports.report.web.form;

import java.util.ArrayList;
import java.util.List;

import pl.com.dbs.reports.report.dao.ReportFilter;
import pl.com.dbs.reports.report.domain.Report.ReportStatus;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;

import com.google.common.collect.Lists;

/**
 * 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class ReportsUnarchivedForm {
	public static final String KEY = "reportsunarchivedform";
	
	private List<Long> id;
	private String value;
	private Phase phase = Phase.ALL;
	private Status status = Status.ALL;
	
	private Action action;
	
	private ReportFilter filter;
	
	public ReportsUnarchivedForm() {
		filter = new ReportFilter().inPhases(phase.getPhases());
	}
	
	public ReportsUnarchivedForm reset() {
		this.id = new ArrayList<Long>();
		this.action = null;
		this.filter.onlyFor(new Long[]{});
		return this;
	}
	
	public ReportsUnarchivedForm reset(Long[] ids) {
		this.id = new ArrayList<Long>();
		this.action = null;
		filter = new ReportFilter().inPhases(phase.getPhases()).onlyFor(ids);
		return this;
	}	
	
	public boolean anyIDselected() {
		return id!=null&&!id.isEmpty();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public List<Long> getId() {
		return id;
	}
	
	public Long[] getIdAsArray() {
		return id.toArray(new Long[]{});
	}	

	public void setId(List<Long> id) {
		this.id = id;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Phase getPhase() {
		return phase;
	}

	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	public ReportFilter getFilter() {
		return filter;
	}

	public enum Action {
		ARCHIVE, REMOVE, CONFIRM;
	}
	
	public enum Phase {
		ALL("wszystkie", ReportPhaseStatus.INIT, ReportPhaseStatus.START, ReportPhaseStatus.READY, ReportPhaseStatus.TRANSIENT),
		INITIALIZED("zlecone", ReportPhaseStatus.INIT, ReportPhaseStatus.START),
		READY("gotowe", ReportPhaseStatus.READY),
		TRANSIENT("niezarchowiz.", ReportPhaseStatus.TRANSIENT);

		private String label;
		private List<ReportPhaseStatus> phases;
		
		private Phase(String label, ReportPhaseStatus... phases) {
			this.label = label;
			this.phases = Lists.newArrayList(phases);
		}

		public String getLabel() {
			return label;
		}

		public List<ReportPhaseStatus> getPhases() {
			return phases;
		}		
	}
	
	public enum Status {
		ALL("wszystkie", ReportStatus.OK, ReportStatus.FAILED),
		OK("poprawne", ReportStatus.OK),
		FAILED("błędne", ReportStatus.FAILED);

		private String label;
		private List<ReportStatus> statuses;
		
		private Status(String label, ReportStatus... statuses) {
			this.label = label;
			this.statuses = Lists.newArrayList(statuses);
		}

		public String getLabel() {
			return label;
		}

		public List<ReportStatus> getStatuses() {
			return statuses;
		}		
	}

}
