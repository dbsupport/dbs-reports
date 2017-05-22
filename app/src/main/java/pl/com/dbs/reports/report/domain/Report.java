/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import com.google.common.collect.Lists;
import org.apache.commons.lang.Validate;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.api.report.pattern.PatternFormat;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.domain.ReportPhase.ReportPhaseStatus;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternFormat;
import pl.com.dbs.reports.report.pattern.domain.ReportPatternTransformate;
import pl.com.dbs.reports.support.db.domain.AEntity;

import javax.persistence.*;
import java.util.*;


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
 * @copyright (c) 2013
 */
@Entity
@Table(name = "tre_report")
//@SecondaryTable(name = "tre_report_content", pkJoinColumns = @PrimaryKeyJoinColumn(name = "report_id"))
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

	@OneToMany(mappedBy="report", orphanRemoval=true)
	//@OrderBy("name")
	private List<ReportParameter> parameters = new ArrayList<ReportParameter>();
	
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="pattern_id")	
	private ReportPattern pattern;
    
	@OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="creator_id")
    private Profile creator;

    /**
     * http://stackoverflow.com/questions/10108533/jpa-should-i-store-a-blob-in-the-same-table-with-fetch-lazy-or-should-i-store-i
     * http://www.hostettler.net/blog/2012/03/22/one-to-one-relations-in-jpa-2-dot-0/
     * http://java.dzone.com/articles/jpa-lazy-loading
     *
     * Lazy loading is necessary coz reports can be huge.
     * Lazy loading works (in that case) only if content is separate class AND when mapping is OneToMany.
     */
    @OneToMany(mappedBy="report", orphanRemoval=true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReportContent> content;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ReportStatus status;
	
    @Embedded
    private ReportPhase phase;	
    
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
    		throw new IllegalStateException("Report "+id+" has improper phase("+phase+") for start!");
    		
    	this.phase.rephase(ReportPhaseStatus.START);
    	return this;
    }       
    
    /**
     * By using @ Transactional if there are any RuntimeExceptions thrown in the method, 
     * it will automatically perform the rollback
     */
    public Report restart() {
    	if (!this.phase.is(ReportPhaseStatus.START))
    		throw new IllegalStateException("Report "+id+" has improper phase("+phase+") for restart!");
    	
    	this.phase.rephase(ReportPhaseStatus.INIT);
    	return this;
    }    
    
    public Report ready(byte[] content) {
    	if (!this.phase.is(ReportPhaseStatus.START))
    		throw new IllegalStateException("Report "+id+" has improper phase("+phase+") for ready!");
    		
    	this.phase.rephase(ReportPhaseStatus.READY);
    	//this.content = content!=null&&content.length<=0?null:content;
        this.content = content!=null&&content.length<=0? null:Lists.newArrayList(new ReportContent(content, this));
    	return this;
    }   
    
    public Report failure() {
    	if (this.phase.is(ReportPhaseStatus.READY)
    		||this.phase.is(ReportPhaseStatus.TRANSIENT))
    		throw new IllegalStateException("Report "+id+" has improper phase("+phase+") for failure!");
    	
    	this.phase.rephase(ReportPhaseStatus.READY);
    	this.status = ReportStatus.FAILED;
    	return this;
    }
    
    public Report timeout() {
    	if (this.phase.is(ReportPhaseStatus.READY)
    		||this.phase.is(ReportPhaseStatus.TRANSIENT))
    		throw new IllegalStateException("Report "+id+" has improper phase("+phase+") for failure!");
    	
    	this.phase.rephase(ReportPhaseStatus.READY);
    	this.content = null;
    	this.status = ReportStatus.FAILED;
    	return this;
    }    
    
    public Report confirm() {
    	if (!this.phase.is(ReportPhaseStatus.READY))
    		throw new IllegalStateException("Report "+id+" has improper phase("+phase+") for confirm!");
    		
    	this.phase.rephase(ReportPhaseStatus.TRANSIENT);
    	return this;
    }       
    
    public Report archive() {
    	if (!this.phase.is(ReportPhaseStatus.TRANSIENT)
    		&&!ReportStatus.OK.equals(this.status))
    		throw new IllegalStateException("Report "+id+" has improper phase("+phase+") or status("+status+") for archive!");
    		
    	this.phase.rephase(ReportPhaseStatus.PERSIST);
    	return this;
    }      
    
    public Report format(PatternFormat format) {
    	this.format = new ReportPatternFormat(format.getReportType(), format.getReportExtension(), format.getPatternExtension());
    	return this;
    }
    
    public Report parameters(List<ReportParameter> params) {
    	this.parameters = params;
		for (ReportParameter param : params) {
			param.report(this);
		}
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
	public List<? extends pl.com.dbs.reports.api.report.ReportParameter> getParameters() {
		return parameters;
	}

	public List<ReportParameter> getParametersAsList() {
		return parameters;
	}

	@Override
	public byte[] getContent() {
		return hasContent()?fetchContent().getContent():null;
	}
	
	public String getContentAsString() {
		return hasContent()?new String(fetchContent().getContent()):"";
	}
	
	public boolean isDownloadable() {
		return ReportStatus.OK.equals(status)
				&&phase.isFinishedUnarchived()
                &&(hasContent()&&fetchContent().hasContent());
				//&&(content!=null&&content.length>0);
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

    private boolean hasContent() {
        return content!=null&&!content.isEmpty();
    }

    private ReportContent fetchContent() {
        return content.get(0);
    }

	/**
     * Status.
     */
    public enum ReportStatus {
    	OK,
    	FAILED;
    }
    
    
}
