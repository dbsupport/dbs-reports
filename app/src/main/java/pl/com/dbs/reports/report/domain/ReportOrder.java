/**
 * 
 */
package pl.com.dbs.reports.report.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.com.dbs.reports.support.db.domain.IEntity;



/**
 * Groups generations.
 * Suposed to be deleted when all generations where archivized. 
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
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
	
	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)	
	private Date date;	
	
	@OneToMany(fetch=FetchType.EAGER, orphanRemoval=false)
    @JoinTable(
        name="tre_order_report",
        joinColumns = @JoinColumn( name="order_id"),
        inverseJoinColumns = @JoinColumn( name="report_id"))
    private List<Report> reports = new ArrayList<Report>();	

    public ReportOrder() {/* JPA */}
    
    public ReportOrder(Date date) {
    	this.date = date;
    }
    
    public ReportOrder add(Report report) {
    	this.reports.add(report);
    	return this;
    }

	public Long getId() {
		return id;
	}

	public List<Report> getReports() {
		return reports;
	}

	public Date getDate() {
		return date;
	}

	/**
	 * Confirm all generations.
	 */
	public ReportOrder confirm() {
		for (Report gen : reports) {
			gen.confirm();
		}
		return this;
	}
	
	public int count() {
		return this.reports.size();
	}
	
}
