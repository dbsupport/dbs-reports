/**
 * 
 */
package pl.com.dbs.reports.access.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.access.dao.AccessDao;
import pl.com.dbs.reports.access.dao.AccessFilter;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.access.domain.AccessCreation;
import pl.com.dbs.reports.access.domain.AccessModification;

/**
 * Access service.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Service("access.service")
public class AccessService {
	//private static final Logger logger = Logger.getLogger(ProfileAccessService.class);
	@Autowired private AccessDao accessDao;

	/**
	 * Get access by id; 
	 */
	public Access find(long id) {
		return accessDao.find(id);
	}

	/**
	 * Get accesses by filter
	 */
	public List<Access> find(AccessFilter filter) {
		return accessDao.find(filter);
	}

	/**
	 * Get ALL accesses
	 */
	public List<Access> find() {
		AccessFilter filter = new AccessFilter();
		filter.getPager().setNoLimitPageSize();
		return find(filter);
	}
	
	@Transactional
	public Access add(AccessCreation form) {
		Access access = new Access(form);
		accessDao.create(access);
		return access;
	}
	
	@Transactional
	public void edit(AccessModification form) {
		Access access = accessDao.find(form.getId());
		access.modify(form);
	}
	
	@Transactional
	public void delete(long id) {
		
	}
	
	
	
}
