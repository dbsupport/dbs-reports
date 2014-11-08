/**
 * 
 */
package pl.com.dbs.reports.logging.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;

import pl.com.dbs.reports.api.report.ReportLoggings;
import pl.com.dbs.reports.logging.domain.LoggingEvent;
import pl.com.dbs.reports.logging.domain.LoggingEventProperty;
import pl.com.dbs.reports.logging.domain.LoggingEventProperty_;
import pl.com.dbs.reports.logging.domain.LoggingEvent_;
import pl.com.dbs.reports.support.db.dao.ADao;
import pl.com.dbs.reports.support.db.dao.ContextDao;
import pl.com.dbs.reports.support.db.dao.IContextDao;

/**
 * Logging CRUD.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Repository
public class LoggingEventDao extends ADao<LoggingEvent, Long> {

	@PersistenceContext
	private EntityManager em;	
	
	@Override
	public EntityManager getEntityManager() {
		return em;
	}
	
	public List<LoggingEvent> find(final LoggingEventFilter filter) {
		IContextDao<LoggingEvent> c = new ContextDao<LoggingEvent>(em, LoggingEvent.class, filter);
		
	    Predicate p = c.getBuilder().conjunction();
	    
	    if (filter.getRid()!=null) {
	    	Join<LoggingEvent, LoggingEventProperty> join = c.getRoot().join(LoggingEvent_.properties, JoinType.LEFT);
    		p = c.getBuilder().and(p, c.getBuilder().equal(join.get(LoggingEventProperty_.key), ReportLoggings.MDC_ID));
    		p = c.getBuilder().and(p, c.getBuilder().equal(join.get(LoggingEventProperty_.value), String.valueOf(filter.getRid())));
	    }
    	if (filter.getMarker()!=null) {
    		Join<LoggingEvent, LoggingEventProperty> join = c.getRoot().join(LoggingEvent_.properties, JoinType.LEFT);
    		p = c.getBuilder().and(p, c.getBuilder().equal(join.get(LoggingEventProperty_.key), filter.getMarker().getName()));
    		p = c.getBuilder().and(p, c.getBuilder().equal(join.get(LoggingEventProperty_.value), Boolean.TRUE.toString()));
	    }
	    
	    c.getCriteria().where(p);
		
		return executeQuery(c);
	}
	

}
