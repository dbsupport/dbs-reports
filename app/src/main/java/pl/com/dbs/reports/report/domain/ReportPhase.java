/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * report phase embedable.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Embeddable
public class ReportPhase {
	@Column(name = "phase")
	@Enumerated(EnumType.STRING)
	private ReportPhaseStatus status;
	
	@Column(name = "phase_date")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date date;	
	
	public ReportPhase() {/*JPA*/}
	
	ReportPhase(Date date) {
		this.status = ReportPhaseStatus.INIT;
		this.date = date;
	}
	
	public boolean is(ReportPhaseStatus status) {
		return this.status.equals(status);
	}
	
	public ReportPhase rephase(ReportPhaseStatus status) {
		this.status = status;
		this.date = new Date();
		return this;
	}

	public ReportPhaseStatus getStatus() {
		return status;
	}

	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return this.status.toString();
	}
	
	/**
	 * Phase of generation.
	 */
	public enum ReportPhaseStatus {
		INIT,
		START,
		READY,
		TRANSIENT,
		PERSIST;
	}	
}


