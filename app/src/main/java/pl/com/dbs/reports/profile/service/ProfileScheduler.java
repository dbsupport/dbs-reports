/**
 * 
 */
package pl.com.dbs.reports.profile.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pl.com.dbs.reports.access.domain.Access;
import pl.com.dbs.reports.api.profile.ClientProfile;
import pl.com.dbs.reports.api.profile.ClientProfileAuthority;
import pl.com.dbs.reports.api.profile.ClientProfileService;
import pl.com.dbs.reports.authority.dao.AuthorityDao;
import pl.com.dbs.reports.authority.domain.Authority;
import pl.com.dbs.reports.profile.domain.Profile;
import pl.com.dbs.reports.profile.domain.ProfileException;
import pl.com.dbs.reports.profile.domain.ProfileGlobalCreation;

/**
 * Tasks to synchronize profiles between CLIENT db and local db.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Service("profile.scheduler")
public class ProfileScheduler {
	private static final Logger logger = Logger.getLogger(ProfileScheduler.class);
	@Autowired(required=false) private ClientProfileService clientProfileService;
	@Autowired private ProfileService profileService;
	@Autowired private AuthorityDao authorityDao;
	
	@Scheduled(cron="0 0 7 * * ?")
	//@Scheduled(cron="0 0/1 * * * ?")
	public void synchronize() {
		if (clientProfileService==null) return;
		logger.debug("Profiles synchronization..");
		
		ClientProfileFilterDefault filter = new ClientProfileFilterDefault(5);
		List<ClientProfile> profiles;
		do {
			profiles = clientProfileService.find(filter);
			logger.debug("Found "+profiles.size()+" profiles!");
			
			for (ClientProfile clientprofile : profiles) {
				synchronize(clientprofile);
				filter.next(clientprofile.getId());
			}
		} while (profiles!=null&&!profiles.isEmpty());
	}
	
	private void synchronize(final ClientProfile clientprofile) {
		logger.debug("Synchronizing: "+clientprofile.getLogin());
		
		Profile profile = profileService.findByUuid(clientprofile.getId());
		if (profile!=null&&profile.isActive()) {
			//..update description..
			modify(profile, clientprofile);
			return;
		}

		if (profile==null) {
			//..create new one..
			create(clientprofile);
		}
	}
	private void modify(Profile profile, final ClientProfile clientprofile) {
		logger.info("Profile "+clientprofile.getLogin()+" FOUND locally and is active. Updating description..");
		profile.modify(clientprofile.getDescription());
	}
		
	private void create(final ClientProfile clientprofile) {
		logger.info("Profile "+clientprofile.getLogin()+" NOT found locally. Creating one..");

		try {
			//..default add user authority..
			final Authority authority = authorityDao.findByName(Authority.USER);
			
			
			profileService.add(new ProfileGlobalCreation() {
				@Override
				public String getLogin() {
					return clientprofile.getLogin();
				}
	
				@Override
				public String getPasswd() {
					return null;
				}
	
				@Override
				public String getFirstName() {
					return clientprofile.getFirstName();
				}
	
				@Override
				public String getLastName() {
					return clientprofile.getLastName();
				}
	
				@Override
				public String getEmail() {
					return null;
				}
	
				@Override
				public String getPhone() {
					return null;
				}
	
				@Override
				public Address getAddress() {
					return null;
				}
	
				@Override
				public File getPhoto() {
					return null;
				}
	
				@Override
				public boolean isAccepted() {
					return false;
				}
	
				@Override
				public List<Authority> getAuthorities() {
					return authority!=null?Arrays.asList(authority):new ArrayList<Authority>();
				}
	
				@Override
				public List<Access> getAccesses() {
					return null;
				}

				@Override
				public String getUuid() {
					return clientprofile.getId();
				}

				@Override
				public List<ClientProfileAuthority> getClientAuthorities() {
					return clientprofile.getAuthorities();
				}

				@Override
				public String getDescription() {
					return clientprofile.getDescription();
				}
				
			});
		} catch (ProfileException e) {
			logger.error("Error synchronizing profile"+clientprofile.getId()+" Details: "+e.getMessage());
		}
		
	}
		
}
