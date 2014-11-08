/**
 * 
 */
package pl.com.dbs.reports.logging.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.logging.dao.LoggingEventDao;
import pl.com.dbs.reports.logging.dao.LoggingEventFilter;

/**
 * Logging events service.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Service
public class LoggingService {
	@Autowired private LoggingEventDao dao;
	
	@Transactional
	public void delog(final long id) {
		dao.erase(dao.find(new LoggingEventFilter().rid(id).limitless()));
	}
}
