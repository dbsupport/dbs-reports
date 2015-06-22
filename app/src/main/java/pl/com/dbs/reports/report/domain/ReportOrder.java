/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.support.db.domain.IEntity;



/**
 * Groups generations.
 * Starts with report(s) generation.
 * Stay alive as long as has any report and at least one report is not PERSISTED.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Entity
@Table(name = "tre_order")
public class ReportOrder implements IEntity {
	private static final long serialVersionUID = 391747562802238863L;
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_order", sequenceName = "sre_order", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_order")
	private Long id;	
	
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="creator_id")
    private Profile creator; 	
	
	@Column(name = "name")
	private String name;	
	
	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date start;
	
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date end;	
	
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ReportOrderStatus status;		
	
	@OneToMany(fetch=FetchType.LAZY, orphanRemoval=false)
    @JoinTable(
        name="tre_order_report",
        joinColumns = @JoinColumn( name="order_id"),
        inverseJoinColumns = @JoinColumn( name="report_id"))
    private List<Report> reports = new ArrayList<Report>();	

    public ReportOrder() {/* JPA */}
    
    public ReportOrder(String name, Profile profile) {
    	this.name = name;
    	this.creator = profile;
    	this.start = new Date();
    	this.status = ReportOrderStatus.NOTREADY;
    }
    
	public ReportOrder ready() {
		if (!ReportOrderStatus.NOTREADY.equals(this.status))
			throw new IllegalStateException("Reports' order "+id+" has inproper status("+status+") for ready!");
		
    	this.status = ReportOrderStatus.UNNOTIFIED;
    	this.end = new Date();
    	return this;
    }
	
	public ReportOrder notified() {
		if (!ReportOrderStatus.NOTREADY.equals(this.status)
			&&!ReportOrderStatus.UNNOTIFIED.equals(this.status))
			throw new IllegalStateException("Reports' order "+id+" has inproper status("+status+") for nofication!");
		
    	this.status = ReportOrderStatus.NOTIFIED;
    	return this;
    }
    
    public ReportOrder add(Report report) {
    	this.reports.add(report);
    	return this;
    }
    
    public ReportOrder remove(Report report) {
    	this.reports.remove(report);
    	return this;
    }    
    
    /**
     * All reports are finished?
     */
    public boolean isFinished() {
    	for (Report report : reports)
    		if (!report.getPhase().isFinished()) return false;
    	return true;
    }
    
    /**
     * All reports are TRANSIENT or READY?
     */
    public boolean isConfirmed() {
    	for (Report report : reports)
    		if (!report.getPhase().isConfirmed()) return false;
    	return true;
    }
    
//    /**
//     * All reports are PERSIST?
//     * @return
//     */
//    public boolean isArchived() {
//    	for (Report report : reports)
//    		if (!report.getPhase().isArchived()) return false;
//    	return true;
//    }    
    
    /**
     * Any reports in collection?
     */
    public boolean isEmpty() {
    	return this.reports.isEmpty();
    }    
    

	public Long getId() {
		return id;
	}

	public List<Report> getReports() {
		return reports;
	}

	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return end;
	}
	

//	/**
//	 * Confirm all generations.
//	 */
//	public ReportOrder confirm() {
//		for (Report gen : reports) {
//			gen.confirm();
//		}
//		return this;
//	}
	
	public ReportOrderStatus getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public Profile getCreator() {
		return creator;
	}

	public int count() {
		return this.reports.size();
	}
	
    /**
     * Status.
     */
    public enum ReportOrderStatus {
    	NOTREADY,
    	UNNOTIFIED, //user was NOT notified
    	NOTIFIED; //user was notified
    }	
	
}
