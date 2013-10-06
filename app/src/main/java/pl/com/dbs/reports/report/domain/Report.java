/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Date;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.com.dbs.reports.api.inner.report.pattern.Pattern;
import pl.com.dbs.reports.report.pattern.domain.ReportPattern;
import pl.com.dbs.reports.support.db.domain.AEntity;



/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
public class Report extends AEntity implements pl.com.dbs.reports.api.inner.report.Report {
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
	
	@Column(name = "params")
	private String params;
	
    @OneToOne
    @JoinColumn(name="pattern_id")	
	private ReportPattern pattern;
    
	@Lob
	@Column(name = "content")
	@Basic(fetch = FetchType.LAZY)
	private byte[] content;

    public Report() {/* JPA */}
    
    public Report(ReportPattern pattern, String name, String params) {
    	this.generationDate = new Date();
    	this.pattern = pattern;
    	this.name = name;
    	this.params = params;
    }
    
	@Override
	public Pattern getPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getGenerationDate() {
		// TODO Auto-generated method stub
		return null;
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
	public Map<String, String> getParams() {
		return null;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

}
