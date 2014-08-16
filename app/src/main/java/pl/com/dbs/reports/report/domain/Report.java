/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternFormat;
import pl.com.dbs.reports.support.db.domain.AEntity;



/**
 * 
 * Report entity.
 * This is report entity from beginning INIT to PERSIST (archived) state.
 * 
 * 
 *                  user orders          scheduler takes         generation          user          user
 *                   reports              processing              finish          confirms      archivize
 * ---------------------|---------------------|---------------------|----------------|--------------|-------
 * processing:         INIT                 START                 READY          TRANSIENT       PERSIST
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Entity
@Table(name = "tre_report")
public class Report extends AEntity implements pl.com.dbs.reports.api.report.Report {
	private static final long serialVersionUID = 391747562802238863L;
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_report", sequenceName = "sre_report", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_report")
	private Long id;	
	
	@Column(name = "generation_date")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date generationDate;
	
	@Column(name = "name")
	private String name;
	
	@Embedded
	private ReportPatternFormat format;	
	
    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="tre_report_parameter", joinColumns=@JoinColumn(name="report_id"))
    private Map<String, String> parameters = new HashMap<String, String>(); 
	
    @OneToOne
    @JoinColumn(name="pattern_id")	
	private ReportPattern pattern;
    
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="creator_id")
    private Profile creator;    
	
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ReportStatus status;
	
    @Embedded
    private ReportPhase phase;	
    
    @OrderBy
	@OneToMany(mappedBy="report", orphanRemoval=true)
    private List<ReportLog> logs = new ArrayList<ReportLog>();    
	    

//recznie obsluguj usuwanie ReportOrder bo powinien byc usuwany tylko jesli wszystkie raporty sa usuniete..    
//    @ManyToOne(fetch=FetchType.LAZY)//, cascade={CascadeType.REMOVE})
//    @JoinTable(
//            name="tre_order_report",
//            joinColumns = @JoinColumn( name="report_id"),
//            inverseJoinColumns = @JoinColumn( name="order_id"))
//    private ReportOrder order;    
	
    public Report() {/* JPA */}
    
    public Report(ReportPattern pattern, String name, Profile creator) {
    	Validate.notNull(pattern, "Pattern is no more!");
    	Validate.notNull(creator, "Profile is no more!");
    	Validate.notEmpty(name, "Name is no more!");
    	
    	this.generationDate = new Date();
    	Validate.notNull(pattern, "Pattern is no more!");
    	this.pattern = pattern;
		Validate.notNull(creator, "Profile is no more!");
		this.creator = creator;
    	this.name = name;
    	this.status = ReportStatus.OK;
    	this.phase = new ReportPhase(this.generationDate);
    }
    
    public Report start() {
    	if (!this.phase.is(ReportPhaseStatus.INIT))
    		throw new IllegalStateException("Report "+id+" has inproper phase("+phase+") for start!");
    		
    	this.phase.rephase(ReportPhaseStatus.START);
    	return this;
    }       
    
    /**
     * By using @ Transactional if there are any RuntimeExceptions thrown in the method, 
     * it will automatically perform the rollback
     */
    public Report restart() {
    	if (!this.phase.is(ReportPhaseStatus.START))
    		throw new IllegalStateException("Report "+id+" has inproper phase("+phase+") for restart!");
    	
    	this.phase.rephase(ReportPhaseStatus.INIT);
    	return this;
    }    
    
    public Report ready(byte[] content, List<ReportLog> logs) {
    	if (!this.phase.is(ReportPhaseStatus.START))
    		throw new IllegalStateException("Report "+id+" has inproper phase("+phase+") for ready!");
    		
    	this.phase.rephase(ReportPhaseStatus.READY);
    	this.content = content!=null&&content.length<=0?null:content;
    	this.logs = logs;
    	for (ReportLog log : logs) log.setReport(this);
    	return this;
    }   
    
    public Report failure(List<ReportLog> logs) {
    	if (this.phase.is(ReportPhaseStatus.READY)
    		||this.phase.is(ReportPhaseStatus.TRANSIENT))
    		throw new IllegalStateException("Report "+id+" has inproper phase("+phase+") for failure!");
    	
    	this.phase.rephase(ReportPhaseStatus.READY);
    	for (ReportLog log : logs) {
    		log.setReport(this);
    		this.logs.add(log);
    	}
    	this.status = ReportStatus.FAILED;
    	return this;
    }
    
    public Report timeout(ReportLog log) {
    	if (this.phase.is(ReportPhaseStatus.READY)
    		||this.phase.is(ReportPhaseStatus.TRANSIENT))
    		throw new IllegalStateException("Report "+id+" has inproper phase("+phase+") for failure!");
    	
    	this.phase.rephase(ReportPhaseStatus.READY);
    	this.content = null;
    	log.setReport(this);
    	this.logs.add(log);
    	this.status = ReportStatus.FAILED;
    	return this;
    }    
    
    public Report confirm() {
    	if (!this.phase.is(ReportPhaseStatus.READY))
    		throw new IllegalStateException("Report "+id+" has inproper phase("+phase+") for confirm!");
    		
    	this.phase.rephase(ReportPhaseStatus.TRANSIENT);
    	return this;
    }       
    
    public Report archive() {
    	if (!this.phase.is(ReportPhaseStatus.TRANSIENT)
    		&&!ReportStatus.OK.equals(this.status))
    		throw new IllegalStateException("Report "+id+" has inproper phase("+phase+") or status("+status+") for archive!");
    		
    	this.phase.rephase(ReportPhaseStatus.PERSIST);
    	return this;
    }      
    
    public Report format(PatternFormat format) {
    	this.format = new ReportPatternFormat(format.getReportType(), format.getReportExtension(), format.getPatternExtension());
    	return this;
    }
    
    public Report parameters(Map<String, String> params) {
    	this.parameters = params;
    	return this;
    }
    
	@Override
	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public Date getGenerationDate() {
		return generationDate;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}

	@Override
	public byte[] getContent() {
		return content;
	}
	
	public String getContentAsString() {
		return content!=null?new String(content):"";
	}
	
	public boolean isDownloadable() {
		return ReportStatus.OK.equals(status)
				&&phase.isFinishedUnarchived()
				&&(content!=null&&content.length>0);
	}

	@Override
	public PatternFormat getFormat() {
		return format;
	}
	
	public Profile getCreator() {
		return creator;
	}
	
	public ReportStatus getStatus() {
		return status;
	}

    
    public ReportPhase getPhase() {
		return phase;
	}


	public List<ReportLog> getLogs() {
		return logs;
	}


	/**
     * Status.
     */
    public enum ReportStatus {
    	OK,
    	FAILED;
    }
    
    
}
