/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.com.dbs.reports.access.dao.AccessDao;
import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.api.profile.ClientProfile;
import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.profile.dao.ProfileAddressDao;
import pl.com.dbs.reports.profile.dao.ProfileAuthorityDao;
import pl.com.dbs.reports.profile.dao.ProfileDao;
import pl.com.dbs.reports.profile.dao.ProfileFilter;
import pl.com.dbs.reports.profile.dao.ProfileNoteDao;
import pl.com.dbs.reports.profile.dao.ProfilePhotoDao;
import pl.com.dbs.reports.profile.dao.ProfilesFilter;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileAddress;
import pl.com.dbs.reports.profile.domain.ProfileCreation;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.domain.ProfileModification;
import pl.com.dbs.reports.profile.domain.ProfileNote;
import pl.com.dbs.reports.profile.domain.ProfilePhoto;
import pl.com.dbs.reports.security.domain.SessionContext;

/**
 * Profiles services.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Service("profile.service")
public class ProfileService {
	private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
	@Autowired private ProfileAddressDao profileAddressDao;
	@Autowired private ProfileDao profileDao;
	@Autowired private AccessDao profileAccessDao;
	@Autowired private ProfileAuthorityDao profileAuthorityDao;
	@Autowired private ProfilePhotoDao profilePhotoDao;
	@Autowired private ProfileNoteDao profileNoteDao;
	
	
	public Profile findCurrent() {
		return profileDao.find(SessionContext.getProfile().getId());
	}
	
	public List<Profile> findByLogin(String login) {
		ProfileFilter filter = new ProfileFilter(login, null);
		return profileDao.find(filter);
	}
	
	public Profile findByLoginAccepted(String login) throws ProfileException {
		ProfileFilter filter = new ProfileFilter(login, null).accepted();
		List<Profile> profiles = profileDao.find(filter);
		if (profiles.size()>1) throw new ProfileException("profile.too.many.profiles.with.same.login", Arrays.asList(String.valueOf(profiles.size()), login));
		return profiles.isEmpty()?null:profiles.get(0);
	}
	
	public Profile findById(Long id) {
		return profileDao.find(id);
	}	
	
	public Profile findByUuid(String uuid) {
		return profileDao.findByUuid(uuid);
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
		
		profile.deactivate();
	}
	
	@Transactional
	public Profile accept(Long id) throws ProfileException {
		logger.info("Accepting profile.."+id);
		Validate.notNull(id, "Profile ID is no more!");
		
		Profile profile = profileDao.find(id);
		Validate.notNull(profile, "Profile is no more!");
		
		//if (profile.isAccepted()) throw new ProfileException();
		
		profile.accept();
		return profile;
	}
	
	@Transactional
	public Profile unaccept(Long id) throws ProfileException {
		logger.info("Unaccepting profile.."+id);
		Validate.notNull(id, "Profile ID is no more!");
		
		Profile profile = profileDao.find(id);
		Validate.notNull(profile, "Profile is no more!");
		
		profile.unaccept();
		return profile;
	}	
	
	@Transactional
	public Profile note(Long id, String content) throws ProfileException {
		logger.info("Noting profile.."+id);
		Validate.notNull(id, "Profile ID is no more!");
		
		Profile profile = profileDao.find(id);
		Validate.notNull(profile, "Profile is no more!");
		
		Profile editor = profileDao.find(SessionContext.getProfile().getId());
		
		ProfileNote note = profile.getNote();
		if (note==null) {
			note = new ProfileNote(content, editor); 
			profileNoteDao.create(note);
			profile.addNote(note);
		} else { 
			note.modify(content, editor);
		}
		
		return profile;
	}
	
	@Transactional
	public Profile modify(Long id, final ClientProfile clientprofile) {
		logger.info("Modyfying profile.."+id);
		Validate.notNull(id, "Profile ID is no more!");
		
		Profile profile = profileDao.find(id);
		Validate.notNull(profile, "Profile is no more!");
		
		logger.info("Profile "+clientprofile.getLogin()+" FOUND locally and is active. Updating description..");
		profile.modify(clientprofile.getDescription());
		return profile;
	}	
}
