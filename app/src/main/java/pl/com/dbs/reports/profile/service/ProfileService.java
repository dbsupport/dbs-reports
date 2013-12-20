/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.profile.dao.ProfileAddressDao;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.dao.ProfilesFilter;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileAddException;
import pl.com.dbs.reports.profile.domain.ProfileAddress;
import pl.com.dbs.reports.profile.domain.ProfileCreation;

/**
 * TODO
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("profile.service")
public class ProfileService {
	private static final Logger logger = Logger.getLogger(ProfileService.class);
	@Autowired private ProfileAddressDao profileAddressDao;
	@Autowired private ProfileDao profileDao;
	
	public Profile find(String login) {
		return profileDao.find(login);
	}
	
	public List<Profile> find(ProfilesFilter filter) {
		return profileDao.find(filter);
	}
	
	@Transactional
	public void add(ProfileCreation form) throws ProfileAddException {
		logger.info("Adding new profile..");
		
		Profile profile = new Profile(form);
		if (!StringUtils.isBlank(form.getAddress().getCity())) {
			ProfileAddress address = new ProfileAddress(form.getAddress());
			profileAddressDao.create(address);
			profile.addAddress(address);
		}
		profileDao.create(profile);
	}
}
