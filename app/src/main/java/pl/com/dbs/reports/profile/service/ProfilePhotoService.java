/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.profile.dao.ProfilePhotoDao;
import pl.com.dbs.reports.profile.domain.ProfilePhoto;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("profile.photo.service")
public class ProfilePhotoService {
	//private static final Logger logger = Logger.getLogger(ProfilePhotoService.class);
	@Autowired private ProfilePhotoDao profilePhotoDao;
	
	public ProfilePhoto find(long id) {
		return profilePhotoDao.find(id);
	}	
}
