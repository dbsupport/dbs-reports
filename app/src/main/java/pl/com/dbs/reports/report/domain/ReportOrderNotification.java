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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;

import pl.com.dbs.reports.support.db.domain.IEntity;



/**
 * While generation order is finished this object occures.
 * Its stays till user take some actions to make generations TRANSIENT.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
@Entity
@Table(name = "tre_ordernot")
public class ReportOrderNotification implements IEntity {
	private static final long serialVersionUID = 391747562802238863L;
	
	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "sg_ordernot", sequenceName = "sre_ordernot", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_ordernot")
	private Long id;
	
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="order_id")	
	private ReportOrder order;
	
	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date date;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private PreReportOrderNotificationStatus status;	
	
    public ReportOrderNotification() {/* JPA */}
    
    public ReportOrderNotification(ReportOrder order) {
    	Validate.notNull(order, "Generation order is no more!");
    	
    	this.date = new Date();
    	this.order = order;
    	this.status = PreReportOrderNotificationStatus.UNNOTIFIED;
    }
    
    public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public ReportOrder getOrder() {
		return order;
	}

	public Date getDate() {
		return date;
	}

	public PreReportOrderNotificationStatus getStatus() {
		return status;
	}

	public ReportOrderNotification notified() {
    	this.date = new Date();
    	this.status = PreReportOrderNotificationStatus.NOTIFIED;
    	return this;
    }
    
    
    /**
     * Status.
     */
    public enum PreReportOrderNotificationStatus {
    	UNNOTIFIED, //user was NOT notified
    	NOTIFIED; //user was notified
    }
    
}
