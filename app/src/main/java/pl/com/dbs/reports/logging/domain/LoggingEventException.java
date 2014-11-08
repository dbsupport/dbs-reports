/**
 * 
 */
package pl.com.dbs.reports.logging.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.com.dbs.reports.support.db.domain.IEntity;

/**
 * exception logging event data
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Entity
@Table(name = "logging_event_exception")
public class LoggingEventException implements IEntity {
	private static final long serialVersionUID = 2736226326428972752L;
	@Id
	private Long id;		
	
	@Column(name = "trace_line")
	private String line;

	@Column(name = "i")
	private int i;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="event_id")
	private LoggingEvent event;
	
	public LoggingEventException() {}
}
