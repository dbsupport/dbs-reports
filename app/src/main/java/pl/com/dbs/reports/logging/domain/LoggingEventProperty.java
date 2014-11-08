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
 * MDC properties of elogging event.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Entity
@Table(name = "logging_event_property")
public class LoggingEventProperty implements IEntity {
	private static final long serialVersionUID = -7127668679997968164L;
	
	@Id
	private Long id;		
	
	@Column(name = "mapped_key")
	private String key;

	@Column(name = "mapped_value")
	private String value;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="event_id")
	private LoggingEvent event;
	
	public LoggingEventProperty() {}
	
	
	
}
