/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import pl.com.dbs.reports.api.report.ReportLogType;
import pl.com.dbs.reports.support.db.domain.IEntity;



/**
 * Generation log entry.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Entity
@Table(name = "tre_log")
public class ReportLog implements IEntity, pl.com.dbs.reports.api.report.ReportLog {
	private static final long serialVersionUID = 391747562802238863L;
	private static final int MSG_LENGTH = 1024;
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_log", sequenceName = "sre_log", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_log")
	private Long id;	
	
	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date date;
	
	@Column(name = "msg")
	private String msg;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private ReportLogType type;	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="report_id")
	private Report report;	

    public ReportLog() {/* JPA */}
    
    public ReportLog(String msg, ReportLogType type) {
    	this.type = type;
    	this.msg = trim(msg);
    	this.date = new Date();
    }
	
    public void setReport(Report report) {
		this.report = report;
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public String getMsg() {
		return msg;
	}

	public ReportLogType getType() {
		return type;
	}

	private String trim(String msg) {
		if (StringUtils.isBlank(msg)) return "";
		if (msg.length()>MSG_LENGTH) {
			return msg.substring(0, MSG_LENGTH-2)+"..";
		}
		return msg;
	}

	
}
