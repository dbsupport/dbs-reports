/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.security.domain.SessionContext;
import pl.com.dbs.reports.support.db.domain.AEntity;



/**
 * TODO
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

    public Report() {/* JPA */}
    
    public Report(Pattern pattern, String name, Map<String, String> params) {
    	this.generationDate = new Date();
    	Validate.notNull(pattern, "Pattern is no more!");
    	this.pattern = (ReportPattern)pattern;
		Profile profile = SessionContext.getProfile();
		Validate.notNull(profile, "Profile is no more!");
		this.creator = profile;
		Validate.notEmpty(name, "Name is no more!");
    	this.name = name;
    	this.parameters = params;
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

	public Profile getCreator() {
		return creator;
	}

}
