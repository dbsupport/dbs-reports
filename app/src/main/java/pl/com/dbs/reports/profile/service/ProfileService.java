/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.access.dao.AccessDao;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.profile.dao.ProfileAddressDao;
import pl.com.dbs.reports.profile.dao.ProfileAuthorityDao;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.dao.ProfilePhotoDao;
import pl.com.dbs.reports.profile.dao.ProfilesFilter;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileAddress;
import pl.com.dbs.reports.profile.domain.ProfileCreation;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.domain.ProfileModification;
import pl.com.dbs.reports.profile.domain.ProfilePhoto;

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
	@Autowired private AccessDao profileAccessDao;
	@Autowired private ProfileAuthorityDao profileAuthorityDao;
	@Autowired private ProfilePhotoDao profilePhotoDao;
	
	public Profile find(String login) {
		return profileDao.find(login);
	}
	
	public Profile find(Long id) {
		return profileDao.find(id);
	}	
	
	public List<Profile> find(ProfilesFilter filter) {
		return profileDao.find(filter);
	}
	
	/**
	 * Brand new profile.
	 */
	@Transactional
	public void add(ProfileCreation form) throws ProfileException {
		logger.info("Adding new profile..");
		
		Profile profile = new Profile(form);
		if (form.getAddress()!=null) {
			ProfileAddress address = new ProfileAddress(form.getAddress());
			profileAddressDao.create(address);
			profile.addAddress(address);
		}
		
		if (form.getAccesses()!=null) {
			for (Access access : form.getAccesses()) 
				profile.addAccess(profileAccessDao.find(access.getId()));
		}
		
		if (form.getAuthorities()!=null) {
			for (Authority authority : form.getAuthorities()) 
				profile.addAuthority(profileAuthorityDao.find(authority.getId()));
		}
		
		if (form.getPhoto()!=null) {
			try {
				ProfilePhoto photo = new ProfilePhoto(form.getPhoto());
				profilePhotoDao.create(photo);
				profile.addPhoto(photo);
			} catch (IOException e) {
				logger.error("Error creating profile photo. Details:"+e.getMessage(), e);
				throw new ProfileException("profile.photo.error");
			}
		} else profile.removePhoto();
		
		profileDao.create(profile);
	}
	
	/**
	 * Modify profile.
	 */
	@Transactional
	public void edit(ProfileModification form) throws ProfileException {
		logger.info("Modifying profile..");
		
		Profile profile = profileDao.find(form.getId());
		Validate.notNull(profile, "Profile is no more!");
		
		profile.modify(form);
		
		profile.removeAddress();
		if (form.getAddress()!=null) {
			ProfileAddress address = new ProfileAddress(form.getAddress());
			profileAddressDao.create(address);
			profile.addAddress(address);
		}

		profile.removeAccesses();
		if (form.getAccesses()!=null) {
			for (Access access : form.getAccesses()) 
				profile.addAccess(profileAccessDao.find(access.getId()));
		}
		
		profile.removeAuthorities();
		if (form.getAuthorities()!=null) {
			for (Authority authority : form.getAuthorities()) 
				profile.addAuthority(profileAuthorityDao.find(authority.getId()));
		}
		
		profile.removePhoto();
		if (form.getPhoto()!=null) {
			try {
				ProfilePhoto photo = new ProfilePhoto(form.getPhoto());
				profilePhotoDao.create(photo);
				profile.addPhoto(photo);
			} catch (IOException e) {
				logger.error("Error creating profile photo. Details:"+e.getMessage(), e);
				throw new ProfileException("profile.photo.error");
			}
		}
	}
	
	@Transactional
	public void delete(Long id) throws ProfileException {
		logger.info("Deleting profile..");
		Validate.notNull(id, "Profile ID is no more!");
		
		Profile profile = profileDao.find(id);
		Validate.notNull(profile, "Profile is no more!");
		
		//profileDao.erase(profile);
	}
}
