/**
 * 
 */
package pl.com.dbs.reports.logging.dao;

import org.slf4j.Marker;

import pl.com.dbs.reports.logging.domain.LoggingEvent;
import pl.com.dbs.reports.logging.domain.LoggingEvent_;
import pl.com.dbs.reports.support.db.dao.AFilter;
import pl.com.dbs.reports.support.filter.Pager;

/**
 * Logging filter.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
public class LoggingEventFilter extends AFilter<LoggingEvent> {
	private static final long serialVersionUID = -6561674834966022844L;
	private static final int DEFAULT_PAGER_SIZE = 10;
	private Long rid;
	private Marker marker;
	
	public LoggingEventFilter() {
		getPager().setPageSize(DEFAULT_PAGER_SIZE);
		getSorter().add(LoggingEvent_.date.getName(), true);
	}
	
	public LoggingEventFilter rid(long rid) {
		this.rid = rid;
		return this;
	}
	
	public LoggingEventFilter limitless() {
		getPager().setPageSize(Pager.NO_LIMIT);
		return this;
	}

	public LoggingEventFilter marked(Marker marker) {
		this.marker = marker;
		return this;
	}	
	
	public Long getRid() {
		return rid;
	}

	public Marker getMarker() {
		return marker;
	}
}
