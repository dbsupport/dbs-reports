/**
 * 
 */
package pl.com.dbs.reports.logging.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * Logging entity.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Entity
@Table(name = "logging_event")
public class LoggingEvent implements IEntity {
	private static final long serialVersionUID = 7992561174666968085L;
	
	@Id
	@Column(name = "event_id")
	private Long id;	
	
	@Column(name = "timestmp")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	@Column(name = "formatted_message")
	private String msg;
	@Column(name = "level_string")
	private String level;
	@Column(name = "reference_flag")
	private int flag;
	
	@Column(name = "caller_class")
	private String clazz;
	@Column(name = "caller_method")
	private String method;
	@Column(name = "caller_line")
	private int line;
	
	@OneToMany(mappedBy="event", orphanRemoval=true)
    private List<LoggingEventProperty> properties = new ArrayList<LoggingEventProperty>();

	@OrderColumn(name="i")
	@OneToMany(mappedBy="event", orphanRemoval=true)
    private List<LoggingEventException> exceptions = new ArrayList<LoggingEventException>();
	
	public LoggingEvent() {}
	

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}
	
	public String getMsg() {
		return msg;
	}

	public String getLevel() {
		return level;
	}

	public int getFlag() {
		return flag;
	}


	public String getClazz() {
		return clazz;
	}


	public String getMethod() {
		return method;
	}


	public int getLine() {
		return line;
	}


	public List<LoggingEventProperty> getProperties() {
		return properties;
	}


	public List<LoggingEventException> getExceptions() {
		return exceptions;
	}


}
