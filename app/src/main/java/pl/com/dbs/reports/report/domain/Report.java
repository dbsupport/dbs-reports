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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import pl.com.dbs.reports.api.report.ReportFormat;
import pl.com.dbs.reports.api.report.pattern.Pattern;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.db.domain.AEntity;



/**
 * Report entity.
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
	
	@Column(name = "format")
	@Enumerated(EnumType.STRING)
	private ReportFormat format;	
	
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
	
	@Column(name = "temporary")
	private boolean temporary;	
    
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;

    public Report() {/* JPA */}
    
    public Report(ReportCreation context) {
    	this.generationDate = new Date();
    	Validate.notNull(context.getPattern(), "Pattern is no more!");
    	this.pattern = context.getPattern();
		Validate.notNull(context.getProfile(), "Profile is no more!");
		this.creator = context.getProfile();
		Validate.notEmpty(context.getName(), "Name is no more!");
    	this.name = context.getName();
    	this.content = context.getContent();
    	this.format = context.getFormat().getFormat();
    	this.parameters = context.getParams();
    	this.temporary = context.getTemporary();
    }
    
    public Report archive() {
    	this.temporary = false;
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

	@Override
	public ReportFormat getFormat() {
		return format;
	}
	
	public Profile getCreator() {
		return creator;
	}

}
