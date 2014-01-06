/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.profile.dao.ProfileAuthorityDao;

/**
 * Authority service.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("profile.authority.service")
public class ProfileAuthorityService {
	//private static final Logger logger = Logger.getLogger(ProfileAuthorityService.class);
	@Autowired private ProfileAuthorityDao profileAuthorityDao;
	
	public Authority find(long id) {
		return profileAuthorityDao.find(id);
	}	
	
	public List<Authority> find() {
		return profileAuthorityDao.find();
	}
}
